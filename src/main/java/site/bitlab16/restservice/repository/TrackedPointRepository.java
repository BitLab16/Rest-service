package site.bitlab16.restservice.repository;

import org.springframework.stereotype.Repository;
import site.bitlab16.restservice.model.TrackedPoint;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TrackedPointRepository extends JpaRepository<TrackedPoint, Long>{
}
