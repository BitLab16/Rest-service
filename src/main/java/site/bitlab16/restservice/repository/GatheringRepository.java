package site.bitlab16.restservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.bitlab16.restservice.model.Gathering;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

@Repository
public interface GatheringRepository extends JpaRepository<Gathering, Long> {

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
    Collection<Gathering> findLastGatheringForAllPoint();

    @Query(value = """
                SELECT *
                FROM gatherings_detection g
                WHERE date_trunc('day', g.detection_time) = :day AND
                    (EXTRACT(MINUTE FROM g.detection_time) = 30 OR
                    EXTRACT(MINUTE FROM g.detection_time) = 00)  
            """, nativeQuery = true)
    Collection<Gathering> getPastDayHoursGatherings(Date day);

    @Query(value = """
                SELECT *
                FROM gatherings_prediction g
                WHERE date_trunc('day', g.detection_time) = :day AND
                    (EXTRACT(MINUTE FROM g.detection_time) = 30 OR
                    EXTRACT(MINUTE FROM g.detection_time) = 00)  
            """, nativeQuery = true)
    Collection<Gathering> getFutureDayHoursGatherings(Date day);

    @Query(value = """
                SELECT *
                FROM gatherings_detection g
                WHERE date_trunc('day', g.detection_time) = :day AND
                    g.tracked_point_id = :id AND
                    (EXTRACT(MINUTE FROM g.detection_time) = 30 OR
                    EXTRACT(MINUTE FROM g.detection_time) = 00)  
            """, nativeQuery = true)
    Collection<Gathering> getPastDayHoursGatherings(Long id, Date day);

    @Query(value = """
                SELECT *
                FROM gatherings_prediction g
                WHERE date_trunc('day', g.detection_time) = :day AND
                    g.tracked_point_id = :id AND
                    (EXTRACT(MINUTE FROM g.detection_time) = 30 OR
                    EXTRACT(MINUTE FROM g.detection_time) = 00)  
            """, nativeQuery = true)
    Collection<Gathering> getFutureDayHoursGatherings(Long id, Date day);

    @Query(value = """
                SELECT *
                FROM gatherings_detection g
                WHERE date_trunc('day', g.detection_time) = :day
            """, nativeQuery = true)
    Collection<Gathering> getPastDayGathering(Date day);

    @Query(value = """
                SELECT *
                FROM gatherings_prediction g
                WHERE date_trunc('day', g.detection_time) = :day
            """, nativeQuery = true)
    Collection<Gathering> getFutureDayGathering(Date day);

    @Query(value = """
                SELECT *
                FROM gatherings_detection g
                WHERE date_trunc('day', g.detection_time) = :day
                    and g.tracked_point_id = :id
            """, nativeQuery = true)
    Collection<Gathering> getPastDayGathering(Long id, Date day);

    @Query(value = """
                SELECT *
                FROM gatherings_prediction g
                WHERE date_trunc('day', g.detection_time) = :day
                    and g.tracked_point_id = :id
            """, nativeQuery = true)
    Collection<Gathering> getFutureDayGathering(Long id, Date day);

    @Query(value = """
                SELECT *
                FROM gatherings_detection g
                WHERE date_trunc('day',g.detection_time) >= :to
                    and date_trunc('day', g.detection_time) <= :from
                    and g.tracked_point_id = :id
            """, nativeQuery = true)
    Collection<Gathering> intervalGatheringFromDate(Long id, Date from ,Date to);

    @Query(value = """
                SELECT *
                FROM gatherings_detection g
                WHERE g.detection_time > (to_date(:day, 'YYYY-MM-DD') - INTERVAL '1 year')
                    and g.detection_time < :day
            """, nativeQuery = true)
    Collection<Gathering> getYearGatheringFromDate(Date day);

    @Query(value = """
                SELECT *
                FROM gatherings_detection g
                WHERE g.detection_time > (to_date(:day, 'YYYY-MM-DD') - INTERVAL '1 year')
                    and g.detection_time < :day
                    and g.tracked_point_id = :id
            """, nativeQuery = true)
    Collection<Gathering> getYearGatheringFromDate(Long id, Date day);

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
    Collection<Gathering> findPastGatherings(@Param("time") Timestamp time);

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
    Collection<Gathering> findFutureGatherings(@Param("time") Timestamp time);
}
