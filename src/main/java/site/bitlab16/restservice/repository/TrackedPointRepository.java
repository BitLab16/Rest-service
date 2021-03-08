package site.bitlab16.restservice.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.bitlab16.restservice.model.TrackedPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface TrackedPointRepository extends JpaRepository<TrackedPoint, Long>{

    @Query(value = """
            WITH summary AS (
                SELECT *
                FROM tracked_point t)
            SELECT s.*
            FROM summary s
            WHERE s.id = :id""", nativeQuery = true)
    @Override
    Optional<TrackedPoint> findById(@Param("id") Long id);
}
