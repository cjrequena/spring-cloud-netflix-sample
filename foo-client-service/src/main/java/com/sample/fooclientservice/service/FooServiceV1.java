package com.sample.fooclientservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sample.fooclientservice.dto.FooDTOV1;
import com.sample.fooclientservice.exception.EErrorCode;
import com.sample.fooclientservice.exception.ServiceException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Log4j2
@Service
public class FooServiceV1 {

  @Autowired
  FooServerServiceV1Feign fooServerServiceV1Feign;

  /**
   *
   * @param dto
   * @return
   * @throws ServiceException
   */
  @HystrixCommand(
    commandKey = "FooServiceV1.create",
    fallbackMethod = "createFallbackMethod"
  )
  public ResponseEntity<Void> create(FooDTOV1 dto) throws ServiceException {
    //--
    try {
      return fooServerServiceV1Feign.create(dto);
    } catch (ServiceException ex) {
      log.error("{}", ex.getMessage());
      throw ex;
    } catch (Exception ex) {
      log.error("{}", ex.getMessage());
      throw new ServiceException(EErrorCode.BAD_REQUEST_ERROR.getErrorCode(), ex);
    }
    //--
  }

  /**
   *
   * @param dto
   * @return
   * @throws ServiceException
   */
  public ResponseEntity<Void> createFallbackMethod(FooDTOV1 dto, Throwable ex) throws ServiceException {
    //--
    log.debug("createFallbackMethod");
    throw (ServiceException) ex;
    //--
  }

  /**
   *
   * @param fields
   * @param filters
   * @param sort
   * @param offset
   * @param limit
   * @return
   * @throws ServiceException
   */
  @HystrixCommand(
    commandKey = "FooServiceV1.retrieve",
    fallbackMethod = "retrieveFallbackMethod"
  )
  public ResponseEntity<List<FooDTOV1>> retrieve(String fields, String filters, String sort, Integer offset, Integer limit) throws ServiceException {
    //--
    try {
      return fooServerServiceV1Feign.retrieve(fields, filters, sort, offset, limit);
    } catch (ServiceException ex) {
      log.error("{}", ex.getMessage());
      throw ex;
    } catch (Exception ex) {
      log.error("{}", ex.getMessage());
      throw new ServiceException(EErrorCode.BAD_REQUEST_ERROR.getErrorCode(), ex);
    }
    //--
  }

  /**
   *
   * @param fields
   * @param filters
   * @param sort
   * @param offset
   * @param limit
   * @param ex
   * @return
   * @throws ServiceException
   */
  public ResponseEntity<List<FooDTOV1>> retrieveFallbackMethod(String fields, String filters, String sort, Integer offset, Integer limit, Throwable ex) throws ServiceException {
    //--
    log.debug("retrieveFallbackMethod");
    throw (ServiceException) ex;
    //--
  }

  /**
   *
   */
  @FeignClient(name = "foo-server-service", url = "http://localhost:9080/foo-server-service")
  public interface FooServerServiceV1Feign {

    @PostMapping(value = "/fooes",
      produces = {
        MediaType.APPLICATION_JSON_VALUE
      },
      headers = "Accept-Version=vnd.foo-service.v1"
    )
    ResponseEntity<Void> create(
      @RequestBody FooDTOV1 dto) throws ServiceException;

    @GetMapping(
      path = "/fooes",
      produces = {
        MediaType.APPLICATION_JSON_VALUE
      },
      headers = "Accept-Version=vnd.foo-service.v1"
    )
    ResponseEntity<List<FooDTOV1>> retrieve(
      @RequestParam(value = "fields") String fields,
      @RequestParam(value = "filters") String filters,
      @RequestParam(value = "sort") String sort,
      @RequestParam(value = "offset") Integer offset,
      @RequestParam(value = "limit") Integer limit) throws ServiceException;

  }
}
