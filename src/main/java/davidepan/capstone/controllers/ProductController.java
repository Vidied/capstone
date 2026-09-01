package davidepan.capstone.controllers;

import davidepan.capstone.entities.Product;
import davidepan.capstone.payloads.ProductDTO;
import davidepan.capstone.payloads.ProductResponseDTO;
import davidepan.capstone.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ProductResponseDTO findById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return productService.convertToResponseDto(product);
    }

    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return productService.findAll()
                .stream()
                .map(productService::convertToResponseDto)
                .toList();
    }

    @GetMapping("/available")
    public List<ProductResponseDTO> getAvailable(){
        return productService.findAvailable()
                .stream()
                .map(productService::convertToResponseDto)
                .toList();
    }

    @PatchMapping("/{id}/availability")
    public ProductResponseDTO toggleAvailability(@PathVariable Long id) {
        Product updated = productService.toggleAvailability(id);
        return productService.convertToResponseDto(updated);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDTO create(@RequestBody @Validated ProductDTO body){
        Product product = productService.save(body);
        return productService.convertToResponseDto(product);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO update(@PathVariable Long id, @RequestBody @Validated ProductDTO body){
        Product updatedProduct = productService.update(id, body);
        return productService.convertToResponseDto(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        productService.delete(id);
    }
}
