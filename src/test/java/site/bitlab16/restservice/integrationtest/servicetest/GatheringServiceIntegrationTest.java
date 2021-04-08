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
import site.bitlab16.restservice.model.Season;
import site.bitlab16.restservice.repository.GatheringRepository;
import site.bitlab16.restservice.service.GatheringService;

import java.sql.Date;
import java.sql.Timestamp;
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

    @Test
    void whenGetPastDayGathering_thenListOfGatheringShouldBeReturn() {
        var p1 = new Gathering(1L, 1L,
                10,
                new Timestamp(1564216200000L),
                Season.SPRING, false, 0L, 0L, 0L,0L);
        var p2 = new Gathering(2L, 1L,
                10,
                new Timestamp(1564218000000L),
                Season.SPRING, false, 0L, 0L, 0L,0L);
        var p3 = new Gathering(3L, 1L,
                10,
                new Timestamp(1564221600000L),
                Season.SPRING, false, 0L, 0L, 0L,0L);
        var p4 = new Gathering(4L, 1L,
                10,
                new Timestamp(1564223400000L),
                Season.SPRING, false, 0L, 0L, 0L,0L);

        Mockito.when(gatheringRepository.getPastDayGathering(
                Date.valueOf(new Timestamp(1564221600000L).toLocalDateTime().toLocalDate())))
                .thenReturn(List.of(p1, p2, p3));
        Mockito.when(gatheringRepository.getFutureDayGathering(
                Date.valueOf(new Timestamp(1564221600000L).toLocalDateTime().toLocalDate())))
                .thenReturn(List.of(p4));

        Collection<Gathering> gatherings = gatheringService.dayGathering(
                Date.valueOf(new Timestamp(1564221600000L).toLocalDateTime().toLocalDate()));
        assertThat(gatherings).hasSize(4).extracting(Gathering::getDetectionTime).contains(
                p1.getDetectionTime(), p2.getDetectionTime(), p3.getDetectionTime(), p4.getDetectionTime());
    }

}
