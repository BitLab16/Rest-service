package site.bitlab16.restservice.unittest.controller;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import site.bitlab16.restservice.controller.TrackedPointController;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.model.Season;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.service.GatheringService;
import site.bitlab16.restservice.service.TrackedPointService;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(TrackedPointController.class)
public class TrackedPointControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TrackedPointService pointService;

    @MockBean
    private GatheringService gatheringService;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.standaloneSetup(new TrackedPointController(pointService, gatheringService)).build();
    }


    @BeforeEach
    void setUp(){
        GeometryFactory factory = new GeometryFactory();
        var p2 = new TrackedPoint(2L,
                "Piazza della frutta",
                200L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)));


        var g2 = new Gathering(2L, p2, 10, new Timestamp(1564243500000L),
                Season.SPRING, false, 0L, 0L, 0L, 0L);


        Mockito.when(pointService.findById(p2.getId())).thenReturn(java.util.Optional.of(p2));
        Mockito.when(gatheringService.futureData(anyObject())).thenReturn(Arrays.asList(g2));

    }

    @Test
    public void pointsShouldReturnLastGatheringForAllPointsFromService() throws Exception{
        GeometryFactory factory = new GeometryFactory();
        var p3 = new TrackedPoint(3L,
                "Prato della valle",
                300L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)));
        var g3 = new Gathering(3L, p3, 10, new Timestamp(1564223400000L),
                Season.SPRING, false, 0L, 0L, 0L, 0L);

        Mockito.when(gatheringService.dayGathering(anyObject())).thenReturn(Arrays.asList(g3));

        mvc.perform(get("/points")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(3L));
    }

    @Test
    public void whenValidId_pointShouldReturnRightPoint() throws Exception{
        mvc.perform(get("/point/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)));
    }

    @Test
    public void whenInvalidId_noPointShouldBeReturned() throws Exception{
        mvc.perform(get("/point/{id}", -99L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void pointsWithTimeShouldReturnAllGatheringsBasedOnTimePassed() throws Exception{
        GeometryFactory factory = new GeometryFactory();
        var p1 = new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)));
        var g1 = new Gathering(1L, p1, 10, new Timestamp(1564216200000L),
                Season.SPRING, false, 0L, 0L, 0L, 0L);

        Mockito.when(gatheringService.dayGathering(anyObject())).thenReturn(Arrays.asList(g1));

        mvc.perform(get("/points/time/{time}", Date.valueOf(new Timestamp(1564216200000L).toLocalDateTime().toLocalDate()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    public void futureDataShouldReturnGatheringsPredictionsForPointsBasedOnTimePassed() throws Exception{
        mvc.perform(get("/point/time/{time}", Date.valueOf(new Timestamp(11564243500000L).toLocalDateTime().toLocalDate()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(2L));
    }

}
