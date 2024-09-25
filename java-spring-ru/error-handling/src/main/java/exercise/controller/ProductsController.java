package exercise.controller;

import exercise.exception.ResourceNotFoundException;
import exercise.model.Product;
import exercise.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path = "")
    public List<Product> index() {
        return productRepository.findAll();
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // BEGIN
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return productRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    }

    @PutMapping("/{id}")
    public Product updateProductById(@PathVariable String id, @RequestBody Product product) {
        if (!productRepository.existsById(Long.parseLong(id))) {
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        }
        product.setId(Long.parseLong(id));
        return productRepository.save(product);
    }
    // END

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        productRepository.deleteById(id);
    }
}
