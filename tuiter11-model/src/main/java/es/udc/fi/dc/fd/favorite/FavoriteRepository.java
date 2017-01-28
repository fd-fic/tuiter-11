package es.udc.fi.dc.fd.favorite;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.publish.PublishUtils;
import es.udc.fi.dc.fd.tuit.Tuit;
import es.udc.fi.dc.fd.tuit.TuitRepository;
import es.udc.fi.dc.fd.util.InstanceNotFoundException;


/**
 * The Class FavoriteRepository.
 */
@Repository
@Transactional(readOnly = true)
@ComponentScan("es.udc.fi.dc.fd")
public class FavoriteRepository {

    /** The mongo template. */
    @Autowired
    private MongoTemplate mongoTemplate;

    /** The account repository. */
    @Autowired
    private AccountRepository accountRepository;

    /** The tuit repository. */
    @Autowired
    private TuitRepository tuitRepository;

    /** The publish utils. */
    @Autowired
    private PublishUtils publishUtils;

    /**
     * Instantiates a new favorite repository.
     */
    public FavoriteRepository() {

    }

    /**
     * Save.
     *
     * @param favorite
     *            the favorite
     * @return the favorite
     */
    @Transactional
    public Favorite save(Favorite favorite) {

        FavoriteDto favoriteDto = new FavoriteDto(favorite.getTuit().getId(),
                favorite.getOwner().getId());
        mongoTemplate.insert(favoriteDto);

        favorite.setId(favoriteDto.getId());

        return favorite;
    }

    /**
     * Delete favorite.
     *
     * @param favorite
     *            the favorite
     */
    @Transactional
    public void deleteFavorite(Favorite favorite) {

        FavoriteDto favoriteDto = mongoTemplate.findOne(
                Query.query(Criteria.where("_id").is(favorite.getId())),
                FavoriteDto.class);

        mongoTemplate.remove(favoriteDto);
    }

    /**
     * Return all Favorites made by the user whith that email.
     *
     * @param email
     *            the email
     * @return the list
     */
    public List<Favorite> findFavoritesBy(String email) {

        Account acc = accountRepository.findByEmail(email);

        List<FavoriteDto> favoritesDtos = mongoTemplate.find(
                Query.query(Criteria.where("ownerId").is(acc.getId())),
                FavoriteDto.class);

        List<Favorite> obtainedFavorites = favoriteDtosToFavoritesConv(favoritesDtos);

        List<Favorite> result = new ArrayList<Favorite>();
        for (Favorite f : obtainedFavorites) {
            if (publishUtils.grantedVisibility(email, f.getTuit().getAcc()
                    .getEmail())) {
                result.add(f);
            }
        }
        return result;
    }

    /**
     * Return all Tuits favoriteed by the user whith that email.
     *
     * @param email
     *            the email
     * @return the list
     */
    public List<Tuit> findFavoriteTuitsOf(String email) {

        List<Tuit> tuits = new ArrayList<Tuit>();
        for (Favorite r : findFavoritesBy(email)) {
            tuits.add(r.getTuit());
        }
        return tuits;
    }

    /**
     * Return a Favorite, made by the user whit email and whit id tuitId.
     *
     * @param tuitId
     *            the tuit id
     * @param email
     *            the email
     * @return the favorite
     */
    public Favorite findByTuitAndEmail(String tuitId, String email) {
        Account acc = accountRepository.findByEmail(email);

        FavoriteDto favoriteDto = mongoTemplate.findOne(
                Query.query(Criteria.where("tuitId").is(tuitId).and("ownerId")
                        .is(acc.getId())), FavoriteDto.class);

        if (favoriteDto == null) {
            throw new InstanceNotFoundException("The user with email: " + email
                    + " doesn't have the tuit: " + tuitId + " like favorite.",
                    Favorite.class);
        }

        return favoriteDtoToFavoriteConv(favoriteDto);
    }

    /**
     * Return true if the user whit email made a favorite to the favorite whith
     * tuitId.
     *
     * @param tuitId
     *            the tuit id
     * @param email
     *            the email
     * @return a boolean
     */
    public boolean wasMarkedAsFavorite(String tuitId, String email) {
        Account acc = accountRepository.findByEmail(email);

        return mongoTemplate.count(
                Query.query(Criteria.where("tuitId").is(tuitId).and("ownerId")
                        .is(acc.getId())), FavoriteDto.class) > 0;
    }

    /**
     * Find favorites by tuit.
     *
     * @param tuitId
     *            the tuit id
     * @return the list
     */
    public List<Favorite> findFavoritesByTuit(String tuitId) {

        tuitRepository.findById(tuitId);

        List<FavoriteDto> favs = mongoTemplate.find(
                Query.query(Criteria.where("tuitId").is(tuitId)),
                FavoriteDto.class);

        return favoriteDtosToFavoritesConv(favs);

    }

    /**
     * Favorite dtos to favorites conv.
     *
     * @param favoritesDtos
     *            the favorites dtos
     * @return the list
     */
    private List<Favorite> favoriteDtosToFavoritesConv(
            List<FavoriteDto> favoritesDtos) {
        List<Favorite> result = new ArrayList<>();
        for (FavoriteDto rDto : favoritesDtos) {
            result.add(favoriteDtoToFavoriteConv(rDto));
        }
        return result;
    }

    /**
     * Favorite dto to favorite conv.
     *
     * @param favoriteDto
     *            the favorite dto
     * @return the favorite
     */
    private Favorite favoriteDtoToFavoriteConv(FavoriteDto favoriteDto) {

        Favorite favorite = new Favorite(tuitRepository.findById(favoriteDto
                .getTuitId()), accountRepository.findById(favoriteDto
                .getOwnerId()));
        favorite.setId(favoriteDto.getId());
        return favorite;
    }
}
