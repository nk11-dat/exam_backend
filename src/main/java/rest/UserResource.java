package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ConferenceDTO;
import dtos.UserDTO;
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
@Path("user")
public class UserResource
{

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    UserFacade FACADE = UserFacade.getUserFacade(EMF);

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
        List<UserDTO> userDTOList = FACADE.getAllSpeakers();
        return GSON.toJson(userDTOList);
    }

    @GET
    @Path("all/conferences")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"speaker", "admin"})
    public String getAllConferences() {
        //US1 = As a user I would like to see all conferences
        List<ConferenceDTO> conferenceDTOList = FACADE.getAllConferences();
        return GSON.toJson(conferenceDTOList);
    }

    //TODO: HUSK NÅR DU LAVER NY Resource AT TILFØJE DEN TIL ApplicationConfig!!!
}