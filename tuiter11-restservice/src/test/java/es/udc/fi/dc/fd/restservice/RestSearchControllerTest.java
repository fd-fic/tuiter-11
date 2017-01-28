package es.udc.fi.dc.fd.restservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.config.WebSecurityConfigurationAware;
import es.udc.fi.dc.fd.tuit.CustomTuit;
import es.udc.fi.dc.fd.tuit.Tuit;
import es.udc.fi.dc.fd.tuit.TuitList;
import es.udc.fi.dc.fd.tuit.TuitRepository;

// TODO: Auto-generated Javadoc
/**
 * The Class RestSearchControllerTest.
 */
public class RestSearchControllerTest extends WebSecurityConfigurationAware {

	/** The account repository. */
	@Autowired
	public AccountRepository accountRepository;

	/** The tuit repository. */
	@Autowired
	public TuitRepository tuitRepository;

	/**
	 * Gets the tuits test.
	 *
	 * @return the tuits test
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void getTuitsTest() throws Exception {

		List<CustomTuit> tuits = new ArrayList<CustomTuit>();
		TuitList tuitList = new TuitList(tuits);

		mockMvc.perform(get("/")).andReturn().equals(tuitList);

		Account acc = accountRepository.findByEmail("user");
		Calendar fecha = Calendar.getInstance();
		fecha.set(Calendar.MILLISECOND, 0);
		Tuit tuit = new Tuit(fecha.getTimeInMillis(), "Hola holaaaaaa", acc);
		Tuit tuit2 = new Tuit(fecha.getTimeInMillis(), "Segunda prueba hola",
				acc);
		tuitRepository.save(tuit);
		tuitRepository.save(tuit2);

		CustomTuit custom = new CustomTuit(acc.getNickName(), fecha,
				tuit.getTuit());
		CustomTuit custom2 = new CustomTuit(acc.getNickName(), fecha,
				tuit2.getTuit());
		tuits.add(custom);
		tuits.add(custom2);
		tuitList.setTuits(tuits);

		mockMvc.perform(get("/?keywords=hola")).andReturn().equals(tuitList);

	}
}
