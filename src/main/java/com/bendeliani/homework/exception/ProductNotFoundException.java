package com.bendeliani.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

  public ProductNotFoundException() {
    super();
  }

  public ProductNotFoundException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ProductNotFoundException(final String message) {
    super(message);
  }

}
