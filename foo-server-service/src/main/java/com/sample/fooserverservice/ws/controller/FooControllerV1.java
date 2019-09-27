package com.sample.fooserverservice.ws.controller;


import com.sample.fooserverservice.dto.FooDTOV1;
import com.sample.fooserverservice.exception.ControllerException;
import com.sample.fooserverservice.exception.EErrorCode;
import com.sample.fooserverservice.exception.ServiceException;
import com.sample.fooserverservice.service.FooServiceV1;
import com.sample.fooserverservice.ws.ApplicationMediaType;
import cz.jirutka.rsql.parser.RSQLParserException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
@RestController
@RequestMapping(value = "/foo-service")
@Api(
  value = "Foo Entity",
  tags = {"Foo Entity"}
)
public class FooControllerV1 implements IBaseController {

  public static final String CACHE_CONTROL = "Cache-Control";

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
    path = "/foos",
    produces = {
      ApplicationMediaType.FOO_API_V1_APPLICATION_JSON_VALUE
    }
  )
  public ResponseEntity<FooDTOV1> create(
    @ApiParam(value = "foo", name = "foo", required = true) @Valid @RequestBody FooDTOV1 dto, BindingResult bindingResult,
    HttpServletRequest request, UriComponentsBuilder ucBuilder) throws ServiceException {
    //--
    try {
      // Validate request body dto
      validateRequestBody(bindingResult);
      //
      dto = fooServiceV1.create(dto);
      // Headers
      HttpHeaders headers = new HttpHeaders();
      headers.set(CACHE_CONTROL, "no store, private, max-age=0");
      headers.setLocation(ucBuilder.path(new StringBuilder().append(request.getServletPath()).append("/{id}").toString())
              .buildAndExpand(dto.getId())
              .toUri());
      //
      return new ResponseEntity<>(headers, HttpStatus.CREATED);
    } catch (ServiceException ex) {
      log.error("{}", ex.getMessage(), ex);
      throw ex;
    } catch (ControllerException ex) { //NOSONAR
      throw new ServiceException(ex.getErrorCode(), ex.getMessage());
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
    path = "/foos/{id}",
    produces = {
      ApplicationMediaType.FOO_API_V1_APPLICATION_JSON_VALUE
    }
  )
  public ResponseEntity<FooDTOV1> retrieve(
    @ApiParam(value = "id", required = true) @PathVariable(value = "id") Long id) throws ServiceException {
    //--
    try {
      //Headers
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
      FooDTOV1 dtoRS = this.fooServiceV1.retrieve(id);
      return new ResponseEntity<>(dtoRS, responseHeaders, HttpStatus.OK);
    } catch (ServiceException ex) {
      log.error("{}: {}", ex.getErrorCode(), ex.getMessage(), ex);
      throw ex;
    }
    //---
  }

  /**
   * Get a list of foos.
   *
   * @param offset {@link Integer}
   * @param limit {@link Integer}
   * @return ResponseEntity
   * @throws ServiceException
   */
  @ApiOperation(
    tags = "Foo Entity",
    value = "Get a list of foos.",
    notes = "Get a list of foos.",
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
    path = "/foos",
    produces = {
      ApplicationMediaType.FOO_API_V1_APPLICATION_JSON_VALUE
    }
  )
  public ResponseEntity<List<FooDTOV1>> retrieve(
    @ApiParam(value = "fields") @RequestParam(value = "fields", required = false) String fieldsQueryParam,
    @ApiParam(value = "filters") @RequestParam(value = "filters", required = false) String searchQueryParam,
    @ApiParam(value = "sort") @RequestParam(value = "sort", required = false) String sortQueryParam,
    @ApiParam(value = "offset") @RequestParam(value = "offset", required = false) Integer offset,
    @ApiParam(value = "limit") @RequestParam(value = "limit", required = false) Integer limit
  ) throws ServiceException {
    //--
    try {

      log.debug("fieldsQueryParam: {} ", fieldsQueryParam);
      log.debug("searchQueryParam: {} ", searchQueryParam);
      log.debug("sortQueryParam: {} ", sortQueryParam);
      log.debug("offset: {} ", offset);
      log.debug("limit: {} ", limit);

      Page page = this.fooServiceV1.retrieve(searchQueryParam, sortQueryParam, offset, limit);

      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
      responseHeaders.set("Total-Count", String.valueOf(page.getTotalElements())); // Total elements in DB.
      responseHeaders.set("Total-Pages", String.valueOf(page.getTotalPages())); // Total pages based on limit/offset
      responseHeaders.set("Number-Of-Elements", String.valueOf(page.getNumberOfElements())); // Total elements in the payload.
      return new ResponseEntity<>(page.getContent(), responseHeaders, HttpStatus.OK);
    } catch (RSQLParserException ex) {
      log.error("{}", ex.getMessage(), ex);
      throw new ServiceException(EErrorCode.BAD_REQUEST_ERROR.getErrorCode(), ex);
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
    path = "/foos/{id}",
    produces = {
      ApplicationMediaType.FOO_API_V1_APPLICATION_JSON_VALUE
    }
  )
  public ResponseEntity<Void> update(
    @ApiParam(value = "id", required = true) @PathVariable(value = "id") Long id,
    @ApiParam(value = "foo", name = "foo", required = true) @Valid @RequestBody FooDTOV1 dto,
    BindingResult bindingResult) throws ServiceException {
    //--
    try {
      // Validate request body dto
      validateRequestBody(bindingResult);
      //
      this.fooServiceV1.update(id, dto);
      //Headers
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.set(CACHE_CONTROL, "no store, private, max-age=0");
      return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
    } catch (ServiceException ex) {
      log.error("{}", ex.getMessage(), ex);
      throw ex;
    } catch (ControllerException ex) {//NOSONAR
      throw new ServiceException(ex.getErrorCode(), ex.getMessage());
    }
    //---
  }

  /**
   * Patch a foo by id.
   *
   * @param id {@link Integer}
   * @param dto {@link FooDTOV1}
   * @param bindingResult {@link BindingResult}
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
    path = "/foos/{id}",
    produces = {
      ApplicationMediaType.FOO_API_V1_APPLICATION_JSON_VALUE
    }
  )
  public ResponseEntity<Void> patch(
    @ApiParam(value = "id", required = true) @PathVariable(value = "id") Long id,
    @ApiParam(value = "foo", name = "foo", required = true) @Valid @RequestBody FooDTOV1 dto,
    BindingResult bindingResult,
    UriComponentsBuilder ucBuilder) throws ServiceException {
    //--
    try {
      // Validate request body dto
      validateRequestBody(bindingResult);
      //
      this.fooServiceV1.patch(id, dto);
      //Headers
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.setLocation(ucBuilder.path("/foos/{id}").buildAndExpand(id).toUri());
      return new ResponseEntity<>(responseHeaders, HttpStatus.NO_CONTENT);
    } catch (ServiceException ex) {
      log.error("Error patching: {}", ex.getMessage(), ex);
      throw ex;
    } catch (ControllerException ex) {//NOSONAR
      throw new ServiceException(ex.getErrorCode(), ex.getMessage());
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
    path = "/foos/{id}",
    produces = {
      ApplicationMediaType.FOO_API_V1_APPLICATION_JSON_VALUE
    }
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
