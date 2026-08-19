package davidepan.capstone.controllers;

import davidepan.capstone.entities.Category;
import davidepan.capstone.payloads.CategoryDTO;
import davidepan.capstone.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAll(){
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public Category getById(@PathVariable Long id){
        return categoryService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@RequestBody CategoryDTO body){
        return categoryService.save(body);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @RequestBody CategoryDTO body){
        return categoryService.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        categoryService.delete(id);
    }
}
