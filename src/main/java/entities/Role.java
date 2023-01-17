package entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "roles")
@NamedQuery(name = "Role.deleteAllRows", query = "DELETE from Role")
public class Role
{
    @Id
    @Size(max = 20)
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "role_name", nullable = false, length = 20)
    private String roleName;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "role_name"),
            inverseJoinColumns = @JoinColumn(name = "user_name"))
    private List<User> users = new ArrayList<>();

    public Role() {
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equals(getRoleName(), role.getRoleName()) && Objects.equals(getUsers(), role.getUsers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleName(), getUsers());
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleName='" + roleName + '\'' +
                ", users=" + users +
                '}';
    }
}