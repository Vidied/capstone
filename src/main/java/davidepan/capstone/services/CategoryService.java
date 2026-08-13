package davidepan.capstone.services;

import davidepan.capstone.entities.Category;
import davidepan.capstone.exceptions.NotFoundException;
import davidepan.capstone.payloads.CategoryDTO;
import davidepan.capstone.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll(){
        return categoryRepository.findAllByOrderByDisplayOrderAsc();
    }

    public Category findById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Categoria con ID " + id + " non trovata"));
    }

    public Category save(CategoryDTO body){
        Category category = new Category(body.name(), body.displayOrder());
        return categoryRepository.save(category);
    }

    public Category update(Long id, CategoryDTO body){
        Category found = this.findById(id);
        found.setDisplayOrder(body.displayOrder());
        found.setName(body.name());
        return categoryRepository.save(found);
    }

    public void delete(Long id){
        Category found = this.findById(id);
        categoryRepository.delete(found);
    }
}
