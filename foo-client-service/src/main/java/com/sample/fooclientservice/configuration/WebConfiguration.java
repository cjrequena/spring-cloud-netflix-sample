package com.sample.fooclientservice.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;
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
@Log4j2
public class WebConfiguration implements WebMvcConfigurer {

  /**
   *
   * @return
   */
  @Bean
  public Filter shallowEtagHeaderFilter() {
    return new ShallowEtagHeaderFilter();
  }

  @Bean
  RSocketRequester rSocketRequester(RSocketStrategies rSocketStrategies) {
    return RSocketRequester.builder()
      .rsocketStrategies(rSocketStrategies)
      .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
      //.metadataMimeType(MimeTypeUtils.APPLICATION_JSON)
      .connectTcp("localhost", 7000)
      .block();
  }

}
