package facades;

import dtos.ConferenceDTO;
import dtos.TalkDTO;
import dtos.UserDTO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;

import entities.Conference;
import entities.Role;
import entities.Talk;
import entities.User;
import security.errorhandling.AuthenticationException;

import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
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

    public UserDTO createUser(UserDTO userDTO)
    {
        EntityManager em = emf.createEntityManager();
        try
        {
            User user;
            if (!userDTO.getGender().equals("") && !userDTO.getProfession().equals(""))
                user = new User(userDTO.getUserName(), userDTO.getUserPass(), userDTO.getProfession(), userDTO.getGender());
            else
                user = new User(userDTO.getUserName(), userDTO.getUserPass());
            em.getTransaction().begin();
            Role role = em.find(Role.class, "speaker");
            user.addRole(role);

            User temp = em.find(User.class, userDTO.getUserName());
            if (temp != null)
                throw new WebApplicationException("A user(speaker), with this username already exists: " + userDTO.getUserName());

            em.persist(user);
            em.getTransaction().commit();
            return new UserDTO(user);
        }
        finally
        {
            em.close();
        }
    }


    public List<UserDTO> getAllSpeakers() {
        EntityManager em = emf.createEntityManager();
        try
        {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = :role", User.class);
            query.setParameter("role", "speaker");
            List<User> owners = query.getResultList();
            return UserDTO.getDTOsWithoutPass(owners);
        }
        finally
        {
            em.close();
        }
    }

    public List<ConferenceDTO> getAllConferences() {
        EntityManager em = emf.createEntityManager();
        try
        {
            TypedQuery<Conference> query = em.createQuery("SELECT c FROM Conference c", Conference.class);
            List<Conference> conferenceList = query.getResultList();
            return ConferenceDTO.getDTOs(conferenceList);
        }
        finally
        {
            em.close();
        }
    }

    public List<TalkDTO> getAllTalks() {
        EntityManager em = emf.createEntityManager();
        try
        {
            TypedQuery<Talk> query = em.createQuery("SELECT t FROM Talk t", Talk.class);
            List<Talk> talkList = query.getResultList();
            return TalkDTO.getDTOs(talkList);
        }
        finally
        {
            em.close();
        }
    }

    public ConferenceDTO getSpecificConference(String name) {
        EntityManager em = emf.createEntityManager();
        try
        {
            TypedQuery<Conference> query = em.createQuery("SELECT c FROM Conference c WHERE c.conferenceName = :name", Conference.class);
            query.setParameter("name", name);
            Conference conference = query.getSingleResult();
            return new ConferenceDTO(conference);
        }
        finally
        {
            em.close();
        }
    }
}
