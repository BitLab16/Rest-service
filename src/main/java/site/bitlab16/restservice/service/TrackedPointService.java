package site.bitlab16.restservice.service;

import org.springframework.stereotype.Service;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.model.TrackedPointStatistic;
import site.bitlab16.restservice.repository.TrackedPointRepository;

import java.sql.Date;
import java.time.DayOfWeek;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TrackedPointService {

    private final TrackedPointRepository trackedPointRepository;

    private final GatheringService gatheringService;

    public TrackedPointService(TrackedPointRepository trackedPointRepository, GatheringService gatheringService) {
        this.trackedPointRepository = trackedPointRepository;
        this.gatheringService = gatheringService;
    }

    public Optional<TrackedPointStatistic> avgFlowByTrackedPointCode(Long code, Date from, Date to) {
        Optional<Long> trackedPointId = trackedPointRepository.findTrackedPointIdByCode(code);
        Optional<TrackedPointStatistic> statistic = Optional.empty();
        if(trackedPointId.isPresent()) {
            var gatherings = gatheringService.intervalGatheringFromDate(trackedPointId.get(), from, to);
            statistic = gatherings.parallelStream()
                    .map(new GatheringToStatistic())
                    .reduce((result, newStatistic) -> {
                        for (var day: DayOfWeek.values()) {
                            var hourMap = newStatistic.getMetrics().get(day);
                            if(hourMap != null)
                                result.addMetrics(day, hourMap);
                        }
                        return result;
                    });
        }
        return statistic;
    }

    public Optional<TrackedPoint> findByCode(Long code, Date date) {
        Optional<TrackedPoint> t = trackedPointRepository.findByCode(code);
        t.ifPresent(trackedPoint -> {
            var gatherings = gatheringService.yearGatheringFromDate(t.get().getId(), date);
            trackedPoint.addGatherings(new ArrayList<>(gatherings));
        });
        return t;
    }

    public Collection<TrackedPoint> dayGathering(Date date) {
        var gatherings = gatheringService.dayGathering(date);
        List<Long> trackedPointId = gatherings.stream().map(Gathering::getPoint).collect(Collectors.toList());
        Collection<TrackedPoint> trackedPoints = trackedPointRepository.findAllById(trackedPointId);
        trackedPoints.forEach(trackedPoint -> trackedPoint.addGatherings(
            gatherings.stream().filter(o -> o.getPoint().equals(trackedPoint.getId())).collect(Collectors.toList())
        ));
        return trackedPoints;
    }
}

class GatheringToStatistic implements Function<Gathering, TrackedPointStatistic> {

    @Override
    public TrackedPointStatistic apply(Gathering gathering) {
        var statistic = new TrackedPointStatistic();
        var weekDay = gathering.getDetectionTime().toLocalDateTime().getDayOfWeek();
        var hour = gathering.getDetectionTime().toLocalDateTime().getHour();
        statistic.addMetric(weekDay, hour,gathering.getFlow());
        return statistic;
    }
}
