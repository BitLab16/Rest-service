package site.bitlab16.restservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.*;
import site.bitlab16.restservice.exception.PointNotFoundException;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.model.jackson_view.View;
import site.bitlab16.restservice.service.GatheringService;
import site.bitlab16.restservice.service.TrackedPointService;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

@RestController
@CrossOrigin(origins = { "http://localhost:8081", "http://localhost:8080" })
public class TrackedPointController {

    private final TrackedPointService pointService;

    private final static Timestamp currTime = new Timestamp(1564223400000L);

    public TrackedPointController(TrackedPointService pointService) {
        this.pointService = pointService;
    }

    @JsonView(View.Summary.class)
    @GetMapping(value= "/points")
    Collection<TrackedPoint> getCurrentDayGatheringDetected() {
        Timestamp currTime = new Timestamp(System.currentTimeMillis());
        return pointService.dayGathering(Date.valueOf(currTime.toLocalDateTime().toLocalDate()));
    }

    @GetMapping(value = "/point/{id}")
    TrackedPoint pointDetailsBasedOnId(@PathVariable("id") Long id) {
        Timestamp currTime = new Timestamp(System.currentTimeMillis());
        return pointService.findByCode(id, Date.valueOf(currTime.toLocalDateTime().toLocalDate()))
                .orElseThrow(() -> new PointNotFoundException(id));
    }

    @JsonView(View.Summary.class)
    @GetMapping(value = "/points/time/{time}")
    Collection<TrackedPoint> getMultipleGatheringBasedOnTimePassed(@PathVariable("time") Date time){
        return pointService.dayGathering(time);
    }
}
