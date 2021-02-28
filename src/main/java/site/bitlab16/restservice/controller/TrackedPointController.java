package site.bitlab16.restservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bitlab16.restservice.exception.PointNotFoundException;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.repository.GatheringRepository;
import site.bitlab16.restservice.service.TrackedPointService;

import java.util.List;

@RestController
public class TrackedPointController {

    private final TrackedPointService pointService;
    private final GatheringRepository gatheringService;

    public TrackedPointController(TrackedPointService pointService, GatheringRepository gatheringService) {
        this.pointService = pointService;
        this.gatheringService = gatheringService;
    }

    @GetMapping("/points")
    List<Gathering> all() {
        return gatheringService.findLastGatheringForAllPoint();
    }

    @GetMapping("/points/{id}")
    TrackedPoint one(@PathVariable Long id) {
        return pointService.findById(id)
                .orElseThrow(() -> new PointNotFoundException(id));
    }

}
