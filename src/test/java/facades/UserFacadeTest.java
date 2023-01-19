package facades;

import dtos.ConferenceDTO;
import dtos.UserDTO;
import entities.Conference;
import entities.Role;
import entities.Talk;
import entities.User;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;

    User user, admin, both;
    Role userRole, adminRole;
    Talk t1, t2, t3;
    Conference c1, c2;

    public UserFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Talk.deleteAllRows").executeUpdate();
            em.createNamedQuery("Conference.deleteAllRows").executeUpdate();

            user = new User("Bo Bobsen", "test1");
            admin = new User("Ib Ibsen", "test2");
            both = new User("Ib Bobsen", "test3");
            c1 = new Conference("24timer", "location1", 1, "2023-01-19");
            c2 = new Conference("eksamen", "location2", 3, "2023-01-23");
            t1 = new Talk(c1, "t1", 1440, "PC, and anxity Medecin");
            t2 = new Talk(c1, "t2", 13, "PC, and anxity Medecin");
            t3 = new Talk(c2, "t3", 30, "PC, and anxity Medecin");

            if(admin.getUserPass().equals("test")||user.getUserPass().equals("test")||both.getUserPass().equals("test"))
                throw new UnsupportedOperationException("You have not changed the passwords");

            userRole = new Role("speaker");
            adminRole = new Role("admin");
            user.addRole(userRole);
            admin.addRole(adminRole);
            both.addRole(userRole);
            both.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);

            em.persist(user);
            em.persist(admin);
            em.persist(both);
            em.persist(c1);
            em.persist(c2);
            em.flush();
            em.persist(t1);
            em.persist(t2);
            em.persist(t3);
            em.flush();

            em.getTransaction().commit();

            em.refresh(c1);
            em.refresh(c2);

        } finally {
            em.close();
        }
    }

    // TODO: Delete or change this method
//    @Disabled
    @Test
    public void getVerifiedUser() throws Exception {
        User actual = facade.getVeryfiedUser(user.getUserName(), "test1");
        User expected = user; //How to test hashed password?
        assertEquals(expected, actual);
    }

    @Test
    public void getAllConferences() throws Exception {
        List<ConferenceDTO> actual = facade.getAllConferences();
        assertEquals(2, actual.size());

        ConferenceDTO cDTO1 = new ConferenceDTO(c1);
        ConferenceDTO cDTO2 = new ConferenceDTO(c2);
        assertThat(actual, containsInAnyOrder(cDTO1, cDTO2));

        //TODO: How to invert ContainsInAnyOrder?
//        Set<ConferenceDTO.TalkDTO> failMe = new LinkedHashSet<>();
//        failMe.add(new ConferenceDTO.TalkDTO(9, "cheese", 55, "nothing"));
//        ConferenceDTO notThere = new ConferenceDTO("derp", "haha", 12, "12-12-2024", failMe);
//        assertThat(actual, !containsInAnyOrder(notThere));
    }
    @Test
    public void getAllSpeakers() throws Exception {
        List<UserDTO> actual = facade.getAllSpeakers();
        assertEquals(2, actual.size());
        //FIXME: Compare elements of list...  should i:
        // - include id's with the DTO?
        // - change the hash & equalsto method in user?
        for (UserDTO userDTO : actual) {
            switch (userDTO.getUserName()){
                case "Ib Ibsen":
                    assertEquals(user.getUserName(), userDTO.getUserName());
                    break;
            }
        }
    }

}
