package davidepan.capstone.services;

import davidepan.capstone.entities.Ingredient;
import davidepan.capstone.entities.Product;
import davidepan.capstone.exceptions.NotFoundException;
import davidepan.capstone.payloads.IngredientDTO;
import davidepan.capstone.repositories.IngredientRepository;
import davidepan.capstone.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

     @Autowired
     private ProductRepository productRepository;

    public List<Ingredient> findAll(){
        return ingredientRepository.findAll();
    }

    public Ingredient findById(Long id){
        return ingredientRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Ingrediente con ID " + id + " non trovato"));
    }

    public Ingredient save(IngredientDTO body){
        Ingredient ingredient = new Ingredient(body.name());

        if (body.isAvailable() != null){
            ingredient.setIsAvailable(body.isAvailable());
        }

        return ingredientRepository.save(ingredient);
    }

    public Ingredient update(Long id, IngredientDTO body){
        Ingredient found = this.findById(id);
        found.setName(body.name());

        if(body.isAvailable() != null){
            found.setIsAvailable(body.isAvailable());

            if(!body.isAvailable()){
                List<Product> associatedProducts = productRepository.findByIngredientsId(id);
                for(Product product : associatedProducts){
                    product.setIsAvailable(false);
                }
                productRepository.saveAll(associatedProducts);

            }
        }

        return ingredientRepository.save(found);
    }

    public void delete(Long id){
        Ingredient found = this.findById(id);
        ingredientRepository.delete(found);
    }
}
