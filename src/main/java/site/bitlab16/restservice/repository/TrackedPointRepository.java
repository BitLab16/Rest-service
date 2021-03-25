package site.bitlab16.restservice.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.bitlab16.restservice.model.Gathering;
import site.bitlab16.restservice.model.TrackedPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
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

    @Query(value = """
                WITH summary AS (
                    SELECT *,
                       ROW_NUMBER() OVER(PARTITION BY g.tracked_point_id
                           ORDER BY g.detection_time DESC) AS rk
                    FROM gatherings_detection g
                    WHERE g.detection_time < '2019-07-27 12:00:00.000000')
                SELECT s.*
                FROM summary s
                WHERE s.rk = 1""", nativeQuery = true)
    Collection<TrackedPoint> findLastGatheringForAllPoint();

    /*@Query(value = """
                SELECT *
                FROM gatherings_detection g JOIN tracked_point tp on tp.id = g.tracked_point_id
                WHERE date_trunc('day', g.detection_time) = :day
            """, nativeQuery = true)*/
    @Query(value = "FROM TrackedPoint as tp JOIN tp.gatherings as ga WHERE date_trunc('day', ga.detection_time) = :day",
    nativeQuery = true)
    Collection<TrackedPoint> getPastDayGathering(Date day);

    @Query(value = """
                SELECT *
                FROM gatherings_prediction g JOIN tracked_point tp on tp.id = g.tracked_point_id
                WHERE date_trunc('day', g.detection_time) = :day
            """, nativeQuery = true)
    Collection<TrackedPoint> getFutureDayGathering(Date day);

    @Query(value = """
            WITH summary AS(
                SELECT *,
                    ROW_NUMBER() OVER(PARTITION BY g.tracked_point_id
                           ORDER BY g.detection_time DESC) as rk
                FROM gatherings_detection g
                WHERE g.detection_time < :time)
            SELECT s.*
            FROM summary s
            WHERE s.rk = 1
            """, nativeQuery = true)
    Collection<TrackedPoint> findPastGatherings(@Param("time") Timestamp time);

    @Query(value = """
            WITH summary AS(
                SELECT *,
                    ROW_NUMBER() OVER(PARTITION BY g.tracked_point_id
                        ORDER BY g.detection_time ASC) as rk
                FROM gatherings_prediction g
                WHERE g.detection_time > :time)
            SELECT s.*
            FROM summary s
            WHERE s.rk = 1
            """, nativeQuery = true)
    Collection<TrackedPoint> findFutureGatherings(@Param("time") Timestamp time);
}
