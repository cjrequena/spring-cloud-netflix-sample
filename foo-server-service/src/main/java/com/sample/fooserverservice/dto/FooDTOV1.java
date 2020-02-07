package com.sample.fooserverservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sample.fooserverservice.dto.serializer.LocalDateDeserializer;
import com.sample.fooserverservice.dto.serializer.LocalDateSerializer;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

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
  "id",
  "name",
  "description",
  "creationDate"
})
@JsonTypeName("foo")
@XmlRootElement
public class FooDTOV1 extends DTO implements Serializable {

  //@NotNull(message = "id is required field")
  @JsonProperty(value = "id")
  @Getter(onMethod = @__({@JsonProperty("id")}))
  private Long id;

  @NotNull(message = "name is a required field")
  @JsonProperty(value = "name", required = true)
  @Getter(onMethod = @__({@JsonProperty("name")}))
  private String name;

  //@NotNull(message = "description is a required field")
  @JsonProperty(value = "description")
  @Getter(onMethod = @__({@JsonProperty("description")}))
  private String description;

  //@NotNull(message = "Creation Date is a required field")
  @JsonProperty(value = "creation_date")
  @Getter(onMethod = @__({@JsonProperty("creation_date")}))
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate creationDate;

  @JsonProperty(value = "booes")
  @Getter(onMethod = @__({@JsonProperty("booes")}))
  private List<BooDTOV1> booes;
}
