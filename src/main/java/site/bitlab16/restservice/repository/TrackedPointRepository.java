package site.bitlab16.restservice.repository;

import com.sun.istack.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.bitlab16.restservice.model.TrackedPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface TrackedPointRepository extends JpaRepository<TrackedPoint, Long>{

    @Query(value = "WITH summary AS (\n" +
            "    SELECT t.id,\n" +
            "           t.code,\n" +
            "           t.point_name,\n" +
            "           t.description, \n" +
            "           t.location \n" +
            "    FROM tracked_point t)\n" +
            "SELECT s.*\n" +
            "FROM summary s\n" +
            "WHERE s.id = :id", nativeQuery = true)
    @Override
    Optional<TrackedPoint> findById(@Param("id") Long id);
}
