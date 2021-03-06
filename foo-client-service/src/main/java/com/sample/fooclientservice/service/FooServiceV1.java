package com.sample.fooclientservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sample.fooclientservice.dto.FooDTOV1;
import com.sample.fooclientservice.exception.EErrorCode;
import com.sample.fooclientservice.exception.ServiceException;
import com.sample.fooclientservice.service.feign.FooServerServiceV1Feign;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.json.JsonMergePatch;
import javax.json.JsonPatch;
import java.util.List;

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
@Service
public class FooServiceV1 {

  @Autowired
  private FooServerServiceV1Feign fooServerServiceV1Feign;

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
   * @param id
   * @return
   * @throws ServiceException
   */
  @HystrixCommand(
    commandKey = "FooServiceV1.retrieveById",
    fallbackMethod = "retrieveByIdFallbackMethod"
  )
  public ResponseEntity<FooDTOV1> retrieveById(Long id) throws ServiceException {
    //--
    try {
      return fooServerServiceV1Feign.retrieveById(id);
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
   * @param id
   * @param ex
   * @return
   * @throws ServiceException
   */
  public ResponseEntity<FooDTOV1> retrieveByIdFallbackMethod(Long id, Throwable ex) throws ServiceException {
    //--
    log.debug("retrieveByIdFallbackMethod");
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
   * @param id
   * @param dto
   * @return
   * @throws ServiceException
   */
  @HystrixCommand(
    commandKey = "FooServiceV1.update",
    fallbackMethod = "updateFallbackMethod"
  )
  public ResponseEntity<Void> update(Long id, FooDTOV1 dto) throws ServiceException {
    //--
    try {
      return fooServerServiceV1Feign.update(id, dto);
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
   * @param id
   * @param dto
   * @param ex
   * @return
   * @throws ServiceException
   */

  public ResponseEntity<Void> updateFallbackMethod(Long id, FooDTOV1 dto, Throwable ex) throws ServiceException {
    //--
    log.debug("updateFallbackMethod");
    throw (ServiceException) ex;
    //--
  }

  @HystrixCommand(
    commandKey = "FooServiceV1.path",
    fallbackMethod = "patchFallbackMethod"
  )
  public ResponseEntity<Void> patch(Long id, JsonPatch patchDocument) throws ServiceException {
    //--
    try {
      return fooServerServiceV1Feign.patch(id, patchDocument);
    } catch (ServiceException ex) {
      log.error("{}", ex.getMessage());
      throw ex;
    } catch (Exception ex) {
      log.error("{}", ex.getMessage());
      throw new ServiceException(EErrorCode.BAD_REQUEST_ERROR.getErrorCode(), ex);
    }
  }


  public ResponseEntity<Void> patchFallbackMethod(Long id, JsonPatch patchDocument, Throwable ex) throws ServiceException {
    //--
    log.debug("patchFallbackMethod");
    throw (ServiceException) ex;
    //--
  }

  @HystrixCommand(
    commandKey = "FooServiceV1.path",
    fallbackMethod = "patchFallbackMethod"
  )
  public ResponseEntity<Void> patch(Long id, JsonMergePatch mergePatchDocument) throws ServiceException {
    //--
    try {
      return fooServerServiceV1Feign.patch(id, mergePatchDocument);
    } catch (ServiceException ex) {
      log.error("{}", ex.getMessage());
      throw ex;
    } catch (Exception ex) {
      log.error("{}", ex.getMessage());
      throw new ServiceException(EErrorCode.BAD_REQUEST_ERROR.getErrorCode(), ex);
    }
  }

  public ResponseEntity<Void> patchFallbackMethod(Long id, JsonMergePatch mergePatchDocument, Throwable ex) throws ServiceException {
    //--
    log.debug("patchFallbackMethod");
    throw (ServiceException) ex;
    //--
  }

  /**
   *
   * @param id
   * @return
   * @throws ServiceException
   */
  @HystrixCommand(
    commandKey = "FooServiceV1.delete",
    fallbackMethod = "deleteFallbackMethod"
  )
  public ResponseEntity<Void> delete(Long id) throws ServiceException {
    //--
    try {
      return fooServerServiceV1Feign.delete(id);
    } catch (ServiceException ex) {
      log.error("{}", ex.getMessage());
      throw ex;
    } catch (Exception ex) {
      log.error("{}", ex.getMessage());
      throw new ServiceException(EErrorCode.BAD_REQUEST_ERROR.getErrorCode(), ex);
    }
    //--
  }

  public ResponseEntity<Void> deleteFallbackMethod(Long id, Throwable ex) throws ServiceException {
    //--
    log.debug("deleteFallbackMethod");
    throw (ServiceException) ex;
    //--
  }
}
