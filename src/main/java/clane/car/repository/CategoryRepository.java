package clane.car.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import clane.car.model.Category;
import java.util.Optional;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "categories", path = "categories")
public interface CategoryRepository extends JpaRepository<Category, String> {

    Optional<Category> findDistinctByName(String name);
}
