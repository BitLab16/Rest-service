package site.bitlab16.restservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "gatherings_detection")
@JsonRootName(value = "gathering")
public class Gathering {

    @Id
    @Column(
            name = "id"
    )
    @GeneratedValue(strategy=GenerationType.TABLE)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "tracked_point_id", nullable = false)
    @JsonBackReference
    private TrackedPoint point;

    @Column(
            name = "people_concentration",
            nullable = false
    )
    private int flow;

    @Column(
            name = "detection_time",
            nullable = false)
    private Timestamp detectionTime;

    @Column(
            name = "season",
            nullable = false
    )
    @JsonIgnore
    private Season season;

    @Column(
            name = "holiday",
            nullable = false
    )
    @JsonIgnore
    private boolean isHoliday;

    @Column(name = "time_index")
    @JsonIgnore
    private Long timeIndex;

    @Column(name = "weather_index")
    @JsonIgnore
    private Long weatherIndex;

    @Column(name = "season_index")
    @JsonIgnore
    private Long seasonIndex;

    @Column(name = "attractions_index")
    @JsonIgnore
    private Long attractionIndex;

    public Gathering(Long id,
                     TrackedPoint point,
                     int flow,
                     Timestamp detectionTime,
                     Season season,
                     boolean isHoliday,
                     Long timeIndex,
                     Long weatherIndex,
                     Long seasonIndex,
                     Long attractionIndex) {
        this.id = id;
        this.point = point;
        this.flow = flow;
        this.detectionTime = detectionTime;
        this.season = season;
        this.isHoliday = isHoliday;
        this.timeIndex = timeIndex;
        this.weatherIndex = weatherIndex;
        this.seasonIndex = seasonIndex;
        this.attractionIndex = attractionIndex;
    }

    public Gathering() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrackedPoint getPoint() {
        return point;
    }

    public void setPoint(TrackedPoint point) {
        this.point = point;
    }

    public Timestamp getDetectionTime() {
        return detectionTime;
    }

    public void setDetectionTime(Timestamp detectionTime) {
        this.detectionTime = detectionTime;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }

    public Long getTimeIndex() {
        return timeIndex;
    }

    public void setTimeIndex(Long timeIndex) {
        this.timeIndex = timeIndex;
    }

    public Long getWeatherIndex() {
        return weatherIndex;
    }

    public void setWeatherIndex(Long weatherIndex) {
        this.weatherIndex = weatherIndex;
    }

    public Long getSeasonIndex() {
        return seasonIndex;
    }

    public void setSeasonIndex(Long seasonIndex) {
        this.seasonIndex = seasonIndex;
    }

    public Long getAttractionIndex() {
        return attractionIndex;
    }

    public void setAttractionIndex(Long attractionIndex) {
        this.attractionIndex = attractionIndex;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gathering gathering = (Gathering) o;

        if (flow != gathering.flow) return false;
        if (isHoliday != gathering.isHoliday) return false;
        if (!id.equals(gathering.id)) return false;
        if (!point.equals(gathering.point)) return false;
        if (!detectionTime.equals(gathering.detectionTime)) return false;
        if (season != gathering.season) return false;
        if (timeIndex != null ? !timeIndex.equals(gathering.timeIndex) : gathering.timeIndex != null) return false;
        if (weatherIndex != null ? !weatherIndex.equals(gathering.weatherIndex) : gathering.weatherIndex != null)
            return false;
        if (seasonIndex != null ? !seasonIndex.equals(gathering.seasonIndex) : gathering.seasonIndex != null)
            return false;
        return attractionIndex != null ? attractionIndex.equals(gathering.attractionIndex) : gathering.attractionIndex == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + point.hashCode();
        result = 31 * result + flow;
        result = 31 * result + detectionTime.hashCode();
        result = 31 * result + season.hashCode();
        result = 31 * result + (isHoliday ? 1 : 0);
        result = 31 * result + (timeIndex != null ? timeIndex.hashCode() : 0);
        result = 31 * result + (weatherIndex != null ? weatherIndex.hashCode() : 0);
        result = 31 * result + (seasonIndex != null ? seasonIndex.hashCode() : 0);
        result = 31 * result + (attractionIndex != null ? attractionIndex.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Gathering{" +
                "id=" + id +
                ", point_id=" + point +
                ", detectionTime=" + detectionTime +
                ", season=" + season +
                ", isHoliday=" + isHoliday +
                ", timeIndex=" + timeIndex +
                ", weatherIndex=" + weatherIndex +
                ", seasonIndex=" + seasonIndex +
                ", attractionIndex=" + attractionIndex +
                '}';
    }
}
