package site.bitlab16.restservice.controller;

import org.springframework.web.bind.annotation.*;
import site.bitlab16.restservice.exception.PointNotFoundException;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.service.GatheringService;
import site.bitlab16.restservice.service.TrackedPointService;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

@RestController
public class TrackedPointController {

    private final TrackedPointService pointService;
    private final GatheringService gatheringService;

    private final static Timestamp currTime = new Timestamp(1564223400000L);

    public TrackedPointController(TrackedPointService pointService, GatheringService gatheringService) {
        this.pointService = pointService;
        this.gatheringService = gatheringService;
    }

    @GetMapping(value= "/points")
    Collection<Gathering> getCurrentDayGatheringDetected() {
        return gatheringService.dayGathering(Date.valueOf(currTime.toLocalDateTime().toLocalDate()));
    }

    @GetMapping(value = "/point/{id}")
    TrackedPoint pointDetailsBasedOnId(@PathVariable("id") Long id) {
        return pointService.findById(id)
                .orElseThrow(() -> new PointNotFoundException(id));
    }

    @GetMapping(value = "/point/time/{time}")
    Collection<Gathering> getSingleGatheringBasedOnTimePassed(@PathVariable("time") Date time){
        if(time.after(currTime))
            return gatheringService.futureData(new Timestamp(time.getTime()));
        else
            return gatheringService.pastData(new Timestamp(time.getTime()));
    }

    @GetMapping(value = "/points/time/{time}")
    Collection<Gathering> getMultipleGatheringBasedOnTimePassed(@PathVariable("time") Date time){
        return gatheringService.dayGathering(time);
    }
}
