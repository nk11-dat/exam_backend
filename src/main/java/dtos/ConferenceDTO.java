package dtos;

import entities.Conference;
import entities.Talk;
import entities.User;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

/**
 * A DTO for the {@link entities.Conference} entity
 */
public class ConferenceDTO implements Serializable
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
    private final Set<TalkDTO> talks;

    public ConferenceDTO(String conferenceName, String location, Integer capacity, String strDate, Set<TalkDTO> talks) {
        this.conferenceName = conferenceName;
        this.location = location;
        this.capacity = capacity;
        this.strDate = strDate;
        this.talks = talks;
    }

    public ConferenceDTO(Conference conference) {
        this.conferenceName = conference.getConferenceName();
        this.location = conference.getLocation();
        this.capacity = conference.getCapacity();
        this.strDate = conference.getStrDate();
        this.talks = new LinkedHashSet<>();
        conference.getTalks().forEach(talk -> this.talks.add(new TalkDTO(talk)));
    }

    public static List<ConferenceDTO> getDTOs(List<Conference> conferences) {
        List<ConferenceDTO> conferenceDTOList = new ArrayList<>();
        conferences.forEach(conference -> conferenceDTOList.add(new ConferenceDTO(conference)));
        return conferenceDTOList;
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

    public Set<TalkDTO> getTalks() {
        return talks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConferenceDTO entity = (ConferenceDTO) o;
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
    public static class TalkDTO implements Serializable
    {
        private final Integer id;
        @Size(max = 45)
        @NotNull
        private final String topic;
        @NotNull
        private final Integer duration;
        @Size(max = 45)
        private final String propsList;

        private final Set<UserDTO> users;

//        public TalkDTO(Integer id, String topic, Integer duration, String propsList) {
//            this.id = id;
//            this.topic = topic;
//            this.duration = duration;
//            this.propsList = propsList;
//        }


        public TalkDTO(Integer id, String topic, Integer duration, String propsList, Set<UserDTO> users) {
            this.id = id;
            this.topic = topic;
            this.duration = duration;
            this.propsList = propsList;
            this.users = users;
        }

        public TalkDTO(Talk talk) {
            this.id = talk.getId();
            this.topic = talk.getTopic();
            this.duration = talk.getDuration();
            this.propsList = talk.getPropsList();
            this.users = new LinkedHashSet<>();
            talk.getUsers().forEach(user -> this.users.add(new UserDTO(user)));
        }

        public Integer getId() {
            return id;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TalkDTO entity = (TalkDTO) o;
            return Objects.equals(this.id, entity.id) &&
                    Objects.equals(this.topic, entity.topic) &&
                    Objects.equals(this.duration, entity.duration) &&
                    Objects.equals(this.propsList, entity.propsList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, topic, duration, propsList);
        }

        @Override
        public String toString() {
            return "TalkDTO{" +
                    "id=" + id +
                    ", topic='" + topic + '\'' +
                    ", duration=" + duration +
                    ", propsList='" + propsList + '\'' +
                    ", users=" + users +
                    '}';
        }

        public static class UserDTO implements Serializable
        {
            @Size(max = 25)
            private final String userName;
            @Size(max = 45)
            private final String profession;

            @Size(max = 20)
            private final String gender;

            public UserDTO(String userName, String profession, String gender) {
                this.userName = userName;
                this.profession = profession;
                this.gender = gender;
            }

            public UserDTO(User user) {
                this.userName = user.getUserName();
                this.profession = user.getProfession();
                this.gender = user.getGender();
            }

            public String getUserName() {
                return userName;
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
                ConferenceDTO.TalkDTO.UserDTO entity = (ConferenceDTO.TalkDTO.UserDTO) o;
                return Objects.equals(this.userName, entity.userName);
            }

            @Override
            public int hashCode() {
                return Objects.hash(userName);
            }

            @Override
            public String toString() {
                return "UserDTO{" +
                        "userName='" + userName + '\'' +
                        ", profession='" + profession + '\'' +
                        ", gender='" + gender + '\'' +
                        '}';
            }
        }
    }
}