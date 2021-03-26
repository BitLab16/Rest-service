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

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class GatheringRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GatheringRepository gatheringRepository;

    @Test
     public void whenFindLastGatheringForAllPoint_thenCollectionOfLastGatheringForAllPointsShouldBeReturned(){
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
         g1.setPoint(p1);
         g1.setFlow(10);
         g1.setDetectionTime(new Timestamp(1464243600000L));
         g1.setSeason(Season.SPRING);
         g1.setHoliday(false);
         g1.setTimeIndex(0L);
         g1.setWeatherIndex(0L);
         g1.setSeasonIndex(0L);
         g1.setAttractionIndex(0L);
         var g2 = new Gathering();
         g2.setPoint(p1);
         g2.setFlow(10);
         g2.setDetectionTime(new Timestamp(1464243500000L));
         g2.setSeason(Season.SPRING);
         g2.setHoliday(false);
         g2.setTimeIndex(0L);
         g2.setWeatherIndex(0L);
         g2.setSeasonIndex(0L);
         g2.setAttractionIndex(0L);
         var g3 = new Gathering();
         g3.setPoint(p2);
         g3.setFlow(10);
         g3.setDetectionTime(new Timestamp(1464243500000L));
         g3.setSeason(Season.SPRING);
         g3.setHoliday(false);
         g3.setTimeIndex(0L);
         g3.setWeatherIndex(0L);
         g3.setSeasonIndex(0L);
         g3.setAttractionIndex(0L);

         entityManager.persist(p1);
         entityManager.persist(p2);
         entityManager.persist(g1);
         entityManager.persist(g2);
         entityManager.persist(g3);
         entityManager.flush();

         Collection<Gathering> found = gatheringRepository.findLastGatheringForAllPoint();

         assertThat(found).hasSize(2).extracting(Gathering::getId).contains(
                  g1.getId(), g3.getId());

    }

    @Test
    public void whenPastDayGathering_thenAllPastGatheringsInThatDayShouldBeReturned(){

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
        g1.setPoint(p1);
        g1.setFlow(10);
        g1.setDetectionTime(new Timestamp(1464243500000L));
        g1.setSeason(Season.SPRING);
        g1.setHoliday(false);
        g1.setTimeIndex(0L);
        g1.setWeatherIndex(0L);
        g1.setSeasonIndex(0L);
        g1.setAttractionIndex(0L);
        var g2 = new Gathering();
        g2.setPoint(p2);
        g2.setFlow(10);
        g2.setDetectionTime(new Timestamp(1464243500000L));
        g2.setSeason(Season.SPRING);
        g2.setHoliday(false);
        g2.setTimeIndex(0L);
        g2.setWeatherIndex(0L);
        g2.setSeasonIndex(0L);
        g2.setAttractionIndex(0L);

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
    public void whenFutureDayGathering_thenAllFutureGatheringPredictionsInThatDayShouldBeReturned(){

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
        g1.setPoint(p1);
        g1.setFlow(10);
        g1.setDetectionTime(new Timestamp(11564243500000L));
        g1.setSeason(Season.SPRING);
        g1.setHoliday(false);
        g1.setTimeIndex(0L);
        g1.setWeatherIndex(0L);
        g1.setSeasonIndex(0L);
        g1.setAttractionIndex(0L);
        var g2 = new Gathering();
        g2.setPoint(p2);
        g2.setFlow(10);
        g2.setDetectionTime(new Timestamp(11564243500000L));
        g2.setSeason(Season.SPRING);
        g2.setHoliday(false);
        g2.setTimeIndex(0L);
        g2.setWeatherIndex(0L);
        g2.setSeasonIndex(0L);
        g2.setAttractionIndex(0L);

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
    public void whenFindPastGatherings_thenTheLastGatheringForAllPointsBeforeThatTimeShouldBeReturned(){
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
        g1.setPoint(p1);
        g1.setFlow(10);
        g1.setDetectionTime(new Timestamp(1364243500000L));
        g1.setSeason(Season.SPRING);
        g1.setHoliday(false);
        g1.setTimeIndex(0L);
        g1.setWeatherIndex(0L);
        g1.setSeasonIndex(0L);
        g1.setAttractionIndex(0L);
        var g2 = new Gathering();
        g2.setPoint(p2);
        g2.setFlow(10);
        g2.setDetectionTime(new Timestamp(1364243500000L));
        g2.setSeason(Season.SPRING);
        g2.setHoliday(false);
        g2.setTimeIndex(0L);
        g2.setWeatherIndex(0L);
        g2.setSeasonIndex(0L);
        g2.setAttractionIndex(0L);

        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.persist(g1);
        entityManager.persist(g2);
        entityManager.flush();

        Collection<Gathering> found = gatheringRepository.findPastGatherings(new Timestamp(1464243600000L));

        assertThat(found).hasSize(2).extracting(Gathering::getId).contains(
                g1.getId(), g2.getId());

    }

    /*@Test
    public void whenFindFutureGatherings_thenTheLastGatheringPredictionForAllPointsAfterThatTimeShouldBeReturned(){
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
        g1.setPoint(p1);
        g1.setFlow(10);
        g1.setDetectionTime(new Timestamp(11564243500000L));
        g1.setSeason(Season.SPRING);
        g1.setHoliday(false);
        g1.setTimeIndex(0L);
        g1.setWeatherIndex(0L);
        g1.setSeasonIndex(0L);
        g1.setAttractionIndex(0L);
        var g2 = new Gathering();
        g2.setPoint(p2);
        g2.setFlow(10);
        g2.setDetectionTime(new Timestamp(11564243500000L));
        g2.setSeason(Season.SPRING);
        g2.setHoliday(false);
        g2.setTimeIndex(0L);
        g2.setWeatherIndex(0L);
        g2.setSeasonIndex(0L);
        g2.setAttractionIndex(0L);

        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.persist(g1);
        entityManager.persist(g2);
        entityManager.flush();

        Collection<Gathering> found = gatheringRepository.findFutureGatherings(new Timestamp(11564233500000L));

        assertThat(found).hasSize(2).extracting(Gathering::getId).contains(
                g1.getId(), g2.getId());

    }*/
}
