package com.sample.fooclientservice.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sample.fooclientservice.dto.DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.XmlRootElement;

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
@Data
@JsonPropertyOrder(value = {
  "date",
  "status",
  "errorCode",
  "message",
  "more_info"
})
@JsonTypeName("error")
@ApiModel(value = "Error", description = "Error DTO")
@XmlRootElement
public class ErrorDTO extends DTO {

  @JsonProperty(value = "date")
  @Getter(onMethod = @__({@JsonProperty("date")}))
  @ApiModelProperty(value = "date", position = 1)
  private String date;

  @JsonProperty(value = "status")
  @Getter(onMethod = @__({@JsonProperty("status")}))
  @ApiModelProperty(value = "status", position = 2)
  private int status;

  @JsonProperty(value = "error_code")
  @Getter(onMethod = @__({@JsonProperty("error_code")}))
  @ApiModelProperty(value = "error_code", position = 3)
  private String errorCode;

  @JsonProperty(value = "message")
  @Getter(onMethod = @__({@JsonProperty("message")}))
  @ApiModelProperty(value = "message", position = 4)
  private String message;

  @JsonProperty(value = "more_info")
  @Getter(onMethod = @__({@JsonProperty("more_info")}))
  @ApiModelProperty(value = "more_info", position = 6)
  private String moreInfo;

}
