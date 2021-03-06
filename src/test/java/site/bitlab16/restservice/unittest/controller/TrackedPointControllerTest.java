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
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.model.TrackedPointStatistic;
import site.bitlab16.restservice.service.TrackedPointService;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(TrackedPointController.class)
class TrackedPointControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TrackedPointService pointService;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.standaloneSetup(new TrackedPointController(pointService)).build();
    }


    @BeforeEach
    void setUp(){
        GeometryFactory factory = new GeometryFactory();
        var p2 = new TrackedPoint(2L,
                "Piazza della frutta",
                200L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)));

        Date date = Date.valueOf(new Timestamp(System.currentTimeMillis()).toLocalDateTime().toLocalDate());

        Mockito.when(pointService.findByCode(p2.getCode())).thenReturn(java.util.Optional.of(p2));
    }

    @Test
    void pointsShouldReturnLastGatheringForAllPointsFromService() throws Exception{
        GeometryFactory factory = new GeometryFactory();
        var p3 = new TrackedPoint(3L,
                "Prato della valle",
                300L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)));

        Mockito.when(pointService.dayHoursGatherings(anyObject())).thenReturn(Collections.singletonList(p3));

        mvc.perform(get("/points")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].code").value(300L));
    }


    @Test
    void whenValidCode_pointShouldReturnRightPoint() throws Exception{
        mvc.perform(get("/point/{id}", 200L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)));
    }

    @Test
    void whenInvalidId_noPointShouldBeReturned() throws Exception{
        mvc.perform(get("/point/{id}", -99L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void pointsWithTimeShouldReturnAllGatheringsBasedOnTimePassed() throws Exception{
        GeometryFactory factory = new GeometryFactory();
        var p1 = new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)));

        Mockito.when(pointService.dayHoursGatherings(anyObject())).thenReturn(Collections.singletonList(p1));

        mvc.perform(get("/points/time/{time}", Date.valueOf(new Timestamp(1564216200000L).toLocalDateTime().toLocalDate()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].code").value(100L));
    }

    @Test
    void pointsWithIncorrectTimeShouldReturnNoGatherings() throws Exception{
        GeometryFactory factory = new GeometryFactory();
        var p1 = new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)));

        Mockito.when(pointService.dayHoursGatherings(Date.valueOf(new Timestamp(1664216200000L)
                .toLocalDateTime().toLocalDate()))).thenReturn(Collections.singletonList(p1));

        mvc.perform(get("/points/time/{time}", Date.valueOf(new Timestamp(1564216200000L).toLocalDateTime().toLocalDate()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void fullDayWithCodeShouldReturnAllGatheringsOfPointWithThatCode() throws Exception {
        GeometryFactory factory = new GeometryFactory();
        var p1 = new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)));
        Timestamp currTime = new Timestamp(System.currentTimeMillis());
        Mockito.when(pointService.dayGathering(100L,Date.valueOf(currTime.toLocalDateTime().toLocalDate())))
                .thenReturn(java.util.Optional.of(p1));

        mvc.perform(get("/point/{code}/full-day", 100L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)));

    }

    @Test
    void fullDayWithInvalidCodeShouldReturnNotFoundStatus() throws Exception {
        GeometryFactory factory = new GeometryFactory();
        var p1 = new TrackedPoint(1L,
                "Piazza dei signori",
                100L,
                "Una delle piazze più importati di padova",
                factory.createPoint(new Coordinate( -110, 30)));
        Timestamp currTime = new Timestamp(System.currentTimeMillis());
        Mockito.when(pointService.dayGathering(100L,Date.valueOf(currTime.toLocalDateTime().toLocalDate())))
                .thenReturn(java.util.Optional.of(p1));

        mvc.perform(get("/point/{code}/full-day", -99L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void whenValidCodeForAvg_pointStatisticShouldBeReturned() throws Exception {
        var expected = new TrackedPointStatistic();
        expected.addMetric(DayOfWeek.TUESDAY, 8, 5);
        expected.addMetric(DayOfWeek.TUESDAY, 9, 9);
        expected.addMetric(DayOfWeek.WEDNESDAY, 9, 12);
        expected.addMetric(DayOfWeek.WEDNESDAY, 10, 8);

        var from = Calendar.getInstance();
        from.set(2019, Calendar.JULY, 5);
        var to = Calendar.getInstance();
        to.set(2019, Calendar.JULY, 3);

        var currTime = new Timestamp(System.currentTimeMillis());

        Mockito.when(pointService.avgFlowByTrackedPointCode(100L,
                Date.valueOf(currTime.toLocalDateTime().toLocalDate()),
                Date.valueOf(currTime.toLocalDateTime().minusWeeks(24).toLocalDate())))
                .thenReturn(Optional.of(expected));

        mvc.perform(get("/point/{id}/avg", 100L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenInvalidCodeForAvg_noPointStatisticShouldBeReturned() throws Exception {
        var expected = new TrackedPointStatistic();
        expected.addMetric(DayOfWeek.TUESDAY, 8, 5);
        expected.addMetric(DayOfWeek.TUESDAY, 9, 9);
        expected.addMetric(DayOfWeek.WEDNESDAY, 9, 12);
        expected.addMetric(DayOfWeek.WEDNESDAY, 10, 8);

        var from = Calendar.getInstance();
        from.set(2019, Calendar.JULY, 5);
        var to = Calendar.getInstance();
        to.set(2019, Calendar.JULY, 3);

        var currTime = new Timestamp(System.currentTimeMillis());

        Mockito.when(pointService.avgFlowByTrackedPointCode(100L,
                Date.valueOf(currTime.toLocalDateTime().toLocalDate()),
                Date.valueOf(currTime.toLocalDateTime().minusWeeks(24).toLocalDate())))
                .thenReturn(Optional.of(expected));

        mvc.perform(get("/point/{id}/avg", -99L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
