package com.sample.fooclientservice.ws;

import org.springframework.http.MediaType;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Map;

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
public class ApplicationMediaType extends MediaType implements Serializable {

  public static final String FOO_API_V1_APPLICATION_XML_VALUE = "application/vnd.foo-api.v1+xml";
  public static final MediaType FOO_API_V1_APPLICATION_XML = valueOf(FOO_API_V1_APPLICATION_XML_VALUE);

  public static final String FOO_API_V1_APPLICATION_JSON_VALUE = "application/vnd.foo-api.v1+json";
  public static final MediaType FOO_API_V1_APPLICATION_JSON = valueOf(FOO_API_V1_APPLICATION_JSON_VALUE);

  public static final String FOO_API_MOCK_V1_APPLICATION_XML_VALUE = "application/vnd.foo-api.mock.v1+xml";
  public static final MediaType FOO_API_MOCK_V1_APPLICATION_XML = valueOf(FOO_API_MOCK_V1_APPLICATION_XML_VALUE);

  public static final String FOO_API_MOCK_V1_APPLICATION_JSON_VALUE = "application/vnd.foo-api.mock.v1+json";
  public static final MediaType FOO_API_MOCK_V1_APPLICATION_JSON = valueOf(FOO_API_MOCK_V1_APPLICATION_JSON_VALUE);

  public ApplicationMediaType(String type) {
    super(type);
  }

  public ApplicationMediaType(String type, String subtype) {
    super(type, subtype);
  }

  public ApplicationMediaType(String type, String subtype, Charset charset) {
    super(type, subtype, charset);
  }

  public ApplicationMediaType(String type, String subtype, double qualityValue) {
    super(type, subtype, qualityValue);
  }

  public ApplicationMediaType(MediaType other, Charset charset) {
    super(other, charset);
  }

  public ApplicationMediaType(MediaType other, Map<String, String> parameters) {
    super(other, parameters);
  }

  public ApplicationMediaType(String type, String subtype, Map<String, String> parameters) {
    super(type, subtype, parameters);
  }
}
