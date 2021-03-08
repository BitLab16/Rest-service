package site.bitlab16.restservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.repository.GatheringRepository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;


@Service
public class GatheringService {

    @Autowired
    private GatheringRepository repository;

    public Collection<Gathering> dayGathering(Date date) {
        Collection<Gathering> result = repository.getPastDayGathering(date);
        var future = repository.getFutureDayGathering(date);
        try {
            result.addAll(future);
        } catch (UnsupportedOperationException e) {
            result = new ArrayList<>(result);
            result.addAll(future);
        }
        return result;
    }

    public Collection<Gathering> lastUpdate() {return repository.findLastGatheringForAllPoint();}

    public Collection<Gathering> pastData(Timestamp time) {return repository.findPastGatherings(time);}

    public Collection<Gathering> futureData(Timestamp time) {return repository.findFutureGatherings(time);}
}