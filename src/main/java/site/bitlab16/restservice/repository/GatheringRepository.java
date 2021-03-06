package site.bitlab16.restservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.bitlab16.restservice.model.Gathering;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface GatheringRepository extends JpaRepository<Gathering, Long> {

    @Query(value = "WITH summary AS (\n" +
            "    SELECT g.id,\n" +
            "           g.tracked_point_id,\n" +
            "           g.detection_time,\n" +
            "           ROW_NUMBER() OVER(PARTITION BY g.tracked_point_id\n" +
            "               ORDER BY g.detection_time DESC) AS rk\n" +
            "    FROM gatherings_detection g)\n" +
            "SELECT s.*\n" +
            "FROM summary s\n" +
            "WHERE s.rk = 1", nativeQuery = true)
    List<Gathering> findLastGatheringForAllPoint();

    @Query(value = """
            WITH summary AS(
                SELECT g.id, 
                       g.tracked_point_id,
                       g.detection_time,
                       ROW_NUMBER() OVER(PARTITION BY g.tracked_point_id)
                           ORDER BY g.detection_time DESC) as rk
                FROM gatherings_detection g
                WHERE g.detection_time < :time)
            SELECT s.*
            FROM summary s
            WHERE s.rk = 1
            """, nativeQuery = true)
    List<Gathering> findPastGatherings(@Param("time") Timestamp time);
}
