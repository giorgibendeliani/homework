package com.bendeliani.homework.controller;

import com.bendeliani.homework.dto.ProductDto;
import com.bendeliani.homework.model.Product;
import com.bendeliani.homework.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@AllArgsConstructor
@RestController
@RequestMapping("api/v1")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        try {
            List<ProductDto> products = productService.getAll()
                    .stream()
                    .map(ProductController::convert)
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id) {
        Optional<Product> product = productService.findById(id);

        return product.map(value -> ResponseEntity.ok().body(convert(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        Product product = productService.create(convert(productDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(convert(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDto dto) {
        Optional<Product> productOptional = productService.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(dto.getName());
            product.setPrice(dto.getPrice());
            product.setAmount(dto.getAmount());
            Product updatedProduct = productService.update(product);
            return ResponseEntity.ok(convert(updatedProduct));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") Long id) {
        productService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    static ProductDto convert(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .price(product.getPrice())
                .amount(product.getAmount())
                .build();
    }

    static Product convert(ProductDto dto) {
        return Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .amount(dto.getAmount())
                .build();
    }

}
