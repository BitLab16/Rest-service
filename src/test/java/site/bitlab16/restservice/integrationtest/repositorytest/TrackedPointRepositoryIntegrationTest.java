package site.bitlab16.restservice.integrationtest.repositorytest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import site.bitlab16.restservice.model.TrackedPoint;
import site.bitlab16.restservice.repository.TrackedPointRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TrackedPointRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TrackedPointRepository pointRepository;

    @Test
    public void whenFindByCode_thenTrackedPointWithThatCodeShouldBeReturned(){
        GeometryFactory factory = new GeometryFactory();
        var p1 = new TrackedPoint();
        p1.setName("Piazza della frutta");
        p1.setCode(200L);
        p1.setDescription("Una delle piazze più importanti di padova");
        p1.setLocation(factory.createPoint(new Coordinate(-110, 30)));

        entityManager.persist(p1);
        entityManager.flush();

        Optional<TrackedPoint> found = pointRepository.findByCode(200L);

        assertThat(found.get().getCode()).isEqualTo(200L);
    }
}
