package facades;

import dtos.ConferenceDTO;
import dtos.TalkDTO;
import dtos.UpdateConferenceDTO;
import dtos.UserDTO;
import entities.Conference;
import entities.Role;
import entities.Talk;
import entities.User;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;
import java.util.LinkedHashSet;
import java.util.Set;

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

    public UpdateConferenceDTO updateConference(UpdateConferenceDTO conferenceInput) {
        EntityManager em = emf.createEntityManager();
        try {
            //Check if ID is somehow missing for the boat exists
            if (conferenceInput.getConferenceName().equals("") )
                throw new WebApplicationException("Missing ConferenceName, can't update");

            em.getTransaction().begin();
            Conference conference = em.find(Conference.class, conferenceInput.getConferenceName());
            if (conference == null) //Check if boat exists in DB
                throw new WebApplicationException("Couldn't find any conference with name: " + conferenceInput.getConferenceName());
            conference.setCapacity(conferenceInput.getCapacity());    //If you only wanna update if these attributes contain data put them inside an if() statement!
            conference.setLocation(conferenceInput.getLocation());      //If you only wanna update if these attributes contain data put them inside an if() statement!
            conference.setStrDate(conferenceInput.getStrDate());    //If you only wanna update if these attributes contain data put them inside an if() statement!

            //TODO: As an admin I would like to update all information about a conference, its talks, and the speakers
            Set<Talk> talks = new LinkedHashSet<>();
            Talk talk;
            if (conferenceInput.getTalks().size() > 0 && conference.getTalks() != null) {
                for (UpdateConferenceDTO.TalkDTO1 talkDTO1 : conferenceInput.getTalks()) {
                    talk = em.find(Talk.class, talkDTO1.getId()); //Find the talk based on ID

                    if (talkDTO1.getUsers().size() > 0 && talkDTO1.getUsers() != null){ //Add speakers
                        for (UpdateConferenceDTO.TalkDTO1.UserDTO1 user : talkDTO1.getUsers()) { //loop through the speakers for the given talk
                            User speaker = em.find(User.class, user.getUserName()); //Find speaker
                            Role role = em.find(Role.class, "speaker");
                            if (speaker.getRoles().contains(role))
                                talk.addSpeaker(speaker); //Add speaker to the chosen Talk
                            else throw new WebApplicationException("User: " + speaker.getUserName() + ", is not a a speaker. Only speakers are allowed to host talks.");
                        }
                    } else throw new WebApplicationException("You tried adding a talk, with no speaker!");

                    talk.setConference(conference); //Update the talk's conference
                    talks.add(talk); //Add the talk to the Set
                }
                conference.setTalks(talks); //Finally add all the talks to the conference!
            } else throw new WebApplicationException("Talk set empty or null.");

            Conference result = em.merge(conference);
            em.flush();
            em.getTransaction().commit();
            return new UpdateConferenceDTO(result);
        } catch (Exception e) {
            throw new WebApplicationException("Failed to update boat");
        } finally {
            em.close();
        }
    }
}
