package com.bendeliani.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class OrderProductDto {
    @NotNull
    private Long productId;

    @NotNull
    private int quantity;
}
