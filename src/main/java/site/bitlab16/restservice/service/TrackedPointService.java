package site.bitlab16.restservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.repository.GatheringRepository;
import site.bitlab16.restservice.repository.TrackedPointRepository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
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

    public Collection<TrackedPoint> dayGathering(Date date) {
        Collection<TrackedPoint> result = repository.getPastDayGathering(date);
        System.out.println(result);
        var future = repository.getFutureDayGathering(date);
        try {
            result.addAll(future);
        } catch (UnsupportedOperationException e) {
            result = new ArrayList<>(result);
            result.addAll(future);
        }
        return result;
    }

    public Collection<TrackedPoint> lastUpdate() {return repository.findLastGatheringForAllPoint();}

    public Collection<TrackedPoint> pastData(Timestamp time) {return repository.findPastGatherings(time);}

    public Collection<TrackedPoint> futureData(Timestamp time) {return repository.findFutureGatherings(time);}
}
