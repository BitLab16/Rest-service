package site.bitlab16.restservice.controller;

import site.bitlab16.restservice.exception.*;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.repository.TrackedPointRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrackedPointController {
    private final TrackedPointRepository repository;

    TrackedPointController(TrackedPointRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/points")
    List<TrackedPoint> all() {
        return repository.findAll();
    }

    @GetMapping("/points/{id}")
    TrackedPoint one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PointNotFoundException(id));
    }

}
