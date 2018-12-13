package org.hcs.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile({"DEV", "default"})
@EnableScheduling
public class SasHikariUpdaterTrigger {
  private static final Logger LOG = LoggerFactory.getLogger(SasHikariUpdaterTrigger.class);
  private final HikariDataSource hikariDataSource;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  public SasHikariUpdaterTrigger(HikariDataSource hikariDataSource) {
    this.hikariDataSource = hikariDataSource;
  }

  private void update(String username, String password) {
    hikariDataSource.setPassword(password);
    hikariDataSource.setUsername(username);
    hikariDataSource.getHikariPoolMXBean().softEvictConnections();
    LOG.info("hikari credentials updated");
  }

  @Scheduled(fixedDelay = 10 * 60 * 1000) //10 minutes
  public void update() {
    if(!this.hikariDataSource.getUsername().equals(username) || !hikariDataSource.getPassword().equals(password)) {
      update(username, password);
    }
    LOG.info("update triggered");
  }
}
