package com.sample.fooserverservice.ws.controller;


import com.sample.fooserverservice.exception.ControllerException;
import com.sample.fooserverservice.exception.EErrorCode;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 *
 * @author cjrequena
 * @version 1.0
 * @see
 * @since JDK1.8
 */
interface IBaseController {

  /**
   *
   * @param bindingResult
   * @throws ControllerException
   */
  default void validateRequestBody(BindingResult bindingResult) throws ControllerException {
    if (bindingResult != null && bindingResult.hasErrors()) {
      List<FieldError> errors = bindingResult.getFieldErrors();
      List<String> errorMessages = new ArrayList<>();
      for (FieldError error : errors) {
        errorMessages.add("@" + error.getField().toUpperCase() + " " + error.getDefaultMessage());
      }
      throw new ControllerException(EErrorCode.BAD_REQUEST_ERROR.getErrorCode(), errorMessages.toString());
    }
  }

}
