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
    private final GatheringService gatheringService;

    private final static Timestamp currTime = new Timestamp(1564223400000L);

    public TrackedPointController(TrackedPointService pointService, GatheringService gatheringService) {
        this.pointService = pointService;
        this.gatheringService = gatheringService;
    }

    @JsonView(View.Summary.class)
    @GetMapping(value= "/points")
    Collection<TrackedPoint> getCurrentDayGatheringDetected() {
        return pointService.dayGathering(Date.valueOf(currTime.toLocalDateTime().toLocalDate()));
    }

    @JsonView(View.Summary.class)
    @GetMapping(value = "/point/{id}")
    TrackedPoint pointDetailsBasedOnId(@PathVariable("id") Long id) {
        return pointService.findById(id)
                .orElseThrow(() -> new PointNotFoundException(id));
    }

    @GetMapping(value = "/point/time/{time}")
    Collection<TrackedPoint> getSingleGatheringBasedOnTimePassed(@PathVariable("time") Date time){
        if(time.after(currTime))
            return pointService.futureData(new Timestamp(time.getTime()));
        else
            return pointService.pastData(new Timestamp(time.getTime()));
    }

    @JsonView(View.Summary.class)
    @GetMapping(value = "/points/time/{time}")
    Collection<TrackedPoint> getMultipleGatheringBasedOnTimePassed(@PathVariable("time") Date time){
        return pointService.dayGathering(time);
    }
}
