package davidepan.capstone.controllers;

import davidepan.capstone.entities.Category;
import davidepan.capstone.payloads.CategoryDTO;
import davidepan.capstone.payloads.CategoryResponseDTO;
import davidepan.capstone.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryResponseDTO> getAll(){
        return categoryService.findAll()
                .stream()
                .map(categoryService::convertToResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoryResponseDTO getById(@PathVariable Long id){
        Category category = categoryService.findById(id);
        return categoryService.convertToResponseDTO(category);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDTO create(@RequestBody @Validated CategoryDTO body){
        Category category = categoryService.save(body);

        return categoryService.convertToResponseDTO(category);
    }

    @PutMapping("/{id}")
    public CategoryResponseDTO update(@PathVariable Long id, @RequestBody @Validated CategoryDTO body){
        Category category = categoryService.update(id, body);

        return categoryService.convertToResponseDTO(category);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        categoryService.delete(id);
    }
}
