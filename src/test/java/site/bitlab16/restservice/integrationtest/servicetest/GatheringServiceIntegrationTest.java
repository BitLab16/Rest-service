package site.bitlab16.restservice.integrationtest.servicetest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.model.Indexes;
import site.bitlab16.restservice.model.Season;
import site.bitlab16.restservice.model.TimeInformation;
import site.bitlab16.restservice.repository.GatheringRepository;
import site.bitlab16.restservice.repository.PredictionRepository;
import site.bitlab16.restservice.service.GatheringService;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class GatheringServiceIntegrationTest {

    @InjectMocks
    private GatheringService gatheringService;

    @Mock
    private GatheringRepository gatheringRepository;

    @Mock
    private PredictionRepository predictionRepository;

    private LocalDateTime getLastDateTimeFromTimestamp(Timestamp timestamp) {
        return LocalDateTime.of(
                LocalDate.of(
                        timestamp.toLocalDateTime().getYear(),
                        timestamp.toLocalDateTime().getMonth(),
                        timestamp.toLocalDateTime().getDayOfMonth()), LocalTime.of(23, 55));
    }

    @Test
    void whenGetPastDayGathering_thenListOfGatheringShouldBeReturn() {
        var p1 = new Gathering(1L, 1L,
                10,
                new TimeInformation(
                        new Timestamp(1577869200000L),
                0,
                        Season.SPRING,
                        false),
                new Indexes(0L, 0L, 0L,0L));
        var p2 = new Gathering(2L, 1L,
                10,
                new TimeInformation(
                        new Timestamp(1577869500000L),
                        0,
                        Season.SPRING,
                        false),
                new Indexes(0L, 0L, 0L,0L));
        var p3 = new Gathering(3L, 1L,
                10,
                new TimeInformation(
                        new Timestamp(1577869800000L),
                        0,
                        Season.SPRING,
                        false),
                new Indexes(0L, 0L, 0L,0L));
        var p4 = new Gathering(4L, 1L,
                10,
                new TimeInformation(
                        new Timestamp(1577872200000L),
                0,
                        Season.SPRING,
                        false),
                new Indexes(0L, 0L, 0L,0L));

        Mockito.when(gatheringRepository.getPastDayGathering(
                Date.valueOf(new Timestamp(1577869800000L).toLocalDateTime().toLocalDate())))
                .thenReturn(List.of(p1, p2, p3));

        var toTime = getLastDateTimeFromTimestamp(new Timestamp(1577869800000L));
        Mockito.when(predictionRepository.findAllFromInterval(
                new Timestamp(1577869800000L).toLocalDateTime(),toTime))
                .thenReturn(List.of(p4));

        Collection<Gathering> gatherings = gatheringService.dayGathering(
                Date.valueOf(new Timestamp(1577869800000L).toLocalDateTime().toLocalDate()));
        assertThat(gatherings).hasSize(4).extracting(Gathering::getDetectionTime).contains(
                p1.getDetectionTime(), p2.getDetectionTime(), p3.getDetectionTime(), p4.getDetectionTime());
    }

    @Test
    void whenGetPastDayGatheringWithId_thenListOfGatheringOfThatPointShouldBeReturn() {
        var p1 = new Gathering(1L, 1L,
                10,
                new TimeInformation(
                        new Timestamp(1577869200000L),
                        0,
                        Season.SPRING,
                        false),
                new Indexes(0L, 0L, 0L,0L));
        var p2 = new Gathering(2L, 1L,
                10,
                new TimeInformation(
                        new Timestamp(1577869500000L),
                        0,
                        Season.SPRING,
                        false),
                new Indexes(0L, 0L, 0L,0L));
        var p3 = new Gathering(3L, 1L,
                10,
                new TimeInformation(
                        new Timestamp(1577869800000L),
                        0,
                        Season.SPRING,
                        false),
                new Indexes(0L, 0L, 0L,0L));
        var p4 = new Gathering(4L, 1L,
                10,
                new TimeInformation(
                        new Timestamp(1577872200000L),
                        0,
                        Season.SPRING,
                        false),
                new Indexes(0L, 0L, 0L,0L));

        Mockito.when(gatheringRepository.getPastDayGathering(1L,
                Date.valueOf(new Timestamp(1577869800000L).toLocalDateTime().toLocalDate())))
                .thenReturn(List.of(p1, p2, p3));

        var toTime = getLastDateTimeFromTimestamp(new Timestamp(1577869800000L));
        Mockito.when(predictionRepository.findByIdFromInterval(1L,
                new Timestamp(1577869800000L).toLocalDateTime(),toTime))
                .thenReturn(List.of(p4));


        Collection<Gathering> gatherings = gatheringService.dayGathering(1L,
                Date.valueOf(new Timestamp(1577869800000L).toLocalDateTime().toLocalDate()));
        assertThat(gatherings).hasSize(4).extracting(Gathering::getDetectionTime).contains(
                p1.getDetectionTime(), p2.getDetectionTime(), p3.getDetectionTime(), p4.getDetectionTime());
    }

    @Test
    void whenGetYearGatheringFromDate_thenListOfPastYearGatheringShouldBeReturn() {
        GeometryFactory factory = new GeometryFactory();
        var p1 = new Gathering(1L, 1L,
                10,
                new TimeInformation(
                        new Timestamp(1564216200000L),
                0,
                        Season.SPRING,
                        false),
                new Indexes(0L, 0L, 0L,0L));
        var p2 = new Gathering(2L, 1L,
                10,
                new TimeInformation(
                        new Timestamp(1564218000000L),
                0,
                        Season.SPRING,
                        false),
                new Indexes(0L, 0L, 0L,0L));
        var p3 = new Gathering(3L, 1L,
                10,
                new TimeInformation(
                        new Timestamp(1564221600000L),
                0,
                        Season.SPRING,
                        false),
                new Indexes(0L, 0L, 0L,0L));

        Mockito.when(gatheringRepository.getYearGatheringFromDate(
                Date.valueOf(new Timestamp(1564221700000L).toLocalDateTime().toLocalDate())))
                .thenReturn(List.of(p1, p2, p3));

        Collection<Gathering> gatherings = gatheringService.yearGatheringFromDate(
                Date.valueOf(new Timestamp(1564221700000L).toLocalDateTime().toLocalDate()));
        assertThat(gatherings).hasSize(3).extracting(Gathering::getDetectionTime).contains(
                p1.getDetectionTime(), p2.getDetectionTime(), p3.getDetectionTime());
    }

    @Test
    void whenGetYearGatheringFromDateWithId_thenListOfPastYearGatheringOfThatPointShouldBeReturn() {
        GeometryFactory factory = new GeometryFactory();
        var p1 = new Gathering(1L, 1L,
                10,
                new TimeInformation(
                        new Timestamp(1564216200000L),
                0,
                        Season.SPRING,
                        false),
                new Indexes(0L, 0L, 0L,0L));
        var p2 = new Gathering(2L, 1L,
                10,
                new TimeInformation(
                        new Timestamp(1564218000000L),
                0,
                        Season.SPRING,
                        false),
                new Indexes(0L, 0L, 0L,0L));
        var p3 = new Gathering(3L, 1L,
                10,
                new TimeInformation(
                        new Timestamp(1564221600000L),
                        0,
                        Season.SPRING,
                        false),
                new Indexes(0L, 0L, 0L,0L));

        Mockito.when(gatheringRepository.getYearGatheringFromDate(1L,
                Date.valueOf(new Timestamp(1564221700000L).toLocalDateTime().toLocalDate())))
                .thenReturn(List.of(p1, p2, p3));

        Collection<Gathering> gatherings = gatheringService.yearGatheringFromDate(1L,
                Date.valueOf(new Timestamp(1564221700000L).toLocalDateTime().toLocalDate()));
        assertThat(gatherings).hasSize(3).extracting(Gathering::getDetectionTime).contains(
                p1.getDetectionTime(), p2.getDetectionTime(), p3.getDetectionTime());
    }


}
