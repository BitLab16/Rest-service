package site.bitlab16.restservice.model;

import org.springframework.data.geo.Point;
import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "tracked_point")
public class TrackedPoint {

    @Id
    private Long id;

    @Column(name = "point_name")
    private String name;

    @Column(name = "code")
    private Long code;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private Point location;

    public TrackedPoint() {}

    public TrackedPoint(Long id, String name, Long code, String description, Point location) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.location = location;
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
        if (o == null || getClass() != o.getClass()) return false;
        TrackedPoint that = (TrackedPoint) o;
        return id.equals(that.id) && name.equals(that.name) && code.equals(that.code) && Objects.equals(description, that.description) && location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, description, location);
    }

    @Override
    public String toString() {
        return "TrackedPoint{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code=" + code +
                ", description='" + description + '\'' +
                ", location='" + location.toString() + '\'' +
                '}';
    }
}
