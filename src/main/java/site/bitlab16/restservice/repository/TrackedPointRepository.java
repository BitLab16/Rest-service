package site.bitlab16.restservice.repository;

import site.bitlab16.restservice.model.TrackedPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackedPointRepository extends JpaRepository<TrackedPoint, Long>{

}
