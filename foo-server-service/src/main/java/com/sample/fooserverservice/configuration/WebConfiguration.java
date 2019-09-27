package com.sample.fooserverservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

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
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  /**
   *
   * @return
   */
  @Bean
  public Filter shallowEtagHeaderFilter() {
    return new ShallowEtagHeaderFilter();
  }

}
