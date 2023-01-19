package entities;

import errorhandling.API_Exception;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "conference")
@NamedQuery(name = "Conference.deleteAllRows", query = "DELETE from Conference ")
public class Conference
{
    @Id
    @Size(max = 45)
    @Column(name = "name", nullable = false, length = 45)
    private String conferenceName;

    @Size(max = 45)
    @NotNull
    @Column(name = "location", nullable = false, length = 45)
    private String location;

    @NotNull
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "date")
    private LocalDate date;

    @OneToMany(mappedBy = "conference")
    private Set<Talk> talks = new LinkedHashSet<>();

    public Conference() {
    }

    public Conference(String conferenceName, String location, Integer capacity) {
        this.conferenceName = conferenceName;
        this.location = location;
        this.capacity = capacity;
    }

    public Conference(String conferenceName, String location, Integer capacity, LocalDate date) {
        this.conferenceName = conferenceName;
        this.location = location;
        this.capacity = capacity;
        this.date = date;
    }

    public void addTalk(Talk talk) throws API_Exception {
        if (talks.size() < capacity)
        {
            talks.add(talk);
            talk.setConference(this);
        }
        else throw new API_Exception("Harbour " + this.conferenceName + ", is at max capacity", this.capacity); //google says errorcode 6 is overflow error ¯\_(ツ)_/¯
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<Talk> getTalks() {
        return talks;
    }

    public void setTalks(Set<Talk> talks) {
        this.talks = talks;
    }

}