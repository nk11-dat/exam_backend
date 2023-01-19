package dtos;

import entities.Conference;
import entities.Talk;
import entities.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link entities.Conference} entity
 */
public class UpdateConferenceDTO implements Serializable
{
    @Size(max = 45)
    private final String conferenceName;
    @Size(max = 45)
    @NotNull
    private final String location;
    @NotNull
    private final Integer capacity;
    @Size(max = 45)
    private final String strDate;
    private final Set<TalkDTO1> talks;

    public UpdateConferenceDTO(String conferenceName, String location, Integer capacity, String strDate, Set<TalkDTO1> talks) {
        this.conferenceName = conferenceName;
        this.location = location;
        this.capacity = capacity;
        this.strDate = strDate;
        this.talks = talks;
    }

    public UpdateConferenceDTO(Conference conference) {
        this.conferenceName = conference.getConferenceName();
        this.location = conference.getLocation();
        this.capacity = conference.getCapacity();
        this.strDate = conference.getStrDate();
        this.talks = new LinkedHashSet<>();
        conference.getTalks().forEach(talk -> this.talks.add(new TalkDTO1(talk)));
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public String getLocation() {
        return location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getStrDate() {
        return strDate;
    }

    public Set<TalkDTO1> getTalks() {
        return talks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateConferenceDTO entity = (UpdateConferenceDTO) o;
        return Objects.equals(this.conferenceName, entity.conferenceName) &&
                Objects.equals(this.location, entity.location) &&
                Objects.equals(this.capacity, entity.capacity) &&
                Objects.equals(this.strDate, entity.strDate) &&
                Objects.equals(this.talks, entity.talks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conferenceName, location, capacity, strDate, talks);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "conferenceName = " + conferenceName + ", " +
                "location = " + location + ", " +
                "capacity = " + capacity + ", " +
                "strDate = " + strDate + ", " +
                "talks = " + talks + ")";
    }

    /**
     * A DTO for the {@link entities.Talk} entity
     */
    public static class TalkDTO1 implements Serializable
    {
        private final Integer id;
//        @Size(max = 45)
//        @NotNull
//        private final String topic;
        private final Set<UserDTO1> users;

//        public TalkDTO1(Integer id, String topic, Set<UserDTO1> users) {
//            this.id = id;
//            this.topic = topic;
//            this.users = users;
//        }

        public TalkDTO1(Integer id, Set<UserDTO1> users) {
            this.id = id;
            this.users = users;
        }

        public TalkDTO1(Talk talk) {
            this.id = talk.getId();
//            this.topic = talk.getTopic();
            this.users = new LinkedHashSet<>();
            talk.getUsers().forEach(user -> this.users.add(new UserDTO1(user)));
        }

        public Integer getId() {
            return id;
        }

//        public String getTopic() {
//            return topic;
//        }

        public Set<UserDTO1> getUsers() {
            return users;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TalkDTO1 entity = (TalkDTO1) o;
            return Objects.equals(this.id, entity.id) &&
                    Objects.equals(this.users, entity.users);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, users);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                    "id = " + id + ", " +
//                    "topic = " + topic + ", " +
                    "users = " + users + ")";
        }

        /**
         * A DTO for the {@link entities.User} entity
         */
        public static class UserDTO1 implements Serializable
        {
            @Size(max = 25)
            private final String userName;
//            private final List<String> roleRoleNames;

//            public UserDTO1(String userName, List<String> roleRoleNames) {
//                this.userName = userName;
////                this.roleRoleNames = roleRoleNames;
//            }

//            public UserDTO1(User user) {
//                this.userName = user.getUserName();
//                this.roleRoleNames = user.getRolesAsStrings();
//            }

            public UserDTO1(String userName) {
                this.userName = userName;
            }
            public UserDTO1(User user) {
                this.userName = user.getUserName();
            }

            public String getUserName() {
                return userName;
            }

//            public List<String> getRoleRoleNames() {
//                return roleRoleNames;
//            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                UserDTO1 entity = (UserDTO1) o;
                return Objects.equals(this.userName, entity.userName);
            }

            @Override
            public int hashCode() {
                return Objects.hash(userName);
            }

            @Override
            public String toString() {
                return getClass().getSimpleName() + "(" +
                        "userName = " + userName + ")";
            }
        }
    }
}