package site.bitlab16.restservice.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.locationtech.jts.geom.Point;
import org.n52.jackson.datatype.jts.GeometryDeserializer;
import org.n52.jackson.datatype.jts.GeometrySerializer;
import site.bitlab16.restservice.model.jackson_view.View;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "tracked_point")
@JsonRootName(value = "tracked_point")
public class TrackedPoint implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "point_name")
    @JsonView(View.Summary.class)
    private String name;

    @Column(name = "code")
    @JsonView(View.Summary.class)
    private Long code;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    @JsonView(View.Summary.class)
    private Point location;

    @JsonView(View.Summary.class)
    @Transient
    private List<Gathering> gatherings = new ArrayList<>();

    public TrackedPoint() {}

    public TrackedPoint(Long id, String name, Long code, String description, Point location) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.location = location;
    }

    public void addGatherings(List<Gathering> gatherings) {
        this.gatherings.addAll(gatherings);
    }

    public List<Gathering> getGatherings() {
        return gatherings;
    }

    public void setGatherings(List<Gathering> gatherings) {
        this.gatherings = gatherings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Point getLocation() { return location; }

    public void setLocation(Point location) { this.location = location; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackedPoint)) return false;

        TrackedPoint that = (TrackedPoint) o;

        if (!id.equals(that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!code.equals(that.code)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!location.equals(that.location)) return false;
        return Objects.equals(gatherings, that.gatherings);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + code.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + location.hashCode();
        result = 31 * result + (gatherings != null ? gatherings.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TrackedPoint{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code=" + code +
                ", description='" + description + '\'' +
                ", location=" + location +
                ", gatherings=" + gatherings +
                '}';
    }
}
