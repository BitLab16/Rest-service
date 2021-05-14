package site.bitlab16.restservice.model;

public class Indexes {

    private long timeIndex;
    private long weatherIndex;
    private long seasonIndex;
    private long attractionsIndex;

    public Indexes(long timeIndex, long weatherIndex, long seasonIndex, long attractionsIndex) {
        this.timeIndex = timeIndex;
        this.weatherIndex = weatherIndex;
        this.seasonIndex = seasonIndex;
        this.attractionsIndex = attractionsIndex;
    }

    public long getTimeIndex() {
        return timeIndex;
    }

    public long getWeatherIndex() {
        return weatherIndex;
    }

    public long getSeasonIndex() {
        return seasonIndex;
    }

    public long getAttractionsIndex() {
        return attractionsIndex;
    }

    @Override
    public String toString() {
        return "Index{" +
                "timeIndex=" + timeIndex +
                ", weatherIndex=" + weatherIndex +
                ", seasonIndex=" + seasonIndex +
                ", attractionsIndex=" + attractionsIndex +
                '}';
    }
}
