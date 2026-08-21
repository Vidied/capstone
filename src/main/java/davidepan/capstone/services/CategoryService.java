package davidepan.capstone.services;

import davidepan.capstone.entities.Category;
import davidepan.capstone.entities.Product;
import davidepan.capstone.exceptions.BadRequestException;
import davidepan.capstone.exceptions.NotFoundException;
import davidepan.capstone.payloads.CategoryDTO;
import davidepan.capstone.payloads.CategoryResponseDTO;
import davidepan.capstone.payloads.ProductResponseDTO;
import davidepan.capstone.repositories.CategoryRepository;
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

    public Category save(CategoryDTO body){
        if(body.displayOrder() != null && categoryRepository.existsByDisplayOrder(body.displayOrder())){
            throw new BadRequestException("Il display order " + body.displayOrder() + " è già in uso");
        }

        Category category = new Category(body.name(), body.displayOrder());
        return categoryRepository.save(category);
    }

    public Category update(Long id, CategoryDTO body){
        Category found = this.findById(id);

        if(body.displayOrder() != null && !body.displayOrder().equals(found.getDisplayOrder())
                &&categoryRepository.existsByDisplayOrder(body.displayOrder())){
            throw new BadRequestException("Il display order " + body.displayOrder() + " è già in uso");
        }

        found.setDisplayOrder(body.displayOrder());
        found.setName(body.name());
        return categoryRepository.save(found);
    }

    public void delete(Long id){
        Category found = this.findById(id);
        categoryRepository.delete(found);
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
