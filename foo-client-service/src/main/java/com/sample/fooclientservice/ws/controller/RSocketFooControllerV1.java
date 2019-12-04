package com.sample.fooclientservice.ws.controller;

import com.sample.fooclientservice.dto.FooDTOV1;
import com.sample.fooclientservice.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.sample.fooclientservice.ws.controller.FooControllerV1.ACCEPT_VERSION_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

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
@RequestMapping(value = "/foo-client-service/rsocket")
@Api(
  value = "Foo Entity",
  tags = {"Foo Entity"}
)
public class RSocketFooControllerV1 {

  private final RSocketRequester rSocketRequester;

  public RSocketFooControllerV1(RSocketRequester rSocketRequester) {
    this.rSocketRequester = rSocketRequester;
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
  public Mono<FooDTOV1> retrieveById(@ApiParam(value = "id", required = true) @PathVariable(value = "id") Long id) {
    //--
    return rSocketRequester
      .route("Fooes.retrieveById")
      .data(id)
      .retrieveMono(FooDTOV1.class);
    //---
  }

  /**
   *
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
    produces = {TEXT_EVENT_STREAM_VALUE},
    headers = {ACCEPT_VERSION_VALUE}
  )
  public Flux<FooDTOV1> retrieve(
    @ApiParam(value = "filters") @RequestParam(value = "filters", required = false) String filters,
    @ApiParam(value = "sort") @RequestParam(value = "sort", required = false) String sort,
    @ApiParam(value = "offset") @RequestParam(value = "offset", required = false) Integer offset,
    @ApiParam(value = "limit") @RequestParam(value = "limit", required = false) Integer limit) {
    //--
    return rSocketRequester
      .route("Fooes.retrieve")
      //.data()
      .retrieveFlux(FooDTOV1.class);
    //---
  }
}
