package es.udc.fi.dc.fd.tuit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import es.udc.fi.dc.fd.account.Account;
import es.udc.fi.dc.fd.account.AccountRepository;
import es.udc.fi.dc.fd.account.UserService;


/**
 * The Class TuitMockitoTest.
 */
@RunWith(MockitoJUnitRunner.class)
public class TuitMockitoTest {

    /** The user service. */
    @InjectMocks
    private UserService userService = new UserService();

    /** The account repository mock. */
    @Mock
    private AccountRepository accountRepositoryMock;

    /** The tuit repository mock. */
    @Mock
    private TuitRepository tuitRepositoryMock;

    /** The thrown. */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Creates the new tuit and find it.
     */
    @Test
    public void createNewTuitAndFindIt() {
        // arrange
        Account cuentaPepe = new Account("pepe@example.com", "pepe", "demo",
                "ROLE_USER");
        Account cuentaManolo = new Account("manolo@example.com", "manolo",
                "demo", "ROLE_USER");
        Calendar fecha = Calendar.getInstance();
        fecha.set(Calendar.MILLISECOND, 0);
        Tuit tuitPepe = new Tuit(fecha.getTimeInMillis(), "tuit de prueba",
                cuentaPepe);
        Tuit tuitManolo = new Tuit(fecha.getTimeInMillis(), "tuit de prueba2",
                cuentaManolo);
        Tuit tuitManolo2 = new Tuit(fecha.getTimeInMillis(), "tuit de prueba3",
                cuentaManolo);

        when(tuitRepositoryMock.save(tuitPepe)).thenReturn(tuitPepe);
        when(tuitRepositoryMock.save(tuitManolo)).thenReturn(tuitManolo);
        when(tuitRepositoryMock.save(tuitManolo2)).thenReturn(tuitManolo2);

        List<Tuit> tuitsPepe = new ArrayList<Tuit>();
        List<Tuit> tuitsManolo = new ArrayList<Tuit>();
        tuitsPepe.add(tuitRepositoryMock.save(tuitPepe));
        tuitsManolo.add(tuitRepositoryMock.save(tuitManolo));
        tuitsManolo.add(tuitRepositoryMock.save(tuitManolo2));

        // assert
        assertThat(tuitsPepe.size()).isEqualTo(1);
        assertThat(tuitsManolo.size()).isEqualTo(2);

        assertThat(tuitPepe.getTuit()).isEqualTo(tuitsPepe.get(0).getTuit());
        assertThat(tuitPepe.getAcc().getEmail()).isEqualTo(
                tuitsPepe.get(0).getAcc().getEmail());
        assertThat(tuitPepe.getFecha()).isEqualTo(tuitsPepe.get(0).getFecha());

        assertThat(tuitManolo.getAcc().getEmail()).isEqualTo(
                tuitsManolo.get(0).getAcc().getEmail());
        assertThat(tuitManolo.getFecha()).isEqualTo(
                tuitsManolo.get(0).getFecha());
        assertThat(tuitManolo.getTuit())
                .isEqualTo(tuitsManolo.get(0).getTuit());

        assertThat(tuitManolo2.getAcc().getEmail()).isEqualTo(
                tuitsManolo.get(1).getAcc().getEmail());
        assertThat(tuitManolo2.getFecha()).isEqualTo(
                tuitsManolo.get(1).getFecha());
        assertThat(tuitManolo2.getTuit()).isEqualTo(
                tuitsManolo.get(1).getTuit());

    }

}