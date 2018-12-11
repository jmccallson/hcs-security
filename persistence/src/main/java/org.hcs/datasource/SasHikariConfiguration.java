package org.hcs.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.io.IOException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@Profile({"DEV", "default"})
public class SasHikariConfiguration {
  @Value("${spring.datasource.url}")
  private String jdbcUrl;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.hikari")
  public HikariConfig buildHikariConfig () {
    return new HikariConfig();
  }

  @Bean
  public HikariDataSource dataSource(HikariConfig hikariConfig) throws IOException, SQLException {

    Driver driver = DriverManager.getDriver(jdbcUrl);
    //NO PASSWORD, this forces hikari to call getConnection(username,password) instead of getConnection()
    SimpleDriverDataSource rawDataSource = new SimpleDriverDataSource(driver, jdbcUrl);

    hikariConfig.setDataSource(rawDataSource);
    hikariConfig.setUsername(username);
    hikariConfig.setPassword(password);
    return new HikariDataSource(hikariConfig);
  }
}
