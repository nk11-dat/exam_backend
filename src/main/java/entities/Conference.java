package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "conference")
@NamedQuery(name = "Conference.deleteAllRows", query = "DELETE from Conference")
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

    @Size(max = 45)
    @Column(name = "strDate", length = 45)
    private String strDate;

    @OneToMany(mappedBy = "conference")
    private Set<Talk> talks = new LinkedHashSet<>();

    public Conference() {
    }

    public Conference(String conferenceName, String location, Integer capacity, String strDate) {
        this.conferenceName = conferenceName;
        this.location = location;
        this.capacity = capacity;
        this.strDate = strDate;
    }

    public Conference(String conferenceName, String location, Integer capacity, String strDate, Set<Talk> talks) {
        this.conferenceName = conferenceName;
        this.location = location;
        this.capacity = capacity;
        this.strDate = strDate;
        this.talks = talks;
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

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public Set<Talk> getTalks() {
        return talks;
    }

    public void setTalks(Set<Talk> talks) {
        this.talks = talks;
    }

}