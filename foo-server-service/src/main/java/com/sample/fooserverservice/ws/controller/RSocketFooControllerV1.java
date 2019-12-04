package com.sample.fooserverservice.ws.controller;

import com.sample.fooserverservice.dto.FooDTOV1;
import com.sample.fooserverservice.exception.ServiceException;
import com.sample.fooserverservice.service.FooServiceV1;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 *
 * @author cjrequena
 * @version 1.0
 */
@Log4j2
@Controller
public class RSocketFooControllerV1 {

  private FooServiceV1 fooServiceV1;

  public RSocketFooControllerV1(FooServiceV1 fooServiceV1) {
    this.fooServiceV1 = fooServiceV1;
  }

  @MessageMapping("Fooes.retrieveById")
  public Mono<FooDTOV1> retrieveById(Long id) throws ServiceException {
    //--
    try {
      return Mono.just(this.fooServiceV1.retrieveById(id));
    } catch (ServiceException ex) {
      //log.error("{}: {}", ex.getErrorCode(), ex.getMessage(), ex);
      throw ex;
    }
    //---
  }

  @MessageMapping("Fooes.retrieve")
  public Flux<FooDTOV1> retrieve() throws ServiceException {
    //--
    try {
      //return Flux.fromIterable(this.fooServiceV1.retrieve(filters, sort, offset, limit).getContent());
      return Flux.fromIterable(this.fooServiceV1.retrieve(null, null, null, null).getContent());
    } catch (ServiceException ex) {
      //log.error("{}: {}", ex.getErrorCode(), ex.getMessage(), ex);
      throw ex;
    }
    //---
  }

}
