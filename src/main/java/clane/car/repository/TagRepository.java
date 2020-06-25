package clane.car.repository;

import clane.car.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "tags", path = "tags")
public interface TagRepository extends JpaRepository<Tag, String> {

    Tag findDistintByNameContainingIgnoreCase(String name);
}
