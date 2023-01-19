package rest;

import dtos.ConferenceDTO;
import dtos.UserDTO;
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
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class UserEndpointTest
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
    public void getAllConferences() {
        login("Bo Bobsen", "test1");
        List<ConferenceDTO> ConferenceDTO =
                given()
                        .contentType("application/json")
                        .accept(ContentType.JSON)
                        .header("x-access-token", securityToken)
                        .when()
                        .get("/user/all/conferences").then()
                        .statusCode(200).extract().body().jsonPath().getList("", ConferenceDTO.class);

        ConferenceDTO cDTO1 = new ConferenceDTO(c1);
        ConferenceDTO cDTO2 = new ConferenceDTO(c2);

        assertThat(ConferenceDTO, containsInAnyOrder(cDTO1, cDTO2));  //compares using DTOs equals method, which only looks at username
    }

    @Test
    public void getAllSpeakers() {
        login("Bo Bobsen", "test1");
        List<UserDTO> userDTOs =
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/user/all/speakers").then()
                .statusCode(200).extract().body().jsonPath().getList("", UserDTO.class);

        System.out.println();
        UserDTO u1 = new UserDTO(user);
        UserDTO u2 = new UserDTO(both);

        assertThat(userDTOs, containsInAnyOrder(u1, u2));  //compares using DTOs equals method, which only looks at username
    }

}
