package site.bitlab16.restservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.repository.GatheringRepository;
import site.bitlab16.restservice.repository.TrackedPointRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GatheringService {

    @Autowired
    private GatheringRepository repository;

    public List<Gathering> lastUpdate() {return repository.findLastGatheringForAllPoint();}
}