package com.bendeliani.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductOutOfStockException extends RuntimeException {

  public ProductOutOfStockException() {
    super();
  }

  public ProductOutOfStockException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ProductOutOfStockException(final String message) {
    super(message);
  }

}
