package davidepan.capstone.services;

import davidepan.capstone.entities.Category;
import davidepan.capstone.entities.Product;
import davidepan.capstone.exceptions.BadRequestException;
import davidepan.capstone.exceptions.NotFoundException;
import davidepan.capstone.payloads.CategoryDTO;
import davidepan.capstone.payloads.CategoryResponseDTO;
import davidepan.capstone.payloads.ProductResponseDTO;
import davidepan.capstone.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    public List<Category> findAll(){
        return categoryRepository.findAllByOrderByDisplayOrderAsc();
    }

    public Category findById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Categoria con ID " + id + " non trovata"));
    }

    @Transactional
    public Category save(CategoryDTO body){
        Integer targetOrder = body.displayOrder();

        if (targetOrder == null) {
            targetOrder = categoryRepository.findAllByOrderByDisplayOrderAsc()
                    .stream()
                    .mapToInt(Category::getDisplayOrder)
                    .max()
                    .orElse(0) + 1;
        }

        Category category = new Category(body.name(), targetOrder);
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(Long id, CategoryDTO body) {
        Category found = this.findById(id);

        if (body.displayOrder() != null && !body.displayOrder().equals(found.getDisplayOrder())) {
            Integer oldOrder = found.getDisplayOrder();
            Integer newOrder = body.displayOrder();

            categoryRepository.findByDisplayOrder(newOrder).ifPresent(otherCategory -> {
                otherCategory.setDisplayOrder(oldOrder);
                categoryRepository.save(otherCategory);
            });
            found.setDisplayOrder(newOrder);
        }

        found.setName(body.name());
        return categoryRepository.save(found);
    }

    @Transactional
    public void delete(Long id) {
        Category found = this.findById(id);
        Integer deletedOrder = found.getDisplayOrder();

        categoryRepository.delete(found);

        List<Category> categoriesToShift = categoryRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .filter(c -> c.getDisplayOrder() > deletedOrder)
                .toList();

        for (Category cat : categoriesToShift) {
            cat.setDisplayOrder(cat.getDisplayOrder() - 1);
            categoryRepository.save(cat);
        }
    }

    public CategoryResponseDTO convertToResponseDTO(Category category){
        List<ProductResponseDTO> productDTOs = (category.getProducts() != null) ?
                category.getProducts().stream()
                .map(productService::convertToResponseDto)
                .toList() : List.of();

        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDisplayOrder(),
                productDTOs
        );
    }
}
