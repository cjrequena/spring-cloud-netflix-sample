package com.sample.fooserverservice.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 *
 * @author cjrequena
 * @since JDK1.8
 */

@ControllerAdvice
@Log4j2
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

  /**
   *
   * @param ex
   * @return
   */
  @ExceptionHandler({ServiceException.class})
  @ResponseBody
  public ResponseEntity<Object> serviceException(ServiceException ex) {
    ResponseEntity responseEntity;
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setErrorCode(ex.getErrorCode());
    errorDTO.setMessage(ex.getMessage());
    errorDTO.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));

    if (ex.getErrorCode().equals(EErrorCode.CONFLICT_ERROR.getErrorCode())) {
      errorDTO.setStatus(HttpStatus.CONFLICT.value());
      responseEntity = new ResponseEntity(errorDTO, HttpStatus.CONFLICT);
    } else if (ex.getErrorCode().equals(EErrorCode.NOT_FOUND_ERROR.getErrorCode())) {
      errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
      responseEntity = new ResponseEntity(errorDTO, HttpStatus.NOT_FOUND);
    } else if (ex.getErrorCode().equals(EErrorCode.BAD_REQUEST_ERROR.getErrorCode())) {
      errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
      responseEntity = new ResponseEntity(errorDTO, HttpStatus.BAD_REQUEST);
    } else if (ex.getErrorCode().equals(EErrorCode.NOT_MODIFIED_ERROR.getErrorCode())) {
      errorDTO.setStatus(HttpStatus.NOT_MODIFIED.value());
      responseEntity = new ResponseEntity(errorDTO, HttpStatus.NOT_MODIFIED);
    } else {
      //errorDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      //responseEntity = new ResponseEntity(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
      responseEntity = new ResponseEntity(errorDTO, HttpStatus.valueOf(errorDTO.getStatus()));
    }

    log.error("Exception in service: " + errorDTO.toString());
    return responseEntity;
  }

  /**
   *
   * @param ex
   * @throws IOException
   */
  @ExceptionHandler(ControllerException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> bindingException(ControllerException ex) {
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setErrorCode(ex.getErrorCode());
    errorDTO.setMessage(ex.getMessage());
    errorDTO.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));

    errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
    return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
  }

}
