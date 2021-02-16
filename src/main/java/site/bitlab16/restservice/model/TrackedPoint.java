package site.bitlab16.restservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;


@Entity
public class TrackedPoint {
    private @Id @GeneratedValue Long id;
    private String placeName;
    private int peopleConcentration;

    public TrackedPoint() {}

    public TrackedPoint(Long id, String placeName, int peopleConcentration){
        this.id = id;
        this.placeName = placeName;
        this.peopleConcentration = peopleConcentration;
    }

    public TrackedPoint(String placeName, int peopleConcentration){
        this.placeName = placeName;
        this.peopleConcentration = peopleConcentration;
    }

    @Override
    public String toString() {
        return "TrackedPoint{" +
                "id=" + id +
                ", placeName='" + placeName + '\'' +
                ", peopleConcentration=" + peopleConcentration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackedPoint)) return false;

        TrackedPoint that = (TrackedPoint) o;

        if (peopleConcentration != that.peopleConcentration) return false;
        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(placeName, that.placeName);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (placeName != null ? placeName.hashCode() : 0);
        result = 31 * result + peopleConcentration;
        return result;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setPeopleConcentration(int peopleConcentration) {
        this.peopleConcentration = peopleConcentration;
    }

    public Long getId() {
        return id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public int getPeopleConcentration() {
        return peopleConcentration;
    }
}
