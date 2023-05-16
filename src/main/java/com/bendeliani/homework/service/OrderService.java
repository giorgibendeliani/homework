package com.bendeliani.homework.service;

import com.bendeliani.homework.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAll();
    Optional<Order> findById(Long id);
    Order create(Order order);
    Order update(Order order);
    void delete(Long id);
}
