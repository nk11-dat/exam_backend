package facades;

import dtos.UserDTO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import entities.Role;
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
            User user = new User(userDTO.getUserName(), userDTO.getUserPass());
            em.getTransaction().begin();
            Role role = em.find(Role.class, "user");
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


    public List<UserDTO> getAllUsers() {
        EntityManager em = emf.createEntityManager();
        try
        {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = :role", User.class);
            query.setParameter("role", "user");
            List<User> owners = query.getResultList();
            return UserDTO.getDTOsWithoutPass(owners);
        }
        finally
        {
            em.close();
        }
    }
}
