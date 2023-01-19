package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ConferenceDTO;
import dtos.TalkDTO;
import dtos.UpdateConferenceDTO;
import dtos.UserDTO;
import entities.Role;
import entities.User;
import facades.AdminFacade;
import facades.UserFacade;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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

    @GET
    @Path("all/talks")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"speaker", "admin"})
    public String getAllTalks() {
        List<TalkDTO> talkDTOList = userFacade.getAllTalks();
        return GSON.toJson(talkDTOList);
    }

    //TODO: US4 = As an admin I would like to create new conferences, talks and speakers
    @POST
    @Path("post/conference")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public String createConference(String input) { //As an admin I would like to create new conferences
        ConferenceDTO conferenceInput = GSON.fromJson(input, ConferenceDTO.class);
        ConferenceDTO createdConference = adminFacade.createConference(conferenceInput);
        return GSON.toJson(createdConference);
    }

    @POST
    @Path("post/talk")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public String createTalk(String input) { //As an admin I would like to create new talks
        TalkDTO talkInput = GSON.fromJson(input, TalkDTO.class);
        TalkDTO createdTalk = adminFacade.createTalk(talkInput);
        return GSON.toJson(createdTalk);
    }

    @POST
    @Path("post/speaker")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String createUser(String input){ //As an admin I would like to create new speakers
        UserDTO userDTO = GSON.fromJson(input, UserDTO.class);
        UserDTO newSpeaker = userFacade.createUser(userDTO);
        return GSON.toJson(newSpeaker);
    }
    //TODO: US6 = As an admin I would like to update all information about a conference, its talks, and the speakers
    @PUT
    @Path("put/conference")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public String updateBoat(String input) {
        //TODO: US5 As an admin I would like to update all information about a boat, its owner and its harbour
//        User users = null;
//        try {
//            users = userFacade.getVeryfiedUser("nicki", "qwer");
//        } catch (AuthenticationException e) {
//            throw new RuntimeException(e);
//        }
//        List<String> roles = new ArrayList<>();
//        users.getRoles().forEach(role -> roles.add(role.getRoleName()));
//
//        Set<UpdateConferenceDTO.TalkDTO1.UserDTO1> userDTO1s = new LinkedHashSet<>();
//        userDTO1s.add(new UpdateConferenceDTO.TalkDTO1.UserDTO1(users.getUserName(), roles));
////        new UpdateConferenceDTO.TalkDTO1.UserDTO1(users.getUserName(), roles);
//        Set<UpdateConferenceDTO.TalkDTO1> talkDTO1s = new LinkedHashSet<>();
//        talkDTO1s.add(new UpdateConferenceDTO.TalkDTO1(1, "cheesecake", userDTO1s));
//
//        UpdateConferenceDTO test = new UpdateConferenceDTO("Abe", "her", 1337, "2025-12-12", talkDTO1s);
//        return GSON.toJson(test);
        UpdateConferenceDTO conferenceInput = GSON.fromJson(input, UpdateConferenceDTO.class);
        UpdateConferenceDTO updatedConference = adminFacade.updateConference(conferenceInput);
        return GSON.toJson(updatedConference);

        //last attempt before i copy into new project....
    }


    //TODO: US7 = As an admin I would like to delete a talk
    @Path("delete/talk/{id}")
    @DELETE
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteTalk(@PathParam("id") Integer talkId) throws ExecutionException, InterruptedException, IOException
    {
        TalkDTO deletedTalk = adminFacade.deleteTalk(talkId);
        return GSON.toJson(deletedTalk);
    }
}