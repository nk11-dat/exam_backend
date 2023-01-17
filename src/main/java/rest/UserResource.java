package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin"})
    public String getListOfOwners() {
        //US1 = As a user I would like to see all owners
        List<UserDTO> userDTOList = FACADE.getAllUsers();
        System.out.println("");
        return GSON.toJson(userDTOList);
    }

    //TODO: HUSK NÅR DU LAVER NY Resource AT TILFØJE DEN TIL ApplicationConfig!!!
}