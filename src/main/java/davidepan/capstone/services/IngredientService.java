package davidepan.capstone.services;

import davidepan.capstone.entities.Ingredient;
import davidepan.capstone.exceptions.NotFoundException;
import davidepan.capstone.payloads.IngredientDTO;
import davidepan.capstone.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    public List<Ingredient> findAll(){
        return ingredientRepository.findAll();
    }

    public Ingredient findById(Long id){
        return ingredientRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Ingrediente con ID " + id + " non trovato"));
    }

    public Ingredient save(IngredientDTO body){
        Ingredient ingredient = new Ingredient(body.name());
        return ingredientRepository.save(ingredient);
    }

    public Ingredient update(Long id, IngredientDTO body){
        Ingredient found = this.findById(id);
        found.setName(body.name());
        return ingredientRepository.save(found);
    }

    public void delete(Long id){
        Ingredient found = this.findById(id);
        ingredientRepository.delete(found);
    }
}
