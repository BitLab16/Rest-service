package site.bitlab16.restservice.integrationtest.repositorytest;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.model.Season;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.repository.GatheringRepository;

import java.util.Calendar;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
class GatheringRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GatheringRepository gatheringRepository;

    @Test
    void whenFindLastGatheringForAllPoint_thenCollectionOfLastGatheringForAllPointsShouldBeReturned(){
        GeometryFactory factory = new GeometryFactory();
        var p1 = new TrackedPoint();
        p1.setName("Piazza della frutta");
        p1.setCode(200L);
        p1.setDescription("Una delle piazze più importanti di padova");
        p1.setLocation(factory.createPoint(new Coordinate(-110, 30)));
        var p2 = new TrackedPoint();
        p2.setName("Prato della valle");
        p2.setCode(300L);
        p2.setDescription("Una delle piazze più importanti di padova");
        p2.setLocation(factory.createPoint(new Coordinate(-110, 30)));

        var g1 = new Gathering();
        g1.setPoint(1L);
        g1.setFlow(10);
        g1.setDetectionTime(new Timestamp(1464243600000L));
        g1.setSeason(Season.SPRING);
        g1.setHoliday(false);
        g1.setTimeIndex(0L);
        g1.setWeatherIndex(0L);
        g1.setSeasonIndex(0L);
        g1.setAttractionsIndex(0L);
        var g2 = new Gathering();
        g2.setPoint(1L);
        g2.setFlow(10);
        g2.setDetectionTime(new Timestamp(1464243500000L));
        g2.setSeason(Season.SPRING);
        g2.setHoliday(false);
        g2.setTimeIndex(0L);
        g2.setWeatherIndex(0L);
        g2.setSeasonIndex(0L);
        g2.setAttractionsIndex(0L);
        var g3 = new Gathering();
        g3.setPoint(2L);
        g3.setFlow(10);
        g3.setDetectionTime(new Timestamp(1464243500000L));
        g3.setSeason(Season.SPRING);
        g3.setHoliday(false);
        g3.setTimeIndex(0L);
        g3.setWeatherIndex(0L);
        g3.setSeasonIndex(0L);
        g3.setAttractionsIndex(0L);

        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.persist(g1);
        entityManager.persist(g2);
        entityManager.persist(g3);
        entityManager.flush();

        Collection<Gathering> found = gatheringRepository.findLastGatheringForAllPoint();

        assertThat(found).hasSize(2).extracting(Gathering::getId).contains(g1.getId(), g3.getId());
    }

    @Test
    void whenPastDayGathering_thenAllPastGatheringsInThatDayShouldBeReturned(){

        GeometryFactory factory = new GeometryFactory();
        var p1 = new TrackedPoint();
        p1.setName("Piazza della frutta");
        p1.setCode(200L);
        p1.setDescription("Una delle piazze più importanti di padova");
        p1.setLocation(factory.createPoint(new Coordinate(-110, 30)));
        var p2 = new TrackedPoint();
        p2.setName("Prato della valle");
        p2.setCode(300L);
        p2.setDescription("Una delle piazze più importanti di padova");
        p2.setLocation(factory.createPoint(new Coordinate(-110, 30)));

        var g1 = new Gathering();
        g1.setPoint(1L);
        g1.setFlow(10);
        g1.setDetectionTime(new Timestamp(1464243500000L));
        g1.setSeason(Season.SPRING);
        g1.setHoliday(false);
        g1.setTimeIndex(0L);
        g1.setWeatherIndex(0L);
        g1.setSeasonIndex(0L);
        g1.setAttractionsIndex(0L);
        var g2 = new Gathering();
        g2.setPoint(2L);
        g2.setFlow(10);
        g2.setDetectionTime(new Timestamp(1464243500000L));
        g2.setSeason(Season.SPRING);
        g2.setHoliday(false);
        g2.setTimeIndex(0L);
        g2.setWeatherIndex(0L);
        g2.setSeasonIndex(0L);
        g2.setAttractionsIndex(0L);

        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.persist(g1);
        entityManager.persist(g2);
        entityManager.flush();

        Collection<Gathering> found = gatheringRepository.getPastDayGathering(
                Date.valueOf(new Timestamp(1464243600000L).toLocalDateTime().toLocalDate()));

        assertThat(found).hasSize(2).extracting(Gathering::getId).contains(
                g1.getId(), g2.getId());

    }

    @Test
    void whenPastDayGatheringWithId_thenAllPastGatheringsOfPointInThatDayShouldBeReturned(){

        GeometryFactory factory = new GeometryFactory();
        var p1 = new TrackedPoint();
        p1.setName("Piazza della frutta");
        p1.setCode(200L);
        p1.setDescription("Una delle piazze più importanti di padova");
        p1.setLocation(factory.createPoint(new Coordinate(-110, 30)));

        var g1 = new Gathering();
        g1.setPoint(1L);
        g1.setFlow(10);
        g1.setDetectionTime(new Timestamp(1464243500000L));
        g1.setSeason(Season.SPRING);
        g1.setHoliday(false);
        g1.setTimeIndex(0L);
        g1.setWeatherIndex(0L);
        g1.setSeasonIndex(0L);
        g1.setAttractionsIndex(0L);
        var g2 = new Gathering();
        g2.setPoint(1L);
        g2.setFlow(10);
        g2.setDetectionTime(new Timestamp(1464243500000L));
        g2.setSeason(Season.SPRING);
        g2.setHoliday(false);
        g2.setTimeIndex(0L);
        g2.setWeatherIndex(0L);
        g2.setSeasonIndex(0L);
        g2.setAttractionsIndex(0L);

        entityManager.persist(p1);
        entityManager.persist(g1);
        entityManager.persist(g2);
        entityManager.flush();

        Collection<Gathering> found = gatheringRepository.getPastDayGathering(1L,
                Date.valueOf(new Timestamp(1464243600000L).toLocalDateTime().toLocalDate()));

        assertThat(found).hasSize(2).extracting(Gathering::getId).contains(
                g1.getId(), g2.getId());

    }

    @Test
    void whenFutureDayGathering_thenAllFutureGatheringPredictionsInThatDayShouldBeReturned(){
        GeometryFactory factory = new GeometryFactory();
        var p1 = new TrackedPoint();
        p1.setName("Piazza della frutta");
        p1.setCode(200L);
        p1.setDescription("Una delle piazze più importanti di padova");
        p1.setLocation(factory.createPoint(new Coordinate(-110, 30)));
        var p2 = new TrackedPoint();
        p2.setName("Prato della valle");
        p2.setCode(300L);
        p2.setDescription("Una delle piazze più importanti di padova");
        p2.setLocation(factory.createPoint(new Coordinate(-110, 30)));

        var g1 = new Gathering();
        g1.setPoint(1L);
        g1.setFlow(10);
        g1.setDetectionTime(new Timestamp(11564243500000L));
        g1.setSeason(Season.SPRING);
        g1.setHoliday(false);
        g1.setTimeIndex(0L);
        g1.setWeatherIndex(0L);
        g1.setSeasonIndex(0L);
        g1.setAttractionsIndex(0L);
        var g2 = new Gathering();
        g2.setPoint(2L);
        g2.setFlow(10);
        g2.setDetectionTime(new Timestamp(11564243500000L));
        g2.setSeason(Season.SPRING);
        g2.setHoliday(false);
        g2.setTimeIndex(0L);
        g2.setWeatherIndex(0L);
        g2.setSeasonIndex(0L);
        g2.setAttractionsIndex(0L);

        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.persist(g1);
        entityManager.persist(g2);
        entityManager.flush();

        Collection<Gathering> found = gatheringRepository.getPastDayGathering(
                Date.valueOf(new Timestamp(11564243500000L).toLocalDateTime().toLocalDate()));
        assertThat(found).hasSize(2).extracting(Gathering::getId).contains(
                g1.getId(), g2.getId());

    }

    @Test
    void whenFindPastGatherings_thenTheLastGatheringForAllPointsBeforeThatTimeShouldBeReturned(){
        GeometryFactory factory = new GeometryFactory();
        var p1 = new TrackedPoint();
        p1.setName("Piazza della frutta");
        p1.setCode(200L);
        p1.setDescription("Una delle piazze più importanti di padova");
        p1.setLocation(factory.createPoint(new Coordinate(-110, 30)));
        var p2 = new TrackedPoint();
        p2.setName("Prato della valle");
        p2.setCode(300L);
        p2.setDescription("Una delle piazze più importanti di padova");
        p2.setLocation(factory.createPoint(new Coordinate(-110, 30)));

        var g1 = new Gathering();
        g1.setPoint(1L);
        g1.setFlow(10);
        g1.setDetectionTime(new Timestamp(1364243500000L));
        g1.setSeason(Season.SPRING);
        g1.setHoliday(false);
        g1.setTimeIndex(0L);
        g1.setWeatherIndex(0L);
        g1.setSeasonIndex(0L);
        g1.setAttractionsIndex(0L);
        var g2 = new Gathering();
        g2.setPoint(2L);
        g2.setFlow(10);
        g2.setDetectionTime(new Timestamp(1364243500000L));
        g2.setSeason(Season.SPRING);
        g2.setHoliday(false);
        g2.setTimeIndex(0L);
        g2.setWeatherIndex(0L);
        g2.setSeasonIndex(0L);
        g2.setAttractionsIndex(0L);

        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.persist(g1);
        entityManager.persist(g2);
        entityManager.flush();

        Collection<Gathering> found = gatheringRepository.findPastGatherings(new Timestamp(1464243600000L));

        assertThat(found).hasSize(2).extracting(Gathering::getId).contains(
                g1.getId(), g2.getId());

    }

    @Test
    void whenIntervalGatheringFromDate_thenCollectionOfGatheringShouldBeReturned(){
        GeometryFactory factory = new GeometryFactory();
        var c = Calendar.getInstance();
        var from = Calendar.getInstance();
        from.set(2019, Calendar.JULY, 5);
        var to = Calendar.getInstance();
        to.set(2019, Calendar.JULY, 3);

        var p1 = new TrackedPoint();
        p1.setName("Piazza della frutta");
        p1.setCode(200L);
        p1.setDescription("Una delle piazze più importanti di padova");
        p1.setLocation(factory.createPoint(new Coordinate(-110, 30)));

        var g1 = new Gathering();
        g1.setPoint(1L);
        g1.setFlow(10);
        c.set(2019, Calendar.JULY,2);
        g1.setDetectionTime(new Timestamp(c.getTimeInMillis()));
        g1.setSeason(Season.SPRING);
        g1.setHoliday(false);
        g1.setTimeIndex(0L);
        g1.setWeatherIndex(0L);
        g1.setSeasonIndex(0L);
        g1.setAttractionsIndex(0L);
        var g2 = new Gathering();
        g2.setPoint(1L);
        g2.setFlow(10);
        c.set(2019, Calendar.JULY,3);
        g2.setDetectionTime(new Timestamp(c.getTimeInMillis()));
        g2.setSeason(Season.SPRING);
        g2.setHoliday(false);
        g2.setTimeIndex(0L);
        g2.setWeatherIndex(0L);
        g2.setSeasonIndex(0L);
        g2.setAttractionsIndex(0L);
        var g3 = new Gathering();
        g3.setPoint(1L);
        g3.setFlow(10);
        c.set(2019, Calendar.JULY,4);
        g3.setDetectionTime(new Timestamp(c.getTimeInMillis()));
        g3.setSeason(Season.SPRING);
        g3.setHoliday(false);
        g3.setTimeIndex(0L);
        g3.setWeatherIndex(0L);
        g3.setSeasonIndex(0L);
        g3.setAttractionsIndex(0L);
        var g4 = new Gathering();
        g4.setPoint(1L);
        g4.setFlow(10);
        c.set(2019, Calendar.JULY,5);
        g4.setDetectionTime(new Timestamp(c.getTimeInMillis()));
        g4.setSeason(Season.SPRING);
        g4.setHoliday(false);
        g4.setTimeIndex(0L);
        g4.setWeatherIndex(0L);
        g4.setSeasonIndex(0L);
        g4.setAttractionsIndex(0L);


        entityManager.persist(p1);
        entityManager.persist(g1);
        entityManager.persist(g2);
        entityManager.persist(g3);
        entityManager.persist(g4);
        entityManager.flush();

        Collection<Gathering> found = gatheringRepository.intervalGatheringFromDate(1L,
                new Date(from.getTimeInMillis()), new Date(to.getTimeInMillis()));

        assertThat(found).hasSize(3).extracting(Gathering::getId).contains(
                g2.getId(), g3.getId(), g4.getId());

    }
}
