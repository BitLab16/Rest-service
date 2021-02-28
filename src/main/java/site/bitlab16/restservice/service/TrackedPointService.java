package site.bitlab16.restservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.repository.TrackedPointRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TrackedPointService {

    @Autowired
    private TrackedPointRepository repository;

    public List<TrackedPoint> findAll() {
        return repository.findAll();
    }

    public Optional<TrackedPoint> findById(Long id) {
        return repository.findById(id);
    }
}
