package site.bitlab16.restservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.bitlab16.restservice.model.TrackedPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface TrackedPointRepository extends JpaRepository<TrackedPoint, Long>{

    @Query(value = """
                SELECT *
                FROM tracked_point t
                WHERE t.code = :code 
            """, nativeQuery = true)
    Optional<TrackedPoint> findByCode(Long code);
}
