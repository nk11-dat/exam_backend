package facades;

import dtos.ConferenceDTO;
import dtos.TalkDTO;
import dtos.UserDTO;
import entities.Conference;
import entities.Role;
import entities.Talk;
import entities.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class AdminFacadeTest
{

    private static EntityManagerFactory emf;
    private static AdminFacade facade;

    User user, admin, both;
    Role userRole, adminRole;
    Talk t1, t2, t3;
    Conference c1, c2;

    public AdminFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = AdminFacade.getAdminFacade(emf);
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
            em.createNativeQuery("ALTER TABLE talk AUTO_INCREMENT = 1").executeUpdate();
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
    public void createConference() throws Exception {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Conference> query = em.createQuery("SELECT c FROM Conference c", Conference.class);
        List<Conference> conferenceList = query.getResultList();
        assertEquals(2, conferenceList.size()); //before boat creation

        ConferenceDTO input = new ConferenceDTO("testCon", "Here or there", 10, "2024-05-11",null);
        ConferenceDTO createdCon = facade.createConference(input);

        // Check if the correct boat was created
        Conference actual = em.find(Conference.class, "testCon");
        ConferenceDTO foundCon = new ConferenceDTO(actual);
        assertEquals(createdCon, foundCon);

        // Check if count was increased
        conferenceList = query.getResultList();
        assertEquals(3, conferenceList.size()); //after boat creation
    }

    @Test
    public void deleteTalk() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        //find boat before it is deleted so that it can be compared later
        Talk boatBefore = em.find(Talk.class, t1.getId());
        TalkDTO beforeDTO = new TalkDTO(boatBefore);

        //count talks before deletion to compare later
        TypedQuery<Talk> query = em.createQuery("SELECT t FROM Talk t", Talk.class);
        List<Talk> talkCountBefore = query.getResultList();

        TalkDTO deletedBoat = facade.deleteTalk(t1.getId());

        List<Talk> talkCountAfter = query.getResultList();

        assertEquals(beforeDTO, deletedBoat); //check if the correct talk was deleted
        assertEquals(talkCountBefore.size()-1, talkCountAfter.size());

        em.getTransaction().commit();
        em.close();
    }

}
