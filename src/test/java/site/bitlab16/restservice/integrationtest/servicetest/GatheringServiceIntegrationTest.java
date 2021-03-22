package site.bitlab16.restservice.integrationtest.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.model.Season;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.repository.GatheringRepository;
import site.bitlab16.restservice.service.GatheringService;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GatheringServiceIntegrationTest {

    @TestConfiguration
    static class GatheringServiceTestContextConfiguration {

        @Bean
        public GatheringService pointService() {
            return new GatheringService();
        }
    }

    @Autowired
    @Qualifier("gatheringService")
    private GatheringService gatheringService;

    @MockBean
    private GatheringRepository gatheringRepository;

    @BeforeEach
    public void setUpDB() {
        GeometryFactory factory = new GeometryFactory();
        var p1 = new Gathering(1L, new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)),
                Collections.emptyList()),
                10,
                new Timestamp(1564216200000L),
                Season.SPRING, false, 0L, 0L, 0L,0L);
        var p2 = new Gathering(2L, new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)),
                Collections.emptyList()),
                10,
                new Timestamp(1564218000000L),
                Season.SPRING, false, 0L, 0L, 0L,0L);
        var p3 = new Gathering(3L, new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)),
                Collections.emptyList()),
                10,
                new Timestamp(1564221600000L),
                Season.SPRING, false, 0L, 0L, 0L,0L);
        var p4 = new Gathering(4L, new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)),
                Collections.emptyList()),
                10,
                new Timestamp(1564223400000L),
                Season.SPRING, false, 0L, 0L, 0L,0L);

        Mockito.when(gatheringRepository.getPastDayGathering(
                Date.valueOf(new Timestamp(1564221600000L).toLocalDateTime().toLocalDate())))
                .thenReturn(List.of(p1, p2, p3));
        Mockito.when(gatheringRepository.getFutureDayGathering(
                Date.valueOf(new Timestamp(1564221600000L).toLocalDateTime().toLocalDate())))
                .thenReturn(List.of(p4));
    }

    @Test
    public void whenGetPastDayGathering_thenListOfGatheringShouldBeReturn() {
        GeometryFactory factory = new GeometryFactory();
        var p1 = new Gathering(1L, new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)),
                Collections.emptyList()),
                10,
                new Timestamp(1564216200000L),
                Season.SPRING, false, 0L, 0L, 0L,0L);
        var p2 = new Gathering(2L, new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)),
                Collections.emptyList()),
                10,
                new Timestamp(1564218000000L),
                Season.SPRING, false, 0L, 0L, 0L,0L);
        var p3 = new Gathering(3L, new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)),
                Collections.emptyList()),
                10,
                new Timestamp(1564221600000L),
                Season.SPRING, false, 0L, 0L, 0L,0L);
        var p4 = new Gathering(4L, new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)),
                Collections.emptyList()),
                10,
                new Timestamp(1564223400000L),
                Season.SPRING, false, 0L, 0L, 0L,0L);

        Collection<Gathering> gatherings = gatheringService.dayGathering(
                Date.valueOf(new Timestamp(1564221600000L).toLocalDateTime().toLocalDate()));
        assertThat(gatherings).hasSize(4).extracting(Gathering::getDetectionTime).contains(
                p1.getDetectionTime(), p2.getDetectionTime(), p3.getDetectionTime(), p4.getDetectionTime());
    }

}
