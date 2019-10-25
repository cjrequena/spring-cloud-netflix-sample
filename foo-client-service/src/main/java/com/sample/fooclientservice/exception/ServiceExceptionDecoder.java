package com.sample.fooclientservice.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
@Service
@Log4j2
@Data
public class ServiceExceptionDecoder implements ErrorDecoder {

  /**
   *
   */
  @Autowired(required = false)
  private JacksonDecoder jacksonDecoder = new JacksonDecoder();

  /**
   *
   */
  private ErrorDecoder delegate = new Default();

  /**
   * @param methodKey
   * @param response
   * @return
   */
  @Override
  public Exception decode(String methodKey, Response response) {
    try {
      ErrorDTO errorDTO = (ErrorDTO) jacksonDecoder.decode(response, ErrorDTO.class);
      if (errorDTO != null) {
        log.debug(errorDTO.getMessage());
        return new ServiceException(errorDTO, HttpStatus.valueOf(errorDTO.getStatus()));
      }

    } catch (IOException e) { //NOSONAR
      // Fail silently as a new exception will be thrown in super
    } catch (IllegalArgumentException ex) {
      log.error("Error instantiating the exception declared thrown for the interface ", ex);
    }
    return delegate.decode(methodKey, response);
  }

}
