package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ConferenceDTO;
import dtos.TalkDTO;
import dtos.UserDTO;
import facades.AdminFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
@Path("admin")
public class AdminResource
{

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    UserFacade userFacade = UserFacade.getUserFacade(EMF);
    AdminFacade adminFacade = AdminFacade.getAdminFacade(EMF);

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String userResourceReached() {
        return "{\"msg\":\"User Resource is working!\"}";
    }

    @GET
    @Path("all/speakers")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "speaker", "admin"})
    public String getAllSpeakers() {
        List<UserDTO> userDTOList = userFacade.getAllSpeakers();
        return GSON.toJson(userDTOList);
    }

    @GET
    @Path("all/conferences")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"speaker", "admin"})
    public String getAllConferences() {
        //US1 = As a user I would like to see all conferences
        List<ConferenceDTO> conferenceDTOList = userFacade.getAllConferences();
        return GSON.toJson(conferenceDTOList);
    }

    //TODO: US4 = As an admin I would like to create new conferences, talks and speakers
    @POST
    @Path("post/conference")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public String createConference(String input) {
        ConferenceDTO conferenceInput = GSON.fromJson(input, ConferenceDTO.class);
        ConferenceDTO createdConference = adminFacade.createConference(conferenceInput);
        return GSON.toJson(createdConference);
    }

    @POST
    @Path("post/talk")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public String createTalk(String input) {
        TalkDTO talkInput = GSON.fromJson(input, TalkDTO.class);
        TalkDTO createdTalk = adminFacade.createTalk(talkInput);
        return GSON.toJson(createdTalk);
    }

    @POST
    @Path("post/speaker")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String createUser(String input){
        UserDTO userDTO = GSON.fromJson(input, UserDTO.class);
        UserDTO newSpeaker = userFacade.createUser(userDTO);
        return GSON.toJson(newSpeaker);
    }
}