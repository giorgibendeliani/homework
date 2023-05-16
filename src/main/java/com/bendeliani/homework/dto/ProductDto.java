package com.bendeliani.homework.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ProductDto {

  @NotNull
  private String name;

  @NotNull
  private double price;

  @NotNull
  private int amount;
}
