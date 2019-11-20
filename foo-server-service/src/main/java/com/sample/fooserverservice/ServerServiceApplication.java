package com.sample.fooserverservice;

import eu.jaspe.jaspe.annotation.EnableJaspe;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
@EnableAutoConfiguration
@EnableJaspe
public class ServerServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(ServerServiceApplication.class, args);
  }
}
