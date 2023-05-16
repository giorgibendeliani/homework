package com.bendeliani.homework.service;

import com.bendeliani.homework.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAll();
    Optional<Product> findById(Long id);
    Product create(Product product);
    Product update(Product product);
    void delete(Long id);
}
