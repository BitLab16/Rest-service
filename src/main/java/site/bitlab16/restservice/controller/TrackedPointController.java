package site.bitlab16.restservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bitlab16.restservice.exception.PointNotFoundException;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.service.GatheringService;
import site.bitlab16.restservice.service.TrackedPointService;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class TrackedPointController {

    private final TrackedPointService pointService;
    private final GatheringService gatheringService;

    private final static Timestamp currTime = new Timestamp(1564255800000L);

    public TrackedPointController(TrackedPointService pointService, GatheringService gatheringService) {
        this.pointService = pointService;
        this.gatheringService = gatheringService;
    }

    @GetMapping("/points")
    List<Gathering> allLastGatheringDetectedForAllPoint() {
        return gatheringService.lastUpdate();
    }

    @PostMapping("/points/{id}")
    TrackedPoint pointDetailsBasedOnId(@PathVariable Long id) {
        return pointService.findById(id)
                .orElseThrow(() -> new PointNotFoundException(id));
    }

    @PostMapping("/points/{time}")
    List<Gathering> getGatheringBasedOnTimePassed(@PathVariable Timestamp time){
        if(time.after(currTime))
            return gatheringService.futureData(time);
        else
            return gatheringService.pastData(time);
    }
}
