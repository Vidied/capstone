package davidepan.capstone.services;

import davidepan.capstone.entities.Category;
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



    public Category findEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria con ID " + id + " non trovata"));
    }

    public CategoryResponseDTO findById(Long id) {
        Category category = categoryRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Categoria con ID " + id + " non trovata"));
        return convertToResponseDTO(category);
    }

    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAllWithDetails().stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public CategoryResponseDTO save(CategoryDTO body) {
        if (body.displayOrder() != null && categoryRepository.existsByDisplayOrder(body.displayOrder())) {
            throw new BadRequestException("Il display order " + body.displayOrder() + " è già in uso");
        }

        Category category = new Category(body.name(), body.displayOrder());
        Category savedCategory = categoryRepository.save(category);
        return convertToResponseDTO(savedCategory);
    }

    public CategoryResponseDTO update(Long id, CategoryDTO body) {
        Category found = this.findEntityById(id);

        if (body.displayOrder() != null && !body.displayOrder().equals(found.getDisplayOrder())
                && categoryRepository.existsByDisplayOrder(body.displayOrder())) {
            throw new BadRequestException("Il display order " + body.displayOrder() + " è già in uso");
        }

        found.setDisplayOrder(body.displayOrder());
        found.setName(body.name());
        Category updatedCategory = categoryRepository.save(found);
        return convertToResponseDTO(updatedCategory);
    }

    public void delete(Long id) {
        Category found = this.findEntityById(id);
        categoryRepository.delete(found);
    }

    public CategoryResponseDTO convertToResponseDTO(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDisplayOrder(),
                category.getProducts() != null
                        ? category.getProducts().stream()
                        .map(ProductResponseDTO::fromEntity)
                        .toList()
                        : List.of()
        );
    }
}