package site.bitlab16.restservice.model;

import com.fasterxml.jackson.annotation.*;
import site.bitlab16.restservice.model.jackson_view.View;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "gatherings_detection")
@JsonRootName(value = "gathering")
public class Gathering implements Serializable {

    @Id
    @Column(
            name = "id"
    )
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @Column(
            name = "tracked_point_id",
            nullable = false
    )
    private Long point;


    @Column(
            name = "people_concentration",
            nullable = false
    )
    @JsonView(View.Summary.class)
    private int flow;

    @Column(
            name = "detection_time",
            nullable = false)
    @JsonView(View.Summary.class)
    private Timestamp detectionTime;

    @Column(
            name = "weather",
            nullable = false
    )
    private int weather;


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
    private Long attractionsIndex;

    public Gathering(Long id,
                     Long point,
                     int flow,
                     Timestamp detectionTime,
                     int weather,
                     Season season,
                     boolean isHoliday,
                     Long timeIndex,
                     Long weatherIndex,
                     Long seasonIndex,
                     Long attractionsIndex) {
        this(point, flow, detectionTime, weather, season, isHoliday, timeIndex, weatherIndex, seasonIndex, attractionsIndex);
        this.id = id;
    }

    public Gathering(Long point,
                     int flow,
                     Timestamp detectionTime,
                     int weather,
                     Season season,
                     boolean isHoliday,
                     Long timeIndex,
                     Long weatherIndex,
                     Long seasonIndex,
                     Long attractionsIndex) {
        this.point = point;
        this.flow = flow;
        this.detectionTime = detectionTime;
        this.weather = weather;
        this.season = season;
        this.isHoliday = isHoliday;
        this.timeIndex = timeIndex;
        this.weatherIndex = weatherIndex;
        this.seasonIndex = seasonIndex;
        this.attractionsIndex = attractionsIndex;
    }

    public Gathering() { }

    public int getWeather() {
        return weather;
    }

    public void setWeather(int weather) {
        this.weather = weather;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
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

    public Long getAttractionsIndex() {
        return attractionsIndex;
    }

    public void setAttractionsIndex(Long attractionsIndex) {
        this.attractionsIndex = attractionsIndex;
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
        if (!(o instanceof Gathering)) return false;

        var gathering = (Gathering) o;

        if (flow != gathering.flow) return false;
        if (isHoliday != gathering.isHoliday) return false;
        if (!id.equals(gathering.id)) return false;
        if (!detectionTime.equals(gathering.detectionTime)) return false;
        if (season != gathering.season) return false;
        if (!Objects.equals(timeIndex, gathering.timeIndex)) return false;
        if (!Objects.equals(weatherIndex, gathering.weatherIndex))
            return false;
        if (!Objects.equals(seasonIndex, gathering.seasonIndex))
            return false;
        return Objects.equals(attractionsIndex, gathering.attractionsIndex);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + point.hashCode();
        result = 31 * result + flow;
        result = 31 * result + detectionTime.hashCode();
        result = 31 * result + weather;
        result = 31 * result + season.hashCode();
        result = 31 * result + (isHoliday ? 1 : 0);
        result = 31 * result + timeIndex.hashCode();
        result = 31 * result + weatherIndex.hashCode();
        result = 31 * result + seasonIndex.hashCode();
        result = 31 * result + attractionsIndex.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Gathering{" +
                "id=" + id +
                ", point=" + point +
                ", flow=" + flow +
                ", detectionTime=" + detectionTime +
                ", weather=" + weather +
                ", season=" + season +
                ", isHoliday=" + isHoliday +
                ", timeIndex=" + timeIndex +
                ", weatherIndex=" + weatherIndex +
                ", seasonIndex=" + seasonIndex +
                ", attractionsIndex=" + attractionsIndex +
                '}';
    }
}
