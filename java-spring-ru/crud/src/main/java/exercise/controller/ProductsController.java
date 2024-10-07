package exercise.controller;

import java.util.List;
import java.util.Optional;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.mapper.ProductMapper;
import exercise.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.ProductRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    // BEGIN
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(t -> productMapper.map(t)).toList();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        Optional<Product> optProduct = productRepository.findById(id);
        if (optProduct.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        return productMapper.map(optProduct.get());

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ProductDTO createProduct(@RequestBody @Valid ProductCreateDTO product) {
        Product crateProduct = productMapper.map(product);
        return productMapper.map(productRepository.save(crateProduct));
    }

    @PutMapping("/{id}")
    private ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDTO updateDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productMapper.update(updateDTO, product);
        return productMapper.map(productRepository.save(product));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void deleteProduct(@PathVariable Long id) {
        productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.deleteById(id);
    }
    // END
}
