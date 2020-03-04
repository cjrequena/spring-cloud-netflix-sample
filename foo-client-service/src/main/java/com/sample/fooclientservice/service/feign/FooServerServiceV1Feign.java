package com.sample.fooclientservice.service.feign;

import com.sample.fooclientservice.dto.FooDTOV1;
import com.sample.fooclientservice.exception.ServiceException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.json.JsonMergePatch;
import javax.json.JsonPatch;
import java.util.List;

import static com.sample.fooclientservice.common.Constant.VND_FOO_SERVICE_V1;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 *
 * @author cjrequena
 * @version 1.0
 */
//@FeignClient(url = "http://localhost:9080/foo-server-service")
@FeignClient(name = "foo-server-service")
@RequestMapping(path = "/foo-server-service", headers = {"Accept-Version=" + VND_FOO_SERVICE_V1})
public interface FooServerServiceV1Feign {

  /**
   *
   * @param dto
   * @return
   * @throws ServiceException
   */
  @PostMapping(value = "/fooes",
    produces = {
      MediaType.APPLICATION_JSON_VALUE
    }
  )
  ResponseEntity<Void> create(@RequestBody FooDTOV1 dto) throws ServiceException;

  /**
   *
   * @param id
   * @return
   * @throws ServiceException
   */
  @GetMapping(
    path = "/fooes/{id}",
    produces = {
      MediaType.APPLICATION_JSON_VALUE
    }
  )
  ResponseEntity<FooDTOV1> retrieveById(@PathVariable(value = "id") Long id) throws ServiceException;

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
  @GetMapping(
    path = "/fooes",
    produces = {
      MediaType.APPLICATION_JSON_VALUE
    }
  )
  ResponseEntity<List<FooDTOV1>> retrieve(
    @RequestParam(value = "fields") String fields,
    @RequestParam(value = "filters") String filters,
    @RequestParam(value = "sort") String sort,
    @RequestParam(value = "offset") Integer offset,
    @RequestParam(value = "limit") Integer limit) throws ServiceException;

  /**
   *
   * @param id
   * @param dto
   * @return
   * @throws ServiceException
   */
  @PutMapping(
    path = "/fooes/{id}",
    produces = {
      MediaType.APPLICATION_JSON_VALUE
    }
  )
  ResponseEntity<Void> update(
    @PathVariable(value = "id") Long id,
    @RequestBody FooDTOV1 dto
  ) throws ServiceException;

  /**
   *
   * @param id
   * @param patchDocument
   * @return
   * @throws ServiceException
   */
  @PatchMapping(
    path = "/fooes/{id}",
    produces = {MediaType.APPLICATION_JSON_VALUE},
    consumes = {"application/json-patch+json"}
  )
  ResponseEntity<Void> patch(@PathVariable(value = "id") Long id, @RequestBody JsonPatch patchDocument) throws ServiceException;

  /**
   *
   * @param id
   * @param mergePatchDocument
   * @return
   * @throws ServiceException
   */
  @PatchMapping(
    path = "/fooes/{id}",
    produces = {MediaType.APPLICATION_JSON_VALUE},
    consumes = {"application/merge-patch+json"}
  )
  ResponseEntity<Void> patch(@PathVariable(value = "id") Long id, @RequestBody JsonMergePatch mergePatchDocument) throws ServiceException;

  /**
   *
   * @param id
   * @return
   * @throws ServiceException
   */
  @DeleteMapping(
    path = "/fooes/{id}",
    produces = {
      MediaType.APPLICATION_JSON_VALUE
    }
  )
  ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) throws ServiceException;
}
