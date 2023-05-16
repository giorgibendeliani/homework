package com.bendeliani.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDto {

  @NotBlank(message = "Email is mandatory")
  private String email;

  @NotBlank(message = "Password is mandatory")
  private String password;

}
