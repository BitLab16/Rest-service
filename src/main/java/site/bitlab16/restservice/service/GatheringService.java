package site.bitlab16.restservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.repository.GatheringRepository;

import java.sql.Timestamp;
import java.util.List;


@Service
public class GatheringService {

    @Autowired
    private GatheringRepository repository;

    public List<Gathering> lastUpdate() {return repository.findLastGatheringForAllPoint();}

    public List<Gathering> pastData(Timestamp time) {return repository.findPastGatherings(time);}

    public List<Gathering> futureData(Timestamp time) {return repository.findFutureGatherings(time);}
}