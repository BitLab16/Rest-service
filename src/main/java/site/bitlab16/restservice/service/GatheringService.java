package site.bitlab16.restservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.repository.GatheringRepository;
import site.bitlab16.restservice.repository.PredictionRepository;

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

    @KafkaListener(topics = "${gathering-source-1.topic.name}", containerFactory = "gatheringKafkaListenerContainerFactory")
    public void listenTopicSource1(List<Gathering> gathering) {
        repository.saveAll(gathering);
    }

    @KafkaListener(topics = "${gathering-source-2.topic.name}", containerFactory = "gatheringKafkaListenerContainerFactory")
    public void listenTopicSource2(List<Gathering> gathering) {
        repository.saveAll(gathering);
    }

    @KafkaListener(topics = "${gathering-source-3.topic.name}", containerFactory = "gatheringKafkaListenerContainerFactory")
    public void listenTopicSource3(List<Gathering> gathering) {
        repository.saveAll(gathering);
    }

    @KafkaListener(topics = "${gathering-source-4.topic.name}", containerFactory = "gatheringKafkaListenerContainerFactory")
    public void listenTopicSource4(List<Gathering> gathering) {
        repository.saveAll(gathering);
    }

    @KafkaListener(topics = "${gathering-source-5.topic.name}", containerFactory = "gatheringKafkaListenerContainerFactory")
    public void listenTopicSource5(List<Gathering> gathering) {
        repository.saveAll(gathering);
    }



    private boolean isPredictionAvailable(Timestamp date) {
        return date.after(Timestamp.valueOf("2019-12-31 23:55:00"));
    }

    private LocalDateTime getLastDateTimeFromTimestamp(Timestamp timestamp) {
        return LocalDateTime.of(
                LocalDate.of(
                        timestamp.toLocalDateTime().getYear(),
                        timestamp.toLocalDateTime().getMonth(),
                        timestamp.toLocalDateTime().getDayOfMonth()), LocalTime.of(23, 55));
    }

    private Timestamp getLastTimeFromGatherings(Collection<Gathering> gatherings) {
        return Collections
                .max(gatherings, Comparator.comparing(Gathering::getDetectionTime)).getDetectionTime();
    }

    private List<Gathering> concatResult(Collection<Gathering> past, Collection<Gathering> future) {
        List<Gathering> result = new ArrayList<>(past.size() + future.size());
        result.addAll(past);
        result.addAll(future);
        return result;
    }


    public Collection<Gathering> dayGathering(Date date) {
        var past = repository.getPastDayGathering(date);
        var maxDetectionTime = getLastTimeFromGatherings(past);
        List<Gathering> future = Collections.emptyList();
        if(isPredictionAvailable(maxDetectionTime)) {
            var timeTime = getLastDateTimeFromTimestamp(maxDetectionTime);
            future = predictionRepository.findAllFromInterval(
                    maxDetectionTime.toLocalDateTime(), timeTime);
        }
        return concatResult(past, future);
    }

    public Collection<Gathering> dayGathering(Long id, Date date) {
        var past = repository.getPastDayGathering(id, date);
        var maxDetectionTime = getLastTimeFromGatherings(past);
        List<Gathering> future = Collections.emptyList();
        if(isPredictionAvailable(maxDetectionTime)) {
            var lastTime = getLastDateTimeFromTimestamp(maxDetectionTime);
            future = predictionRepository.findByIdFromInterval(
                    id, maxDetectionTime.toLocalDateTime(), lastTime);
        }
        return concatResult(past, future);
    }

    public Collection<Gathering> dayGatheringsOnlyHour(Long id, Date date) {
        var past = repository.getPastDayHoursGatherings(id, date);
        var maxDetectionTime = getLastTimeFromGatherings(past);
        List<Gathering> future = Collections.emptyList();
        if(isPredictionAvailable(maxDetectionTime)) {
            var lastTime = getLastDateTimeFromTimestamp(maxDetectionTime);
            future = predictionRepository.findOnlyHoursByIdFromInterval(
                    id, maxDetectionTime.toLocalDateTime(), lastTime);
        }
        return concatResult(past, future);
    }

    public Collection<Gathering> dayGatheringsOnlyHour(Date date) {
        var past = repository.getPastDayHoursGatherings(date);
        var maxDetectionTime = getLastTimeFromGatherings(past);
        List<Gathering> future = Collections.emptyList();
        if(isPredictionAvailable(maxDetectionTime)) {
            var lastTime = getLastDateTimeFromTimestamp(maxDetectionTime);
            future = predictionRepository.findAllOnlyHoursFromInterval(
                    maxDetectionTime.toLocalDateTime(), lastTime);
        }
        return concatResult(past, future);
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