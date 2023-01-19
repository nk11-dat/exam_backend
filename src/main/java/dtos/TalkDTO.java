package dtos;

import entities.Conference;
import entities.Talk;
import entities.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

/**
 * A DTO for the {@link entities.Talk} entity
 */
public class TalkDTO implements Serializable
{
    private Integer id;
    @Size(max = 45)
    private String conferenceConferenceName;
    @Size(max = 45)
    @NotNull
    private String conferenceLocation;
    @NotNull
    private Integer conferenceCapacity;
    @Size(max = 45)
    private String conferenceStrDate;
    @Size(max = 45)
    @NotNull
    private String topic;
    @NotNull
    private Integer duration;
    @Size(max = 45)
    private String propsList;
    private Set<UserDTO> users;

    public TalkDTO(Integer id, String conferenceConferenceName, String conferenceLocation, Integer conferenceCapacity, String conferenceStrDate, String topic, Integer duration, String propsList, Set<UserDTO> users) {
        this.id = id;
        this.conferenceConferenceName = conferenceConferenceName;
        this.conferenceLocation = conferenceLocation;
        this.conferenceCapacity = conferenceCapacity;
        this.conferenceStrDate = conferenceStrDate;
        this.topic = topic;
        this.duration = duration;
        this.propsList = propsList;
        this.users = users;
    }

    public TalkDTO(Talk talk) {
        this.id = talk.getId();
        if (talk.getConference() != null){ //needed for delete talk
            this.conferenceConferenceName = talk.getConference().getConferenceName();
            this.conferenceLocation = talk.getConference().getLocation();
            this.conferenceCapacity = talk.getConference().getCapacity();
            this.conferenceStrDate = talk.getConference().getStrDate();
        }
        this.topic = talk.getTopic();
        this.duration = talk.getDuration();
        this.propsList = talk.getPropsList();
        this.users = null;
        if (talk.getUsers().size() > 0)
            this.users = new LinkedHashSet<>();
            talk.getUsers().forEach(user -> this.users.add(new TalkDTO.UserDTO(user)));
    }

    public static List<TalkDTO> getDTOs(List<Talk> talks) {
        List<TalkDTO> talkDTOList = new ArrayList<>();
        talks.forEach(talk -> talkDTOList.add(new TalkDTO(talk)));
        return talkDTOList;
    }
    public Integer getId() {
        return id;
    }

    public String getConferenceConferenceName() {
        return conferenceConferenceName;
    }

    public String getConferenceLocation() {
        return conferenceLocation;
    }

    public Integer getConferenceCapacity() {
        return conferenceCapacity;
    }

    public String getConferenceStrDate() {
        return conferenceStrDate;
    }

    public String getTopic() {
        return topic;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getPropsList() {
        return propsList;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TalkDTO entity = (TalkDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.conferenceConferenceName, entity.conferenceConferenceName) &&
                Objects.equals(this.conferenceLocation, entity.conferenceLocation) &&
                Objects.equals(this.conferenceCapacity, entity.conferenceCapacity) &&
                Objects.equals(this.conferenceStrDate, entity.conferenceStrDate) &&
                Objects.equals(this.topic, entity.topic) &&
                Objects.equals(this.duration, entity.duration) &&
                Objects.equals(this.propsList, entity.propsList) &&
                Objects.equals(this.users, entity.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, conferenceConferenceName, conferenceLocation, conferenceCapacity, conferenceStrDate, topic, duration, propsList, users);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "conferenceConferenceName = " + conferenceConferenceName + ", " +
                "conferenceLocation = " + conferenceLocation + ", " +
                "conferenceCapacity = " + conferenceCapacity + ", " +
                "conferenceStrDate = " + conferenceStrDate + ", " +
                "topic = " + topic + ", " +
                "duration = " + duration + ", " +
                "propsList = " + propsList + ", " +
                "users = " + users + ")";
    }

    /**
     * A DTO for the {@link entities.User} entity
     */
    public static class UserDTO implements Serializable
    {
        @Size(max = 25)
        private final String userName;
        @Size(max = 255)
        @NotNull
        private final String userPass;
        @Size(max = 45)
        private final String profession;
        @Size(max = 20)
        private final String gender;

        public UserDTO(String userName, String userPass, String profession, String gender) {
            this.userName = userName;
            this.userPass = userPass;
            this.profession = profession;
            this.gender = gender;
        }

        public UserDTO(User user) {
            this.userName = user.getUserName();
            this.userPass = user.getUserPass();
            this.profession = user.getProfession();
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserDTO entity = (UserDTO) o;
            return Objects.equals(this.userName, entity.userName) &&
                    Objects.equals(this.userPass, entity.userPass) &&
                    Objects.equals(this.profession, entity.profession) &&
                    Objects.equals(this.gender, entity.gender);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userName, userPass, profession, gender);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                    "userName = " + userName + ", " +
                    "userPass = " + userPass + ", " +
                    "profession = " + profession + ", " +
                    "gender = " + gender + ")";
        }
    }
}