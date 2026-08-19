package davidepan.capstone.services;

import davidepan.capstone.entities.Category;
import davidepan.capstone.entities.Ingredient;
import davidepan.capstone.entities.Product;
import davidepan.capstone.exceptions.NotFoundException;
import davidepan.capstone.payloads.ProductDTO;
import davidepan.capstone.repositories.IngredientRepository;
import davidepan.capstone.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private IngredientRepository ingredientRepository;

    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public Product findById(Long id){
        return productRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Prodotto con ID " + id + " non trovato"));
    }

    public List<Product> findByCategoryId(Long categoryId){
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> findAvailable(){
        return productRepository.findByIsAvailableTrue();
    }

    public Product save(ProductDTO body){
        Category category = categoryService.findById(body.categoryId());

        List<Ingredient> ingredients = new ArrayList<>();
        if (body.ingredientIds() != null && !body.ingredientIds().isEmpty()){
            ingredients = ingredientRepository.findAllById(body.ingredientIds());
        }

        boolean hasUnavailableIngredient = ingredients.stream()
                .anyMatch(ingredient -> Boolean.FALSE.equals(ingredient.getIsAvailable()));

        boolean isAvailable = !hasUnavailableIngredient && (body.isAvailable() != null ? body.isAvailable() : true);

        Product product = new Product(
                body.name(),
                body.description(),
                body.price(),
                isAvailable,
                category,
                ingredients
        );

        return productRepository.save(product);
    }

    public Product update(Long id, ProductDTO body){
        Product found = this.findById(id);
        Category category = categoryService.findById(body.categoryId());

        List<Ingredient> ingredients = new ArrayList<>();
        if (body.ingredientIds() != null && !body.ingredientIds().isEmpty()){
            ingredients = ingredientRepository.findAllById(body.ingredientIds());
        }

        boolean hasUnavailableIngredient = ingredients.stream()
                .anyMatch(ingredient -> Boolean.FALSE.equals(ingredient.getIsAvailable()));

        found.setName(body.name());
        found.setDescription(body.description());
        found.setPrice(body.price());

        if(hasUnavailableIngredient){
            found.setIsAvailable(false);
        }else if(body.isAvailable() != null){
            found.setIsAvailable(body.isAvailable());
        };
        found.setCategory(category);
        found.getIngredients().clear();
        found.getIngredients().addAll(ingredients);

        return productRepository.save(found);
    }

    public void delete(Long id){
        Product found = this.findById(id);
        productRepository.delete(found);
    }
}
