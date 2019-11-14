package com.sample.fooclientservice.ws.controller;

import com.sample.fooclientservice.dto.FooDTOV1;
import com.sample.fooclientservice.exception.EErrorCode;
import com.sample.fooclientservice.exception.ServiceException;
import com.sample.fooclientservice.service.FooServiceV1;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.json.JsonMergePatch;
import javax.json.JsonPatch;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
@RestController
@RequestMapping(value = "/foo-client-service")
@Api(
  value = "Foo Entity",
  tags = {"Foo Entity"}
)
public class FooControllerV1 {

  public static final String CACHE_CONTROL = "Cache-Control";
  public static final String ACCEPT_VERSION_VALUE = "Accept-Version=vnd.foo-service.v1";
  public static final String APPLICATION_JSON_PATCH_VALUE = "application/json-patch+json";
  public static final String APPLICATION_JSON_MERGE_PATCH_VALUE = "application/merge-patch+json";

  @Autowired
  private FooServiceV1 fooServiceV1;

  /**
   * Create a new foo.
   *
   * @param dto {@link FooDTOV1}
   * @param bindingResult {@link BindingResult}
   * @return ResponseEntity {@link ResponseEntity}
   * @throws ServiceException {@link ServiceException}
   */
  @ApiOperation(
    tags = "Foo Entity",
    value = "Create a new foo.",
    notes = "Create a new foo."
  )
  @ApiResponses(
    value = {
      @ApiResponse(code = 201, message = "Created - The request was successful, we created a new resource and the response body contains the representation."),
      @ApiResponse(code = 204, message = "No Content - The request was successful, we created a new resource and the response body does not contains the representation."),
      @ApiResponse(code = 400, message = "Bad Request - The data given in the POST failed validation. Inspect the response body for details."),
      @ApiResponse(code = 401, message = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
      @ApiResponse(code = 408, message = "Request Timeout"),
      @ApiResponse(code = 409, message = "Conflict - The request could not be processed because of conflict in the request"),
      @ApiResponse(code = 429, message = "Too Many Requests - Your application is sending too many simultaneous requests."),
      @ApiResponse(code = 500, message = "Internal Server Error - We couldn't create the resource. Please try again."),
      @ApiResponse(code = 503, message = "Service Unavailable - We are temporarily unable. Please wait for a bit and try again. ")
    }
  )
  @PostMapping(
    path = "/fooes",
    produces = {APPLICATION_JSON_VALUE},
    headers = {ACCEPT_VERSION_VALUE}
  )
  public ResponseEntity<Void> create(
    @ApiParam(value = "foo", name = "foo", required = true) @Valid @RequestBody FooDTOV1 dto, BindingResult bindingResult,
    HttpServletRequest request, UriComponentsBuilder ucBuilder) throws ServiceException {
    //--
    try {
      return fooServiceV1.create(dto);
    } catch (ServiceException ex) {
      log.error("{}", ex.getMessage(), ex);
      throw ex;
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
      throw new ServiceException(EErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), ex);
    }
    //---
  }

    /**
     * Get a foo by id.
     *
     * @param id {@link Integer}
     * @return ResponseEntity {@link ResponseEntity}
     */
    @ApiOperation(
      tags = "Foo Entity",
      value = "Get a foo by id.",
      notes = "Get a foo by id."
    )
    @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK - The request was successful and the response body contains the representation requested."),
        @ApiResponse(code = 400, message = "Bad Request - The data given in the GET failed validation. Inspect the response body for details."),
        @ApiResponse(code = 401, message = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 408, message = "Request Timeout"),
        @ApiResponse(code = 429, message = "Too Many Requests - Your application is sending too many simultaneous requests."),
        @ApiResponse(code = 500, message = "Internal Server Error - We couldn't return the representation due to an internal server error."),
        @ApiResponse(code = 503, message = "Service Unavailable - We are temporarily unable to return the representation. Please wait for a bit and try again."),
      }
    )
    @GetMapping(
      path = "/fooes/{id}",
      produces = {APPLICATION_JSON_VALUE},
      headers = {ACCEPT_VERSION_VALUE}
    )
    public ResponseEntity<FooDTOV1> retrieveById(
      @ApiParam(value = "id", required = true) @PathVariable(value = "id") Long id) throws ServiceException {
      //--
      try {
        //Headers
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
        return this.fooServiceV1.retrieveById(id);
      } catch (ServiceException ex) {
        log.error("{}: {}", ex.getErrorCode(), ex.getMessage(), ex);
        throw ex;
      }
      //---
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
  @ApiOperation(
    tags = "Foo Entity",
    value = "Get a list of fooes.",
    notes = "Get a list of fooes.",
    responseContainer = "List"
  )
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK - The request was successful and the response body contains the representation requested."),
      @ApiResponse(code = 400, message = "Bad Request - The data given in the GET failed validation. Inspect the response body for details."),
      @ApiResponse(code = 401, message = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
      @ApiResponse(code = 404, message = "Not Found"),
      @ApiResponse(code = 408, message = "Request Timeout"),
      @ApiResponse(code = 429, message = "Too Many Requests - Your application is sending too many simultaneous requests."),
      @ApiResponse(code = 500, message = "Internal Server Error - We couldn't return the representation due to an internal server error."),
      @ApiResponse(code = 503, message = "Service Unavailable - We are temporarily unable to return the representation. Please wait for a bit and try again."),
    }
  )
  @GetMapping(
    path = "/fooes",
    produces = {APPLICATION_JSON_VALUE},
    headers = {ACCEPT_VERSION_VALUE}
  )
  public ResponseEntity<List<FooDTOV1>> retrieve(
    @ApiParam(value = "fields") @RequestParam(value = "fields", required = false) String fields,
    @ApiParam(value = "filters") @RequestParam(value = "filters", required = false) String filters,
    @ApiParam(value = "sort") @RequestParam(value = "sort", required = false) String sort,
    @ApiParam(value = "offset") @RequestParam(value = "offset", required = false) Integer offset,
    @ApiParam(value = "limit") @RequestParam(value = "limit", required = false) Integer limit
  ) throws ServiceException {
    //--
    try {

      log.debug("fields: {} ", fields);
      log.debug("filters: {} ", filters);
      log.debug("sort: {} ", sort);
      log.debug("offset: {} ", offset);
      log.debug("limit: {} ", limit);

      return this.fooServiceV1.retrieve(fields, filters, sort, offset, limit);

    } catch (ServiceException ex) {
      log.error("{}", ex.getMessage(), ex);
      throw ex;
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
      throw new ServiceException(EErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(), ex);
    }
    //--
  }

    /**
     * Update a foo by id.
     *
     * @param id {@link Integer}
     * @param dto {@link FooDTOV1}
     * @param bindingResult {@link BindingResult}
     * @return Void {@link Void}
     * @throws ServiceException {@link ServiceException}
     */
    @ApiOperation(
      tags = "Foo Entity",
      value = "Update a foo by id.",
      notes = "Update a foo by id."
    )
    @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "OK - The request was successful, we updated the resource and the response body contains the representation."),
        @ApiResponse(code = 204, message = "No Content - The request was successful, we created a new resource and the response body does not contains the representation."),
        @ApiResponse(code = 400, message = "Bad Request - The data given in the PUT failed validation. Inspect the response body for details."),
        @ApiResponse(code = 401, message = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
        @ApiResponse(code = 408, message = "Request Timeout"),
        @ApiResponse(code = 409, message = "Conflict - The request could not be processed because of conflict in the request"),
        @ApiResponse(code = 429, message = "Too Many Requests - Your application is sending too many simultaneous requests."),
        @ApiResponse(code = 500, message = "Internal Server Error - We couldn't create the resource. Please try again."),
        @ApiResponse(code = 503, message = "Service Unavailable - We are temporarily unable. Please wait for a bit and try again. ")
      }
    )
    @PutMapping(
      path = "/fooes/{id}",
      produces = {APPLICATION_JSON_VALUE},
      headers = {ACCEPT_VERSION_VALUE}
    )
    public ResponseEntity<Void> update(
      @ApiParam(value = "id", required = true) @PathVariable(value = "id") Long id,
      @ApiParam(value = "foo", name = "foo", required = true) @Valid @RequestBody FooDTOV1 dto,
      BindingResult bindingResult) throws ServiceException {
      //--
      try {
        //
        this.fooServiceV1.update(id, dto);
        //Headers
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
        return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
      } catch (ServiceException ex) {
        log.error("{}", ex.getMessage(), ex);
        throw ex;
      }
      //---
    }

  /**
   * Patch a foo by id.
   *
   * @param id {@link Integer}
   * @param patchDocument {@link JsonPatch}
   * @return Void {@link Void}
   * @throws ServiceException {@link ServiceException}
   */
  @ApiOperation(
    tags = "Foo Entity",
    value = "Patch a foo by id.",
    notes = "Patch a foo by id."
  )
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK - The request was successful, we updated the resource and the response body contains the representation."),
      @ApiResponse(code = 204, message = "No Content - The request was successful, we created a new resource and the response body does not contains the representation."),
      @ApiResponse(code = 400, message = "Bad Request - The data given in the PATCH failed validation. Inspect the response body for details."),
      @ApiResponse(code = 401, message = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
      @ApiResponse(code = 404, message = "Not Found"),
      @ApiResponse(code = 408, message = "Request Timeout"),
      @ApiResponse(code = 409, message = "Conflict - The request could not be processed because of conflict in the request"),
      @ApiResponse(code = 429, message = "Too Many Requests - Your application is sending too many simultaneous requests."),
      @ApiResponse(code = 500, message = "Internal Server Error - We couldn't create the resource. Please try again."),
      @ApiResponse(code = 503, message = "Service Unavailable - We are temporarily unable. Please wait for a bit and try again. ")
    }
  )
  @PatchMapping(
    path = "/fooes/{id}",
    produces = {APPLICATION_JSON_VALUE},
    consumes = {APPLICATION_JSON_PATCH_VALUE},
    headers = {ACCEPT_VERSION_VALUE}
  )
  public ResponseEntity<Void> patch(
    @ApiParam(value = "id", required = true) @PathVariable(value = "id") Long id,
    @ApiParam(value = "foo", name = "foo", required = true) @RequestBody JsonPatch patchDocument,
    UriComponentsBuilder ucBuilder) throws ServiceException {
    //--
    try {

      this.fooServiceV1.patch(id, patchDocument);
      //Headers
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.setLocation(ucBuilder.path("/fooes/{id}").buildAndExpand(id).toUri());
      return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
    } catch (ServiceException ex) {
      log.error("Error patching: {}", ex.getMessage());
      throw ex;
    }
    //---
  }

  /**
   * Patch a foo by id.
   *
   * @param id {@link Integer}
   * @param mergePatchDocument {@link JsonMergePatch}
   * @return Void {@link Void}
   * @throws ServiceException {@link ServiceException}
   */
  @ApiOperation(
    tags = "Foo Entity",
    value = "Patch a foo by id.",
    notes = "Patch a foo by id."
  )
  @ApiResponses(
    value = {
      @ApiResponse(code = 200, message = "OK - The request was successful, we updated the resource and the response body contains the representation."),
      @ApiResponse(code = 204, message = "No Content - The request was successful, we created a new resource and the response body does not contains the representation."),
      @ApiResponse(code = 400, message = "Bad Request - The data given in the PATCH failed validation. Inspect the response body for details."),
      @ApiResponse(code = 401, message = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
      @ApiResponse(code = 408, message = "Request Timeout"),
      @ApiResponse(code = 409, message = "Conflict - The request could not be processed because of conflict in the request"),
      @ApiResponse(code = 429, message = "Too Many Requests - Your application is sending too many simultaneous requests."),
      @ApiResponse(code = 500, message = "Internal Server Error - We couldn't create the resource. Please try again."),
      @ApiResponse(code = 503, message = "Service Unavailable - We are temporarily unable. Please wait for a bit and try again. ")
    }
  )
  @PatchMapping(
    path = "/fooes/{id}",
    produces = {APPLICATION_JSON_VALUE},
    consumes = {APPLICATION_JSON_MERGE_PATCH_VALUE},
    headers = {ACCEPT_VERSION_VALUE}
  )
  public ResponseEntity<Void> patch(
    @ApiParam(value = "id", required = true) @PathVariable(value = "id") Long id,
    @ApiParam(value = "foo", name = "foo", required = true) @RequestBody JsonMergePatch mergePatchDocument,
    UriComponentsBuilder ucBuilder) throws ServiceException {
    //--
    try {
      this.fooServiceV1.patch(id, mergePatchDocument);
      //Headers
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.setLocation(ucBuilder.path("/fooes/{id}").buildAndExpand(id).toUri());
      return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
    } catch (ServiceException ex) {
      log.error("Error patching: {}", ex.getMessage());
      throw ex;
    }
    //---
  }

    /**
     * Delete a foo by id.
     *
     * @param id {@link Integer}
     * @return ResponseEntity {@link ResponseEntity}
     */
    @ApiOperation(
      tags = "Foo Entity",
      value = "Delete a foo by id.",
      notes = "Delete a foo by id."
    )
    @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "OK - The request was successful; the resource was deleted."),
        @ApiResponse(code = 401, message = "Unauthorized - The supplied credentials, if any, are not sufficient to access the resource."),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 408, message = "Request Timeout"),
        @ApiResponse(code = 429, message = "Too Many Requests - Your application is sending too many simultaneous requests."),
        @ApiResponse(code = 500, message = "Internal Server Error - We couldn't delete the resource. Please try again."),
        @ApiResponse(code = 503, message = "Service Unavailable")
      }
    )
    @DeleteMapping(
      path = "/fooes/{id}",
      produces = {APPLICATION_JSON_VALUE},
      headers = {ACCEPT_VERSION_VALUE}
    )
    public ResponseEntity<Void> delete(@ApiParam(value = "id", required = true) @PathVariable(value = "id") Long id) throws ServiceException {
      //--
      try {
        //Headers
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");

        this.fooServiceV1.delete(id);

        return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);

      } catch (ServiceException ex) {
        log.error("{}", ex.getMessage(), ex);
        throw ex;
      }
      //---
    }

}
