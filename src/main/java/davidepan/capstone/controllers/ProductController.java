package davidepan.capstone.controllers;

import davidepan.capstone.entities.Product;
import davidepan.capstone.payloads.ProductDTO;
import davidepan.capstone.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAll(@RequestParam(required = false) Long categoryId){
        if (categoryId != null){
            return productService.findByCategoryId(categoryId);
        }
        return productService.findAll();
    }

    @GetMapping("/available")
    public List<Product> getAvailable(){
        return productService.findAvailable();
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id){
        return productService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody ProductDTO body){
        return productService.save(body);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody ProductDTO body){
        return productService.update(id, body);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        productService.delete(id);
    }
}
