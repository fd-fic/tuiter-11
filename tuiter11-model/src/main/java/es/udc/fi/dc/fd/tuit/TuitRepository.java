package es.udc.fi.dc.fd.tuit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.favorite.Favorite;
import es.udc.fi.dc.fd.favorite.FavoriteDto;
import es.udc.fi.dc.fd.favorite.FavoriteRepository;
import es.udc.fi.dc.fd.publish.PublishUtils;
import es.udc.fi.dc.fd.retuit.Retuit;
import es.udc.fi.dc.fd.retuit.RetuitDto;
import es.udc.fi.dc.fd.retuit.RetuitRepository;
import es.udc.fi.dc.fd.util.InstanceNotFoundException;

/**
 * The Class TuitRepository.
 */
@Repository
@Transactional(readOnly = true)
@ComponentScan("es.udc.fi.dc.fd")
public class TuitRepository {

	/** The account repository. */
	@Autowired
	private AccountRepository accountRepository;

	/** The favorite repository. */
	@Autowired
	private FavoriteRepository favoriteRepository;

	/** The retuit repository. */
	@Autowired
	private RetuitRepository retuitRepository;

	/** The mongo template. */
	@Autowired
	private MongoTemplate mongoTemplate;

	/** The publish utils. */
	@Autowired
	private PublishUtils publishUtils;

	/** The conversor. */
	@Autowired
	private TuitDtoToTuitConversor conversor;

	/**
	 * Instantiates a new tuit repository.
	 */
	public TuitRepository() {

	}

	/**
	 * Save.
	 *
	 * @param tuit
	 *            the tuit
	 * @return the tuit
	 */
	public Tuit save(Tuit tuit) {
		return saveWithParent(tuit, null, 0);
	}

	/**
	 * Saves an offensive tuit.
	 *
	 * @param tuit
	 *            the tuit
	 * @return the tuit
	 */
	public Tuit saveOffensiveTuit(Tuit tuit) {
		return saveWithParent(tuit, null, 1);
	}

	/**
	 * Save with parent.
	 *
	 * @param tuit
	 *            the tuit
	 * @param parent
	 *            the parent
	 * @param isOffensive
	 *            the is offensive
	 * @return the tuit
	 */
	@Transactional
	public Tuit saveWithParent(Tuit tuit, String parent, int isOffensive) {

		TuitDto tuitDto = new TuitDto(tuit.getFechaMs(), tuit.getTuit(), tuit
				.getAcc().getId(), parent, isOffensive);
		mongoTemplate.insert(tuitDto);
		tuit.setId(tuitDto.getId());

		return tuit;
	}

	/**
	 * Gets the offensive tuits.
	 *
	 * @return the offensive tuits
	 */
	public List<Tuit> getOffensiveTuits() {

		List<Tuit> tuits = new ArrayList<Tuit>();

		List<TuitDto> tuitDtos = mongoTemplate.find(
				Query.query(Criteria.where("isOffensive").is(1)).with(
						new Sort(Sort.Direction.DESC, "fechaMs")),
				TuitDto.class, "tuitDto");

		if (tuitDtos != null) {
			tuits = conversor.tuitDtosToTuits(tuitDtos);
		}

		return tuits;
	}

	/**
	 * Find by user email.
	 *
	 * @param email
	 *            the email
	 * @return the user's list of tuits
	 */
	public List<Tuit> findByUserEmail(String email) {

		Account accFromEmail = accountRepository.findByEmail(email);

		List<TuitDto> tuitsDtos = mongoTemplate.find(
				Query.query(
						Criteria.where("accountId").is(accFromEmail.getId()))
						.with(new Sort(Sort.Direction.DESC, "fechaMs")),
				TuitDto.class, "tuitDto");

		return conversor.tuitDtosToTuits(tuitsDtos);
	}

	/**
	 * Find by id.
	 *
	 * @param tuitId
	 *            the tuit id
	 * @return the tuit
	 */
	public Tuit findById(String tuitId) {

		TuitDto tuitDto = mongoTemplate.findOne(
				Query.query(Criteria.where("_id").is(tuitId)), TuitDto.class,
				"tuitDto");

		if (tuitDto == null) {
			throw new InstanceNotFoundException(
					"Unable to find the tuit with id: " + tuitId, Tuit.class);
		}

		return conversor.tuitDtoToTuit(tuitDto);
	}

	/**
	 * 
	 * @param keywords
	 * @return a tuit list
	 */
	public List<Tuit> findByKeyWords(String keywords) {

		Query query = new Query();
		query.addCriteria(Criteria.where("tuit").regex(keywords));

		List<TuitDto> tuitsDtos = mongoTemplate.find(query, TuitDto.class);

		return conversor.tuitDtosToTuits(tuitsDtos);

	}

	/**
	 * Find direct children.
	 *
	 * @param tuitDtoId
	 *            the tuit dto id
	 * @return the list
	 */
	private List<TuitDto> findDirectChildren(String tuitDtoId) {
		return mongoTemplate.find(
				Query.query(Criteria.where("parentTuitId").is(tuitDtoId)).with(
						new Sort(Sort.Direction.ASC, "fechaMs")),
				TuitDto.class, "tuitDto");

	}

	/**
	 * Gets the responses to a tuit.
	 *
	 * @param tuitId
	 *            the tuit id
	 * @param emailVisitor
	 *            the email visitor
	 * @return the responses
	 */
	public List<Tuit> getResponses(String tuitId, String emailVisitor) {

		Tuit tuit = null;
		List<Tuit> result = new ArrayList<Tuit>();
		List<TuitDto> tuitsRecuperados = new ArrayList<TuitDto>();

		TuitDto primeiroTuit = mongoTemplate.findOne(
				Query.query(Criteria.where("_id").is(tuitId)), TuitDto.class,
				"tuitDto");
		// Si no existe ese tuit, devolvemos la lista vacía
		if (primeiroTuit == null) {
			return result;
		}

		// Si existe, le añadimos todos sus hijos directos
		tuitsRecuperados.addAll(findDirectChildren(primeiroTuit.getId()));

		while (!tuitsRecuperados.isEmpty()) {
			// Para cada elemento del arbol, extraemos el primer elemento
			TuitDto t = tuitsRecuperados.remove(0);
			Account acc = accountRepository.findById(t.getAccountId());

			// Si tenemos permisos para visualizar el tuit, lo añadimos a la
			// lista
			if (publishUtils.grantedVisibility(emailVisitor, acc.getEmail())) {
				tuit = conversor.tuitDtoToTuit(t);
				result.add(tuit);
			}
			// Obtenemos los hijos del tuit procesado anteriormente
			List<TuitDto> fillos = findDirectChildren(t.getId());
			tuitsRecuperados.addAll(fillos);
		}

		// Ordenamos las respuestas
		Collections.sort(result, new TuitDateComparator());
		return result;
	}

	/**
	 * Gets the parents.
	 *
	 * @param tuitId
	 *            the tuit id
	 * @param emailVisitor
	 *            the principal
	 * @return the parents
	 */
	public List<Tuit> getParents(String tuitId, String emailVisitor) {

		Account acc = null;
		Tuit tuit = null;
		TuitDto tuitDto = mongoTemplate.findOne(
				Query.query(Criteria.where("_id").is(tuitId)), TuitDto.class,
				"tuitDto");
		List<Tuit> tuits = new ArrayList<Tuit>();
		do {
			tuitDto = mongoTemplate.findOne(
					Query.query(Criteria.where("_id").is(
							tuitDto.getParentTuitId())), TuitDto.class,
					"tuitDto");
			if (tuitDto != null) {
				acc = accountRepository.findById(tuitDto.getAccountId());
				if (publishUtils
						.grantedVisibility(emailVisitor, acc.getEmail())) {
					tuit = conversor.tuitDtoToTuit(tuitDto);
					tuits.add(tuit);
				}
			}
		} while (tuitDto != null);
		Collections.sort(tuits, new TuitDateComparator());
		return tuits;
	}

	/**
	 * Borrar tuit.
	 *
	 * @param tuit
	 *            the tuit
	 * @return the tuit
	 */
	@Transactional
	public Tuit borrarTuit(Tuit tuit) {

		// buscamos el tuit a borrar en bd
		TuitDto tuitDto = mongoTemplate.findOne(
				Query.query(Criteria.where("_id").is(tuit.getId())),
				TuitDto.class);

		if (tuitDto == null) {
			throw new InstanceNotFoundException(
					"Unable to find the tuit with id: " + tuit.getId(),
					Tuit.class);
		}

		// buscamos los favoritos asociados al tuit y si hay los borramos.
		List<Favorite> favTuits = favoriteRepository.findFavoritesByTuit(tuit
				.getId());
		if (favTuits != null) {
			for (Favorite f : favTuits) {
				mongoTemplate.remove(
						Query.query(Criteria.where("_id").is(f.getId())),
						FavoriteDto.class);
			}

		}

		// buscamos sus retuits asociados y si hay, los borramos.
		List<Retuit> retuits = retuitRepository.findRetuitsByTuit(tuit.getId());
		if (retuits != null) {
			for (Retuit r : retuits) {
				mongoTemplate.remove(
						Query.query(Criteria.where("_id").is(r.getId())),
						RetuitDto.class);
			}
		}

		// buscamos las respuestas asociadas al tuit y si hay las "deslinkamos".
		List<TuitDto> responses = findDirectChildren(tuitDto.getId());
		if (responses != null) {
			for (TuitDto t : responses) {
				t.setParentTuitId(null);
			}
		}
		// por ultimo borramos el tuit
		mongoTemplate.remove(tuitDto);

		return tuit;

	}

	/**
	 * The Class TuitDateComparator.
	 */
	static class TuitDateComparator implements Comparator<Tuit> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Tuit tuit1, Tuit tuit2) {
			return tuit1.getPublishedDate().compareTo(tuit2.getPublishedDate());
		}

	}
}
