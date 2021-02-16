package site.bitlab16.restservice.integrationtest.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.repository.TrackedPointRepository;
import site.bitlab16.restservice.service.TrackedPointService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        var p1 = new TrackedPoint(1L,"Piazza dei signori", 150);
        var p2 = new TrackedPoint(2L,"Prato della valle", 500);
        var p3 = new TrackedPoint(3L,"Piazza della frutta", 200);

        Mockito.when(pointRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));
        Mockito.when(pointRepository.findById(p1.getId())).thenReturn(java.util.Optional.of(p1));
    }

    @Test
    public void whenGetAll_thenTrackedPointShouldBeReturn() {
        var p1 = new TrackedPoint(1L,"Piazza dei signori", 150);
        var p2 = new TrackedPoint(2L,"Prato della valle", 500);
        var p3 = new TrackedPoint(3L,"Piazza della frutta", 200);
        List<TrackedPoint> points = pointService.findAll();
        verifyFindAllTrackedPointsIsCalledOnce();
        assertThat(points).hasSize(3).extracting(TrackedPoint::getPlaceName).contains(p1.getPlaceName(), p2.getPlaceName(), p3.getPlaceName());
    }

    @Test
    public void whenInvalidId_thenTrackedPointShouldNotBeFound() {
        assertThat(pointService.findById(-99L)).isEmpty();
    }

    @Test
    public void whenValidId_thenTrackedPointShouldNotBeFound() {
        Optional<TrackedPoint> point = pointService.findById(1L);
        assertThat(point.orElse(new TrackedPoint("Piazza dei signori", 150)) .getPlaceName()).isEqualTo("Piazza dei signori");
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
