package clane.car.repository;

import clane.car.model.Car;
import clane.car.model.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Car image repository class
 *
 * @author Babatope Festus
 */
public interface ImageRepository extends JpaRepository<Image, String> {

    @Query("select DISTINCT i from Image i where i.link = :link")
    Optional<Image> findDistinctByLink(@Param("link") String link);

    List<Image> findByCar(Car car);

    Optional<Image> findByCarAndId(Car car, String id);
}
