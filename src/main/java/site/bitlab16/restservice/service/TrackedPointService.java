package site.bitlab16.restservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.repository.TrackedPointRepository;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrackedPointService {

    @Autowired
    private TrackedPointRepository trackedPointRepository;

    @Autowired
    private GatheringService gatheringService;

    public Optional<TrackedPoint> findById(Long id, Date date) {
        Optional<TrackedPoint> t = trackedPointRepository.findById(id);
        t.ifPresent(trackedPoint -> {
            var gatherings = gatheringService.dayGathering(date);
            trackedPoint.addGatherings(new ArrayList<>(gatherings));
        });
        return t;
    }

    public Collection<TrackedPoint> dayGathering(Date date) {
        var gaterings = gatheringService.dayGathering(date);
        List<Long> trackedPointId = gaterings.stream().map(Gathering::getPoint).collect(Collectors.toList());
        Collection<TrackedPoint> trackedPoints = trackedPointRepository.findAllById(trackedPointId);
        trackedPoints.stream().forEach(trackedPoint -> {
            trackedPoint.addGatherings(
                gaterings.stream().filter(o -> o.getPoint() == trackedPoint.getId()).collect(Collectors.toList())
            );
        });
        return trackedPoints;
    }
}
