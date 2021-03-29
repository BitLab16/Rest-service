package site.bitlab16.restservice.integrationtest.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.repository.TrackedPointRepository;
import site.bitlab16.restservice.service.TrackedPointService;
import org.locationtech.jts.geom.Point;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrackedPointServiceIntegrationTest {

    @TestConfiguration
    static class TrackedPointServiceTestContextConfiguration {

        @Bean
        public TrackedPointService pointService() {
            return new TrackedPointService();
        }
    }

    @Autowired
    @Qualifier("pointService")
    private TrackedPointService pointService;

    @MockBean
    private TrackedPointRepository pointRepository;


    @BeforeEach
    public void setUpDB() {
        GeometryFactory factory = new GeometryFactory();
        var p1 = new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)),
                new ArrayList< Gathering >());
        var p2 = new TrackedPoint(2L,
                "Piazza della frutta",
                200L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)),
                new ArrayList< Gathering >());
        var p3 = new TrackedPoint(3L,
                "Prato della valle",
                300L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)),
                new ArrayList< Gathering >());

        Mockito.when(pointRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));
        Mockito.when(pointRepository.findById(p1.getId())).thenReturn(java.util.Optional.of(p1));
    }

    @Test
    public void whenInvalidId_thenTrackedPointShouldNotBeFound() {
        assertThat(pointService.findById(-99L, Date.valueOf(new Timestamp(1564223400000L).toLocalDateTime().toLocalDate()))).isEmpty();
    }

    @Test
    public void whenValidId_thenTrackedPointShouldBeFound() {
        Date date = Date.valueOf(new Timestamp(1564223400000L).toLocalDateTime().toLocalDate());
        Optional<TrackedPoint> point = pointService.findById(1L, date);
        assertThat(point).isNotNull();
        //assertThat(point.get().getName()).isEqualTo("Piazza dei signori");
        verifyFindByIdIsCalledOnce();
    }

    private void verifyFindAllTrackedPointsIsCalledOnce() {
        Mockito.verify(pointRepository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(pointRepository);
    }

    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(pointRepository, VerificationModeFactory.times(1)).findById(Mockito.anyLong());
        Mockito.reset(pointRepository);
    }
}
