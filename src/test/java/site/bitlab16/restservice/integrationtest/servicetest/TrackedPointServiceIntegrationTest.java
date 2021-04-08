package site.bitlab16.restservice.integrationtest.servicetest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.model.Season;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.model.TrackedPointStatistic;
import site.bitlab16.restservice.repository.TrackedPointRepository;
import site.bitlab16.restservice.service.GatheringService;
import site.bitlab16.restservice.service.TrackedPointService;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class TrackedPointServiceIntegrationTest {

    @InjectMocks
    private TrackedPointService pointService;

    @Mock
    private GatheringService gatheringServiceMock;

    @Mock
    private TrackedPointRepository pointRepository;

    @Test
    public void whenInvalidCode_thenTrackedPointShouldNotBeFound() {
        Mockito.when(pointRepository.findByCode(-99L)).thenReturn(java.util.Optional.empty());
        assertThat(pointService.findByCode(-99L, Date.valueOf(new Timestamp(1564223400000L).toLocalDateTime().toLocalDate()))).isEmpty();
    }

    @Test
    public void whenAvgWithValidCode_thenTrackedPointStatisticReturned() {
        GeometryFactory factory = new GeometryFactory();
        var c = Calendar.getInstance();
        var from = Calendar.getInstance();
        from.set(2019, Calendar.JULY, 5);
        var to = Calendar.getInstance();
        to.set(2019, Calendar.JULY, 3);

        var p1 = new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)));

        c.set(2019, Calendar.JULY,2, 8, 0);
        var g1 = new Gathering(1L,
                1L, 5, new Timestamp(c.getTimeInMillis()), Season.SPRING,
                false, 0L, 0L, 0L, 0L);

        c.set(2019, Calendar.JULY,2, 9, 0);
        var g2 = new Gathering(2L,
                1L, 6, new Timestamp(c.getTimeInMillis()), Season.SPRING,
                false, 0L, 0L, 0L, 0L);

        c.set(2019, Calendar.JULY,3, 9, 0);
        var g3 = new Gathering(3L,
                1L, 12, new Timestamp(c.getTimeInMillis()), Season.SPRING,
                false, 0L, 0L, 0L, 0L);

        c.set(2019, Calendar.JULY,3, 10, 0);
        var g4 = new Gathering(4L,
                1L, 8, new Timestamp(c.getTimeInMillis()), Season.SPRING,
                false, 0L, 0L, 0L, 0L);

        c.set(2019, Calendar.JULY,9, 9, 0);
        var g5 = new Gathering(4L,
                1L, 12, new Timestamp(c.getTimeInMillis()), Season.SPRING,
                false, 0L, 0L, 0L, 0L);


        Mockito.when(pointRepository.findTrackedPointIdByCode(100L)).thenReturn(java.util.Optional.of(1L));
        Mockito.when(gatheringServiceMock.intervalGatheringFromDate(1L,
                new Date(from.getTimeInMillis()),
                new Date(to.getTimeInMillis()))).thenReturn(Arrays.asList(g1, g2, g3, g4, g5));
        Optional<TrackedPointStatistic> statistic = pointService.avgFlowByTrackedPointCode(100L,
                new Date(from.getTimeInMillis()),
                new Date(to.getTimeInMillis()));
        var expected = new TrackedPointStatistic();
        expected.addMetric(DayOfWeek.TUESDAY, 8, 5);
        expected.addMetric(DayOfWeek.TUESDAY, 9, 9);
        expected.addMetric(DayOfWeek.WEDNESDAY, 9, 12);
        expected.addMetric(DayOfWeek.WEDNESDAY, 10, 8);
        assertThat(statistic.get().getMetrics()).hasSize(2);
        assertThat(statistic.get()).usingRecursiveComparison().isEqualTo(expected);
        verifyFindTrackedPointIdByCodeIsCalledOnce();
    }


    @Test
    public void whenValidCode_thenTrackedPointShouldBeFound() {
        GeometryFactory factory = new GeometryFactory();
        var p1 = new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)));
        var g1 = new Gathering(1L,
                1L, 5, new Timestamp(1564223400000L), Season.SPRING,
                false, 0L, 0L, 0L, 0L);
        var g2 = new Gathering(2L,
                1L, 6, new Timestamp(1564223400001L), Season.SPRING,
                false, 0L, 0L, 0L, 0L);
        var g3 = new Gathering(3L,
                1L, 7, new Timestamp(1564223400002L), Season.SPRING,
                false, 0L, 0L, 0L, 0L);
        var g4 = new Gathering(4L,
                1L, 8, new Timestamp(1564223399999L), Season.SPRING,
                false, 0L, 0L, 0L, 0L);
        Mockito.when(pointRepository.findByCode(100L)).thenReturn(java.util.Optional.of(p1));
        Mockito.when(gatheringServiceMock.yearGatheringFromDate(1L,
                Date.valueOf(new Timestamp(1564223400000L).toLocalDateTime().toLocalDate())))
                .thenReturn(Arrays.asList(g1, g2, g3, g4));
        Date date = Date.valueOf(new Timestamp(1564223400000L).toLocalDateTime().toLocalDate());
        Optional<TrackedPoint> point = pointService.findByCode(100L, date);
        assertThat(point.get().getName()).isEqualTo("Piazza dei signori");
        assertThat(point.get().getGatherings()).hasSize(4);
        verifyFindByCodeIsCalledOnce();
    }

    @Test
    public void whenDayGathering_thenTrackedPointShouldBeFound() {
        GeometryFactory factory = new GeometryFactory();
        var p1 = new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)));
        var g1 = new Gathering(1L,
                1L, 5, new Timestamp(1564223400000L), Season.SPRING,
                false, 0L, 0L, 0L, 0L);
        var g2 = new Gathering(2L,
                1L, 6, new Timestamp(1564223400001L), Season.SPRING,
                false, 0L, 0L, 0L, 0L);
        var g3 = new Gathering(3L,
                1L, 7, new Timestamp(1564223400002L), Season.SPRING,
                false, 0L, 0L, 0L, 0L);
        var g4 = new Gathering(4L,
                1L, 8, new Timestamp(1564223399999L), Season.SPRING,
                false, 0L, 0L, 0L, 0L);
        List<Long> trackedPointId = new ArrayList<Long>();
        trackedPointId.add(1L);
        trackedPointId.add(1L);
        trackedPointId.add(1L);
        trackedPointId.add(1L);
        Mockito.when(pointRepository.findAllById(trackedPointId)).thenReturn(Arrays.asList(p1));
        Mockito.when(gatheringServiceMock.dayGathering(
                Date.valueOf(new Timestamp(1564223400000L).toLocalDateTime().toLocalDate())))
                .thenReturn(Arrays.asList(g1, g2, g3, g4));
        Date date = Date.valueOf(new Timestamp(1564223400000L).toLocalDateTime().toLocalDate());
        Collection<TrackedPoint> points = pointService.dayGathering(date);
        assertThat(points).hasSize(1).extracting(TrackedPoint::getName).contains("Piazza dei signori");
        assertThat(points).extracting(TrackedPoint::getGatherings).contains(Arrays.asList(g1, g2, g3, g4));
    }


    private void verifyFindAllTrackedPointsIsCalledOnce() {
        Mockito.verify(pointRepository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(pointRepository);
    }

    private void verifyFindByCodeIsCalledOnce() {
        Mockito.verify(pointRepository, VerificationModeFactory.times(1))
                .findByCode(Mockito.anyLong());
        Mockito.reset(pointRepository);
    }

    private void verifyFindTrackedPointIdByCodeIsCalledOnce() {
        Mockito.verify(pointRepository, VerificationModeFactory.times(1))
                .findTrackedPointIdByCode(Mockito.anyLong());
        Mockito.reset(pointRepository);
    }
}
