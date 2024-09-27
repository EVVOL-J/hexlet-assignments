package exercise.controller;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ProductMapper;
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
    @Autowired
    private ProductMapper productMapper;

    // BEGIN
    @GetMapping(path = "")
    public List<ProductDTO> index() {
        var products = productRepository.findAll();
        return products.stream()
                .map(product -> productMapper.map(product))
                .toList();
    }

    @GetMapping(path = "/{id}")
    public ProductDTO show(@PathVariable long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
        return productMapper.map(product);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody ProductCreateDTO productData) {
        var product = productMapper.map(productData);
        product = productRepository.save(product);
        return productMapper.map(product);
    }

    // BEGIN
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO update(@RequestBody ProductUpdateDTO productUpdateDTO, @PathVariable Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        productMapper.update(productUpdateDTO, product);
        product = productRepository.save(product);
        return productMapper.map(product);
    }
    // END
}
