package com.sample.fooclientservice.exception;

import lombok.Data;

/**
 *
 * @author amonterop
 *
 */
@Data
public class ControllerException extends BaseException {

  /**
   *
   */
  protected final String errorCode;

  /**
   *
   * @param errorCode
   * @param message
   */
  public ControllerException(String errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
}
