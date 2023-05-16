package com.bendeliani.homework.dto;

import com.bendeliani.homework.model.OrderStatus;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class OrderDto {
  private String client;
  @NotNull
  private List<OrderProductDto> orderProducts;
  private OrderStatus status;
}
