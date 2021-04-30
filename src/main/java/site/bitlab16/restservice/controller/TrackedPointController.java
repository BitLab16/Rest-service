package site.bitlab16.restservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.*;
import site.bitlab16.restservice.exception.PointNotFoundException;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.model.TrackedPointStatistic;
import site.bitlab16.restservice.model.jackson_view.View;
import site.bitlab16.restservice.service.TrackedPointService;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

@RestController
@CrossOrigin(origins = { "http://localhost:8081", "http://localhost:8080" })
public class TrackedPointController {

    private final TrackedPointService pointService;

    public TrackedPointController(TrackedPointService pointService) {
        this.pointService = pointService;
    }

    @JsonView(View.Summary.class)
    @GetMapping(value= "/points")
    public Collection<TrackedPoint> getCurrentDayGatheringDetected() {
        Timestamp currTime = new Timestamp(System.currentTimeMillis());
        return pointService.dayHoursGatherings(Date.valueOf(currTime.toLocalDateTime().toLocalDate()));
    }

    @JsonView(View.PointInfo.class)
    @GetMapping(value = "/point/{id}")
    public TrackedPoint pointDetailsBasedOnId(@PathVariable("id") Long id) {
        return pointService.findByCode(id)
                .orElseThrow(() -> new PointNotFoundException(id));
    }

    @JsonView(View.Summary.class)
    @GetMapping(value= "/point/{code}/full-day")
    public TrackedPoint dayGatheringForPointGivenCode(@PathVariable("code") Long code) {
        Timestamp currTime = new Timestamp(System.currentTimeMillis());
        return pointService.dayGathering(code,Date.valueOf(currTime.toLocalDateTime().toLocalDate()))
                .orElseThrow(() -> new PointNotFoundException(code));
    }

    @GetMapping(value = "/point/{id}/avg")
    public TrackedPointStatistic pointDetailsBasedOnIdAndTime(@PathVariable("id") Long id) {
        Timestamp currTime = new Timestamp(System.currentTimeMillis());
        return pointService.avgFlowByTrackedPointCode(id,
                Date.valueOf(currTime.toLocalDateTime().toLocalDate()),
                Date.valueOf(currTime.toLocalDateTime().minusWeeks(24).toLocalDate()))
                .orElseThrow(() -> new PointNotFoundException(id));
    }

    @JsonView(View.Summary.class)
    @GetMapping(value = "/points/time/{time}")
    public Collection<TrackedPoint> getMultipleGatheringBasedOnTimePassed(@PathVariable("time") Date time){
        return pointService.dayHoursGatherings(time);
    }
}
