package site.bitlab16.restservice.model;

import java.sql.Timestamp;

public class TimeInformation {

    private Timestamp detectionTime;
    private int weather;
    private Season season;
    private boolean isHoliday;

    public TimeInformation(Timestamp detectionTime, int weather, Season season, boolean isHoliday) {
        this.detectionTime = detectionTime;
        this.weather = weather;
        this.season = season;
        this.isHoliday = isHoliday;
    }

    public Timestamp getDetectionTime() {
        return detectionTime;
    }

    public int getWeather() {
        return weather;
    }

    public Season getSeason() {
        return season;
    }

    public boolean isHoliday() {
        return isHoliday;
    }
}
