package com.sample.fooserverservice.common.util;

import com.google.common.base.CaseFormat;

import java.util.function.Function;

/**
 * <p>
 * <p>
 * <p>
 * <p>
 *
 * @author cjrequena
 * @version 1.0
 * @see
 * @since JDK1.8
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

  public static final String CAMEL_CASE_PATTERN = "([a-z]+[A-Z]+\\w+)+";

  /**
   *
   */
  public static Function<String, String> toCamelCase = property -> {
    if (property != null && !property.matches(CAMEL_CASE_PATTERN)) {
      property = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, property); // This convert snake_case properties to camelCase.
    }
    return property;
  };

  /**
   *
   */
  public static Function<String, Boolean> isCamelCase = attribute -> attribute.matches(CAMEL_CASE_PATTERN);
}
