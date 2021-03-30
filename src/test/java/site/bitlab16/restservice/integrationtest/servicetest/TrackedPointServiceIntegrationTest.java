package site.bitlab16.restservice.integrationtest.servicetest;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
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
import site.bitlab16.restservice.repository.TrackedPointRepository;
import site.bitlab16.restservice.service.GatheringService;
import site.bitlab16.restservice.service.TrackedPointService;
import org.locationtech.jts.geom.Point;

import java.sql.Date;
import java.sql.Timestamp;
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
    public void whenInvalidId_thenTrackedPointShouldNotBeFound() {
        Mockito.when(pointRepository.findById(-99L)).thenReturn(java.util.Optional.empty());
        assertThat(pointService.findById(-99L, Date.valueOf(new Timestamp(1564223400000L).toLocalDateTime().toLocalDate()))).isEmpty();
    }

    @Test
    public void whenValidId_thenTrackedPointShouldBeFound() {
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
        Mockito.when(pointRepository.findById(1L)).thenReturn(java.util.Optional.of(p1));
        Mockito.when(gatheringServiceMock.dayGathering(1L,
                Date.valueOf(new Timestamp(1564223400000L).toLocalDateTime().toLocalDate())))
                .thenReturn(Arrays.asList(g1, g2, g3, g4));
        Date date = Date.valueOf(new Timestamp(1564223400000L).toLocalDateTime().toLocalDate());
        Optional<TrackedPoint> point = pointService.findById(1L, date);
        assertThat(point.get().getName()).isEqualTo("Piazza dei signori");
        assertThat(point.get().getGatherings()).hasSize(4);
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
