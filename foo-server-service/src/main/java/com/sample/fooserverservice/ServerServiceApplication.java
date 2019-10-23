package com.sample.fooserverservice;

import eu.jaspe.jaspe.annotation.EnableJaspe;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Log4j2
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableAutoConfiguration
@EnableJaspe
public class ServerServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(ServerServiceApplication.class, args);
  }
}
