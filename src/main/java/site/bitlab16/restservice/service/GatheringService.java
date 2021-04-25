package site.bitlab16.restservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.repository.GatheringRepository;
import site.bitlab16.restservice.repository.PredictionRepository;

import static org.apache.kafka.common.quota.ClientQuotaEntity.CLIENT_ID;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


@Service
public class GatheringService {

    private final GatheringRepository repository;
    private final PredictionRepository predictionRepository;

    @Autowired
    public GatheringService(GatheringRepository repository, PredictionRepository predictionRepository) {
        this.repository = repository;
        this.predictionRepository = predictionRepository;
    }

    @KafkaListener(topics = "${gathering.topic.name}", containerFactory = "gatheringKafkaListenerContainerFactory")
    public void listenGroupFoo(List<Gathering> gathering) {
        repository.saveAll(gathering);
    }


    public Collection<Gathering> dayGathering(Date date) {
        var past = repository.getPastDayGathering(date);
        var maxDetectionTime = Collections
                .max(past, Comparator.comparing(Gathering::getDetectionTime)).getDetectionTime();
        LocalDateTime time = LocalDateTime.of(
                LocalDate.of(
                        maxDetectionTime.toLocalDateTime().getYear(),
                        maxDetectionTime.toLocalDateTime().getMonth(),
                        maxDetectionTime.toLocalDateTime().getDayOfMonth()), LocalTime.of(23, 55));
        var future = predictionRepository.findAllFromInterval(
                maxDetectionTime.toLocalDateTime(), time);
        List<Gathering> result = new ArrayList<>(past.size() /*+ predictions.size()*/);
        result.addAll(past);
        //result.addAll(predictions);
        return result;
    }

    public Collection<Gathering> dayGathering(Long id, Date date) {
        var past = repository.getPastDayGathering(id, date);
        var maxDetectionTime = Collections
                .max(past, Comparator.comparing(Gathering::getDetectionTime)).getDetectionTime();
        LocalDateTime time = LocalDateTime.of(
                LocalDate.of(
                        maxDetectionTime.toLocalDateTime().getYear(),
                        maxDetectionTime.toLocalDateTime().getMonth(),
                        maxDetectionTime.toLocalDateTime().getDayOfMonth()), LocalTime.of(23, 55));
        var future = predictionRepository.findByIdFromInterval(
                id, maxDetectionTime.toLocalDateTime(), time);
        //var predictions= future.collectList().block();
        List<Gathering> result = new ArrayList<>(past.size() + future.size());
        result.addAll(past);
        result.addAll(future);
        return result;
    }

    public Collection<Gathering> dayGatheringsOnlyHour(Long id, Date date) {
        var past = repository.getPastDayHoursGatherings(id, date);
        var future = repository.getFutureDayHoursGatherings(id, date);
        List<Gathering> result = new ArrayList<>(past.size() + future.size());
        result.addAll(past);
        result.addAll(future);
        return result;
    }

    public Collection<Gathering> dayGatheringsOnlyHour(Date date) {
        var past = repository.getPastDayHoursGatherings(date);
        var future = repository.getFutureDayHoursGatherings(date);
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