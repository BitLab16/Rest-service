package site.bitlab16.restservice.model;

import java.lang.Object;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class TrackedPoint {
    private @Id @GeneratedValue Long id;
    private String placeName;
    private int peopleConcentration;

    public TrackedPoint() {}

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
        if (o == null || getClass() != o.getClass()) return false;
        TrackedPoint that = (TrackedPoint) o;
        return peopleConcentration == that.peopleConcentration && Objects.equals(id, that.id) && Objects.equals(placeName, that.placeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placeName, peopleConcentration);
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
