package site.bitlab16.restservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.repository.GatheringRepository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class GatheringService {

    @Autowired
    private GatheringRepository repository;

    public Collection<Gathering> dayGathering(Date date) {
        var past = repository.getPastDayGathering(date);
        var future = repository.getFutureDayGathering(date);
        List<Gathering> result = new ArrayList<>(past.size() + future.size());
        result.addAll(past);
        result.addAll(future);
        return result;
    }

    public Collection<Gathering> dayGathering(Long id, Date date) {
        var past = repository.getPastDayGathering(id, date);
        var future = repository.getFutureDayGathering(id, date);
        List<Gathering> result = new ArrayList<>(past.size() + future.size());
        result.addAll(past);
        result.addAll(future);
        return result;
    }

    public Collection<Gathering> intervalGatheringFromDate(Long id, Date from, Date to) {
        return repository.intervalGatheringFromDate(id, from, to);
    }

    public Collection<Gathering> yearGatheringFromDate(Date date) {return repository.getYearGatheringFromDate(date);}

    public Collection<Gathering> yearGatheringFromDate(Long id, Date date) {
        return repository.getYearGatheringFromDate(id, date);
    }

    public Collection<Gathering> lastUpdate() {return repository.findLastGatheringForAllPoint();}

    public Collection<Gathering> pastData(Timestamp time) {return repository.findPastGatherings(time);}

    public Collection<Gathering> futureData(Timestamp time) {return repository.findFutureGatherings(time);}

}