package site.bitlab16.restservice.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "gatherings_detection")
public class Gathering {

    @Id
    @Column(
            name = "id"
    )
    @GeneratedValue(strategy=GenerationType.TABLE)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "tracked_point_id", nullable = false)
    private TrackedPoint point;

    @Column(
            name = "detection_time",
            nullable = false)
    private Timestamp detectionTime;

    @Column(
            name = "season",
            nullable = false
    )
    private Season season;

    @Column(
            name = "holiday",
            nullable = false
    )
    private boolean isHoliday;

    @Column(name = "time_index")
    private Long timeIndex;

    @Column(name = "weather_index")
    private Long weatherIndex;

    @Column(name = "season_index")
    private Long seasonIndex;

    @Column(name = "attractions_index")
    private Long attractionIndex;

    public Gathering(Long id,
                     TrackedPoint point,
                     Timestamp detectionTime,
                     Season season,
                     boolean isHoliday,
                     Long timeIndex,
                     Long weatherIndex,
                     Long seasonIndex,
                     Long attractionIndex) {
        this.id = id;
        this.point = point;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gathering gathering = (Gathering) o;
        return isHoliday == gathering.isHoliday &&
                id.equals(gathering.id) && point.equals(gathering.point) &&
                detectionTime.equals(gathering.detectionTime) && season == gathering.season &&
                Objects.equals(timeIndex, gathering.timeIndex) &&
                Objects.equals(weatherIndex, gathering.weatherIndex) &&
                Objects.equals(seasonIndex, gathering.seasonIndex) &&
                Objects.equals(attractionIndex, gathering.attractionIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, point, detectionTime, season, isHoliday, timeIndex, weatherIndex, seasonIndex, attractionIndex);
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
