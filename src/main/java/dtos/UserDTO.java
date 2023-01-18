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
//    @Size(max = 45)
//    private final String address;
//    @Size(max = 45)
//    private final String phone;

    public UserDTO(String userName, String userPass) {
        this.userName = userName;
        this.userPass = userPass;
    }

    public UserDTO(String userName) {
        this.userName = userName;
        this.userPass = "XXX";
    }

    public UserDTO(User user) {
        this.userName = user.getUserName();
        this.userPass = user.getUserPass();
//        this.address = user.getAddress();
//        this.phone = user.getPhone();
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPass() {
        return userPass;
    }

//    public String getAddress() {
//        return address;
//    }
//
//    public String getPhone() {
//        return phone;
//    }

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
        return "UserInnerDTO{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
