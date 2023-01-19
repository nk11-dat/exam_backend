package rest;

import dtos.ConferenceDTO;
import dtos.UpdateConferenceDTO;
import entities.Conference;
import entities.Role;
import entities.Talk;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AdminEndpointTest
{

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    User user, admin, both;
    Talk t1, t2, t3;
    Conference c1, c2;
    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
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

            Role userRole = new Role("speaker");
            Role adminRole = new Role("admin");
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
            em.flush();
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

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String userName, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", userName, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void serverIsRunning() {
        given().when().get("/info").then().statusCode(200);
    }

    @Test
    public void createConference() {
        login("Ib Ibsen", "test2");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(new ConferenceDTO("testCon", "Here or there", 10, "2024-05-11",null))
                .when()
                .post("/admin/post/conference")
                .then()
                .body("conferenceName", equalTo("testCon"))
                .body("location", equalTo("Here or there"))
                .body("capacity", equalTo(10))
                .body("strDate", equalTo("2024-05-11"));
//                .body("talks", equalTo("Hmmmm...."));
    }

    @Test
    public void testUnauthorizedCreateConference() {
        login("Bo Bobsen", "test1");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(new ConferenceDTO("testCon", "Here or there", 10, "2024-05-11",null))
                .when()
                .post("/admin/post/conference")
                .then()
                .statusCode(401);
        //TODO: Maybe also test message?
    }

    @Test
    public void UpdateCon() {
        login("Ib Ibsen", "test2");
        Set<UpdateConferenceDTO.TalkDTO1.UserDTO1> userDTO1s = new LinkedHashSet<>();
        userDTO1s.add(new UpdateConferenceDTO.TalkDTO1.UserDTO1(user.getUserName()));

        UpdateConferenceDTO.TalkDTO1 tDTO = new UpdateConferenceDTO.TalkDTO1(1, userDTO1s);
        Set<UpdateConferenceDTO.TalkDTO1> talkDTO1s = new LinkedHashSet<>();
        talkDTO1s.add(tDTO);

        UpdateConferenceDTO changeTo = new UpdateConferenceDTO("24timer", "her", 1337, "2025-01-01", talkDTO1s);

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(changeTo)
                .when()
                .put("/admin/put/conference")
                .then()
                .body("conferenceName", equalTo("24timer"))
                .body("location", equalTo("her"))
                .body("capacity", equalTo(1337))
                .body("strDate", equalTo("2025-01-01"))
                .body("talks[0].id", equalTo((int)tDTO.getId()))
                .body("talks[0].users[0].userName", equalTo(user.getUserName()));
        //TODO: Kunne ikke finde ud af at sammenligne med hele Set'et  :(
    }

    @Test
    public void deleteTalk() {
        login("Ib Ibsen", "test2");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete("/admin/delete/talk/1")
                .then()
                .body("id", equalTo(1))
                .body("topic", equalTo("t1"))
                .body("duration", equalTo(1440))
                .body("propsList", equalTo("PC, and anxity Medecin"));
    }

}
