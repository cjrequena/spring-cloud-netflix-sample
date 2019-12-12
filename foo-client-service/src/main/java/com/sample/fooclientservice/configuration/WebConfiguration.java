package com.sample.fooclientservice.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

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
  //  @Bean
  //  public Filter shallowEtagHeaderFilter() {
  //    return new ShallowEtagHeaderFilter();
  //  }

  @Bean
  public RSocketRequester rSocketRequester(RSocketStrategies rSocketStrategies) {
    return RSocketRequester.builder()
      .rsocketStrategies(rSocketStrategies)
      .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
      //.metadataMimeType(MimeTypeUtils.APPLICATION_JSON)
      .connectTcp("localhost", 7000)
      .block(Duration.ofSeconds(5));
  }

  @Bean
  public RSocketStrategies rSocketStrategies() {
    return RSocketStrategies.builder()
      .encoders(encoders -> encoders.add(new Jackson2JsonEncoder()))
      .decoders(decoders -> decoders.add(new Jackson2JsonDecoder()))
      .build();
  }

}
