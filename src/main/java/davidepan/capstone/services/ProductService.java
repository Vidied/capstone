package davidepan.capstone.services;

import davidepan.capstone.entities.Category;
import davidepan.capstone.entities.Ingredient;
import davidepan.capstone.entities.Product;
import davidepan.capstone.exceptions.NotFoundException;
import davidepan.capstone.payloads.ProductDTO;
import davidepan.capstone.payloads.ProductResponseDTO;
import davidepan.capstone.repositories.CategoryRepository;
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
    private CategoryRepository categoryRepository;

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
        Category category = categoryRepository.findById(body.categoryId())
                .orElseThrow(()-> new NotFoundException("Categoria con ID " + body.categoryId() + " non trovata"));

        List<Ingredient> ingredients = new ArrayList<>();
        if (body.ingredientIds() != null && !body.ingredientIds().isEmpty()){
            ingredients = ingredientRepository.findAllById(body.ingredientIds());
            if (ingredients.size() != body.ingredientIds().size()){
                throw new NotFoundException("Uno o più ingredienti non sono stati trovati");
            }
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
        Category category = categoryRepository.findById(body.categoryId())
                .orElseThrow(() -> new NotFoundException("Categoria con ID " + body.categoryId() + " non trovata"));

        List<Ingredient> ingredients = new ArrayList<>();
        if (body.ingredientIds() != null && !body.ingredientIds().isEmpty()){
            ingredients = ingredientRepository.findAllById(body.ingredientIds());
            if (ingredients.size() != body.ingredientIds().size()){
                throw new NotFoundException("Uno o più ingredienti non sono stati trovati");
            }
        }

        boolean hasUnavailableIngredient = ingredients.stream()
                .anyMatch(ingredient -> Boolean.FALSE.equals(ingredient.getIsAvailable()));

        found.setName(body.name());
        found.setDescription(body.description());
        found.setPrice(body.price());
        found.setCategory(category);

        found.getIngredients().clear();
        found.getIngredients().addAll(ingredients);

        // Se almeno un ingrediente non è disponibile il prodotto rimane non disponibile
        if (hasUnavailableIngredient) {
            found.setIsAvailable(false);
        } else {
            found.setIsAvailable(body.isAvailable() != null ? body.isAvailable() : true);
        }

        return productRepository.save(found);
    }

    public Product toggleAvailability(Long id) {
        Product product = this.findById(id);
        product.setIsAvailable(!product.getIsAvailable());
        return productRepository.save(product);
    }

    public void delete(Long id){
        Product found = this.findById(id);
        productRepository.delete(found);
    }

    public ProductResponseDTO convertToResponseDto(Product product) {
        List<String> ingredientNames = product.getIngredients() != null
                ? product.getIngredients().stream().map(Ingredient::getName).toList()
                : List.of();

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getIsAvailable(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getCategory() != null ? product.getCategory().getName() : null,
                ingredientNames
        );
    }
}
