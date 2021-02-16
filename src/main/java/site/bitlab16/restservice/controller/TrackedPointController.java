package site.bitlab16.restservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bitlab16.restservice.exception.PointNotFoundException;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.service.TrackedPointService;

import java.util.List;

@RestController
public class TrackedPointController {
    private final TrackedPointService service;

    public TrackedPointController(@Qualifier("trackedPointService") TrackedPointService service) {
        this.service = service;
    }

    @GetMapping("/points")
    List<TrackedPoint> all() {
        return service.findAll();
    }

    @GetMapping("/points/{id}")
    TrackedPoint one(@PathVariable Long id) {
        return service.findById(id)
                .orElseThrow(() -> new PointNotFoundException(id));
    }

}
