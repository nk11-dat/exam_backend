package dtos;

import entities.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link entities.User} entity
 */
public class UserDTO implements Serializable
{
    @NotNull
    private final String userName;
    @NotNull
    @Size(min = 1, max = 255)
    private final String userPass;
    @Size(max = 45)
    private String profession;
    @Size(max = 45)
    private String gender;

    public UserDTO(String userName, String userPass) {
        this.userName = userName;
        this.userPass = userPass;
    }

    public UserDTO(String userName, String userPass, String profession, String gender) {
        this.userName = userName;
        this.userPass = userPass;
        this.profession = profession;
        this.gender = gender;
    }

    public UserDTO(String userName) {
        this.userName = userName;
        this.userPass = "XXX";
    }

    public UserDTO(User user) {
        this.userName = user.getUserName();
        this.userPass = user.getUserPass();
        if (user.getProfession() != null && !user.getProfession().equals(""))
            this.profession = user.getProfession();
        if (user.getGender() != null && !user.getGender().equals(""))
            this.gender = user.getGender();
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public String getProfession() {
        return profession;
    }

    public String getGender() {
        return gender;
    }

    public static List<UserDTO> getDTOs(List<User> users)
    {
        List<UserDTO> userDTOList = new ArrayList<>();
        users.forEach(user ->  userDTOList.add(new UserDTO(user)));
        return userDTOList;
    }

    public static List<UserDTO> getDTOsWithoutPass(List<User> users)
    {
        List<UserDTO> userDTOList = new ArrayList<>();
        users.forEach(user ->  userDTOList.add(new UserDTO(user.getUserName())));
        return userDTOList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(getUserName(), userDTO.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName());
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userName='" + userName + '\'' +
                ", userPass='" + userPass + '\'' +
                ", profession='" + profession + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
