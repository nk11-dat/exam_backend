package entities;

import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users")
@NamedQuery(name = "User.deleteAllRows", query = "DELETE from User")  //TODO: Remember this if you have to recreate from DB using JPABuddy!
public class User
{
    @Id
    @Size(max = 25)
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "user_name", nullable = false, length = 25)
    private String userName;

    @Size(max = 255)
    @Column(name = "user_pass")
    private String userPass;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_name"),
            inverseJoinColumns = @JoinColumn(name = "role_name"))
    private List<Role> roles = new ArrayList<>();

    public User() {}

    public User(String userName, String userPass) {
        this.userName = userName;
        this.userPass = BCrypt.hashpw(userPass,BCrypt.gensalt());
    }

//    public User(String userName, String userPass, String address, String phone) {
//        this.userName = userName;
//        this.userPass = userPass;
//        this.address = address;
//        this.phone = phone;
//    }

    public List<String> getRolesAsStrings() {
        if (roles.isEmpty()) {
            return null;
        }
        List<String> rolesAsStrings = new ArrayList<>();
        roles.forEach((role) -> {
            rolesAsStrings.add(role.getRoleName());
        });
        return rolesAsStrings;
    }

    public boolean verifyPassword(String pw){
        return BCrypt.checkpw(pw , this.userPass);
    }

    public void addRole(Role userRole) {
        roles.add(userRole);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getUserName(), user.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName());
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userPass='" + userPass + '\'' +
                ", roles=" + roles +
                '}';
    }
}