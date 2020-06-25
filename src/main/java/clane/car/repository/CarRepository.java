package clane.car.repository;

import clane.car.model.Car;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import clane.car.model.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RestResource;

public interface CarRepository extends JpaRepository<Car, String>, JpaSpecificationExecutor<Car> {

    @Query("select DISTINCT a from Car a where a.number = :number")
    Optional<Car> findDistinctByNumber(@Param("number") String number);

    @RestResource(path = "query")
    @Query("select c from Car c where c.number = :term or c.name like '%:term%'"
            + " or c.description like '%:term%' or c.category.name like '%:term%'"
            + " or :term member of c.tags ")
    List<Car> query(@Param("term") String term);

    long countByCategory(Category category);
}
