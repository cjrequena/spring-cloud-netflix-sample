package com.sample.fooserverservice.configuration;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.validation.annotation.Validated;

import javax.sql.DataSource;

/**
 * <p>
 * <p>
 *
 * @author cjrequena
 * @version 1.0
 * @see
 * @since JDK1.8
 */
@Log4j2
@Configuration
public class DataSourceConfiguration {

  private ConexionType conexionType;

  @Profile("!integration-test")
  @Primary
  @Bean(name = "dataSource", destroyMethod = "")
  @Validated
  @ConfigurationProperties(prefix = "spring.datasource.postgres")
  @ConditionalOnProperty(
    name = {"spring.datasource.postgres.type"},
    havingValue = "com.zaxxer.hikari.HikariDataSource",
    matchIfMissing = true
  )
  @ConditionalOnClass({HikariDataSource.class})
  public HikariDataSource dataSource() {
    conexionType = ConexionType.POSTGRES;
    return new HikariDataSource();
  }

  @Profile("integration-test")
  @Bean(name = "dataSource", destroyMethod = "")
  public DataSource dataSourceIT() {
    conexionType = ConexionType.HSQL;
    return new EmbeddedDatabaseBuilder()
      .setType(EmbeddedDatabaseType.HSQL)
      .build();
  }

  @Bean
  public Flyway flyway(@Qualifier("dataSource") DataSource dataSource) {
    FluentConfiguration configuration = Flyway.configure().dataSource(dataSource).baselineOnMigrate(true).baselineVersion("0");
    if (conexionType == ConexionType.POSTGRES) {
      configuration.locations("/db/postgres/");
    } else if (conexionType == ConexionType.HSQL) {
      configuration.locations("/db/hsql/");
    }
    Flyway flyway = new Flyway(configuration);
    flyway.migrate();
    return flyway;
  }

  private enum ConexionType {
    POSTGRES,
    HSQL
  }
}
