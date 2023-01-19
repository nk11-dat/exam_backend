package facades;

import dtos.UserDTO;
import entities.Role;
import entities.User;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;

    User user, admin, both;
    Role userRole, adminRole;

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

            user = new User("Bo Bobsen", "test1");
            admin = new User("Ib Ibsen", "test2");
            both = new User("Ib Bobsen", "test3");

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
            em.flush();

            em.getTransaction().commit();

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
