package site.bitlab16.restservice.integrationtest.servicetest;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.repository.TrackedPointRepository;
import site.bitlab16.restservice.service.TrackedPointService;

import java.util.Arrays;

public class TrackedPointServiceIntegrationTest {

    @TestConfiguration
    static class TrackedPointServiceTestContextConfiguration {

        @Bean
        public TrackedPointService pointService() {
            return new TrackedPointService();
        }
    }

    @Autowired
    private TrackedPointService pointService;

    @MockBean
    private TrackedPointRepository pointRepository;

    @Before
    public void setUpDB() {
        var p1 = new TrackedPoint("Piazza dei signori", 150);
        var p2 = new TrackedPoint("Prato della valle", 500);
        var p3 = new TrackedPoint("Piazza della frutta", 200);

        Mockito.when(pointRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));
        Mockito.when(pointRepository.findById(p1.getId())).thenReturn(java.util.Optional.of(p1));
    }

    @Test
    public void whenGetAll_thenTrackedPointShouldBeReturn() {

    }
}
