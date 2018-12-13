package org.hcs.ws.v1.utils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping(value = "/healthcheck")
public class HealthCheck {
  private static final String HEALTHY_STATUS_MSG = "Healthy";

  @RequestMapping(value = "/heartbeat", method = {RequestMethod.GET, RequestMethod.HEAD},
                  produces = {TEXT_PLAIN_VALUE, TEXT_HTML_VALUE, APPLICATION_JSON_VALUE})
  @ResponseBody
  public String heartBeat(){
    return HEALTHY_STATUS_MSG;
  }
}
