package com.bendeliani.homework.service.impl;

import com.bendeliani.homework.model.OrderProduct;
import com.bendeliani.homework.repository.OrderProductRepository;
import com.bendeliani.homework.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderProductServiceImpl implements OrderProductService {

    private final OrderProductRepository orderProductRepository;

    @Override
    public OrderProduct create(OrderProduct orderProduct) {
        return orderProductRepository.save(orderProduct);
    }
}
