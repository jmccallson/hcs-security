package org.hcs.ws.v1;

import org.hcs.ws.v1.persistence.DataSourceSetup;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;

public class TestControllers {
  JdbcTemplate jdbc = DataSourceSetup.getJdbcTemplate();
  Timestamp timestamp = new Timestamp(System.currentTimeMillis());

}
