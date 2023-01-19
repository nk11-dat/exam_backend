package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.WebApplicationException;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "talk")
@NamedQuery(name = "Talk.deleteAllRows", query = "DELETE from Talk")
public class Talk
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "talk_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "conference_name", nullable = false)
    private Conference conference;

    @Size(max = 45)
    @NotNull
    @Column(name = "topic", nullable = false, length = 45)
    private String topic;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Size(max = 45)
    @Column(name = "props_list", length = 45)
    private String propsList;

    @ManyToMany
    @JoinTable(name = "users_talk",
            joinColumns = @JoinColumn(name = "talk_id"),
            inverseJoinColumns = @JoinColumn(name = "user_name"))
    private Set<User> users = new LinkedHashSet<>();

    public Talk() {
    }

    public Talk(Conference conference, String topic, Integer duration, String propsList) {
        this.conference = conference;
        this.topic = topic;
        this.duration = duration;
        this.propsList = propsList;
    }

    public Talk(Conference conference, String topic, Integer duration, String propsList, Set<User> users) {
        this.conference = conference;
        this.topic = topic;
        this.duration = duration;
        this.propsList = propsList;
        this.users = users;
    }

    public void addSpeaker(User speaker) {
//        if (speaker.getRoles().contains(new Role("speaker"))) //Skulle tilføje en entitymanger for at finde den rigtige role entity...
        this.users.add(speaker);
//        else throw new WebApplicationException("Only speakers are allowed to host talks. User: " + speaker.getUserName() + ", is not a speaker.");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getPropsList() {
        return propsList;
    }

    public void setPropsList(String propsList) {
        this.propsList = propsList;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}