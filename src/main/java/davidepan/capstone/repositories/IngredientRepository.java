package davidepan.capstone.repositories;

import davidepan.capstone.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
