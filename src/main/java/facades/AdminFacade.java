package facades;

import dtos.ConferenceDTO;
import dtos.TalkDTO;
import dtos.UserDTO;
import entities.Conference;
import entities.Role;
import entities.Talk;
import entities.User;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;

/**
 * @author lam@cphbusiness.dk
 */
public class AdminFacade
{

    private static EntityManagerFactory emf;
    private static AdminFacade instance;

    private AdminFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static AdminFacade getAdminFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AdminFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public UserDTO createSpeaker(UserDTO userDTO)
    {
        EntityManager em = emf.createEntityManager();
        try
        {

            em.getTransaction().begin();
            User temp = em.find(User.class, userDTO.getUserName());
            if (temp != null)
                throw new WebApplicationException("User with that username already exists.");

            User user = new User(userDTO.getUserName(), userDTO.getUserPass());
            Role role = em.find(Role.class, "speaker");
            user.addRole(role);
            em.persist(user);
            em.getTransaction().commit();
            return new UserDTO(user);
        }
        finally
        {
            em.close();
        }
    }

    public ConferenceDTO createConference(ConferenceDTO conferenceDTO)
    {
        EntityManager em = emf.createEntityManager();
        try
        {
            em.getTransaction().begin();
            Conference temp = em.find(Conference.class, conferenceDTO.getConferenceName());
            if (temp != null) {
                throw new WebApplicationException("Failed to create Conference. Conference called: " + conferenceDTO.getConferenceName() + ", already exists.");
            }

            Conference conference = new Conference(conferenceDTO.getConferenceName(), conferenceDTO.getLocation(), conferenceDTO.getCapacity(), conferenceDTO.getStrDate());
            em.persist(conference);
            em.getTransaction().commit();
            return new ConferenceDTO(conference);
        }
        finally
        {
            em.close();
        }
    }

    public TalkDTO createTalk(TalkDTO talkDTO)
    {
        EntityManager em = emf.createEntityManager();
        try
        {
            em.getTransaction().begin();
            Conference con = em.find(Conference.class, talkDTO.getConferenceConferenceName());
            if (con == null)
                throw new WebApplicationException("Failed to create Talk. Couldn't find the specified Conference: " + talkDTO.getConferenceConferenceName());

            Talk talk = new Talk(con, talkDTO.getTopic(), talkDTO.getDuration(), talkDTO.getPropsList());
            em.persist(talk);
            em.getTransaction().commit();
            return new TalkDTO(talk);
        }
        finally
        {
            em.close();
        }
    }

    public TalkDTO deleteTalk(Integer talkId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Talk talkToDelete = em.find(Talk.class, talkId);
            if (talkToDelete == null)
                throw new WebApplicationException("Failed to delete talk, reason: Couldn't find talk with ID: "+talkId);
            em.remove(talkToDelete);

            em.flush();
            em.getTransaction().commit();
            return new TalkDTO(talkToDelete);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }
}
