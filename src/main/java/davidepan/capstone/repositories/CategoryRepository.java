package davidepan.capstone.repositories;

import davidepan.capstone.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByOrderByDisplayOrderAsc();
    boolean existsByDisplayOrder(Integer displayOrder);
    Optional<Category> findByDisplayOrder(Integer displayOrder);
}
