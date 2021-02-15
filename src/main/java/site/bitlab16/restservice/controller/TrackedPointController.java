package site.bitlab16.restservice.controller;

import site.bitlab16.restservice.exception.*;
import site.bitlab16.restservice.model.TrackedPoint;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bitlab16.restservice.service.TrackedPointService;

@RestController
public class TrackedPointController {
    private final TrackedPointService service;

    TrackedPointController(TrackedPointService service) {
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
