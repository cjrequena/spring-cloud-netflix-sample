package com.sample.fooclientservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 * @author cjrequena
 * @version 1.0
 * @since JDK1.8
 * @see
 *
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Log4j2
public class BaseException extends Exception {

  /**
   * @param exception - The exception
   */
  public BaseException(Throwable exception) {
    super(exception);
  }

  /**
   * @param message - The error exceptionMessage
   */
  public BaseException(String message) {
    super(message);
  }

  /**
   *
   * @param message - The error exceptionMessage
   * @param tex - Throwable exception
   */
  public BaseException(String message, Throwable tex) {
    super(message, tex);
  }

}
