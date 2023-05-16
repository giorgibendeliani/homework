package com.bendeliani.homework.controller;

import com.bendeliani.homework.dto.OrderDto;
import com.bendeliani.homework.dto.OrderProductDto;
import com.bendeliani.homework.exception.ProductNotFoundException;
import com.bendeliani.homework.exception.ProductOutOfStockException;
import com.bendeliani.homework.model.Order;
import com.bendeliani.homework.model.OrderProduct;
import com.bendeliani.homework.model.OrderStatus;
import com.bendeliani.homework.model.Product;
import com.bendeliani.homework.service.OrderProductService;
import com.bendeliani.homework.service.OrderService;
import com.bendeliani.homework.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@AllArgsConstructor
@RestController
@RequestMapping("api/v1")
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final OrderProductService orderProductService;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> products = orderService.getAll()
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long id) {
        Optional<Order> order = orderService.findById(id);

        return order.map(value -> ResponseEntity.ok().body(convert(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    @PostMapping("/orders")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        validateOrder(orderDto);

        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        order.setClient(userDetails.getUsername());

        order = this.orderService.create(order);

        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderProductDto dto : orderDto.getOrderProducts()) {

            Product product = productService.findById(dto.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found!"));

            OrderProduct orderProduct = new OrderProduct(order, product, dto.getQuantity());

            OrderProduct saved = orderProductService.create(orderProduct);

            orderProducts.add(saved);
        }

        order.setProducts(orderProducts);

        orderService.update(order);

        String uri = ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/orders/{id}")
                .buildAndExpand(order.getId())
                .toString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uri);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .body(convert(order));

    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") Long id, @Valid @RequestBody OrderDto dto) {
        Optional<Order> orderOptional = orderService.findById(id);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setClient(dto.getClient());
            order.setStatus(dto.getStatus());

            List<OrderProduct> products = dto.getOrderProducts()
                    .stream()
                    .map(d -> convertOrderProduct(d, order))
                    .collect(Collectors.toList());

            order.setProducts(products);
            Order updatedOrder = orderService.update(order);
            return ResponseEntity.ok(convert(updatedOrder));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("id") Long id) {
        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void validateOrder(OrderDto orderDto) {
        orderDto.getOrderProducts().forEach(orderProduct -> {

            Long productId = orderProduct.getProductId();

            Product product = productService.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(
                            "Product with id: " + productId + " was not found in the store!"
                    ));

            if (product.getAmount() < orderProduct.getQuantity()) {
                throw new ProductOutOfStockException(
                        "Not enough quantity of product with id: " + productId + " in the store!"
                );
            }
        });
    }

    private OrderDto convert(Order order) {
        List<OrderProductDto> orderProducts = order.getProducts()
                .stream()
                .map(this::convertOrderProduct)
                .collect(Collectors.toList());

        return OrderDto.builder()
                .client(order.getClient())
                .status(order.getStatus())
                .orderProducts(orderProducts)
                .build();
    }

    private OrderProductDto convertOrderProduct(OrderProduct orderProduct) {
        return new OrderProductDto(orderProduct.getProduct().getId(), orderProduct.getQuantity());
    }

    private OrderProduct convertOrderProduct(OrderProductDto dto, Order order) {
        Product product = productService.findById(dto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(
                "Product with id: " + dto.getProductId() + " was not found in the store!"
        ));

        return new OrderProduct(order, product, dto.getQuantity());
    }

}
