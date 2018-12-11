package org.hcs.utils;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HcsRestTemplateBuilder {
  private final RestTemplate restTemplate;

  HcsRestTemplateBuilder(RestTemplateBuilder restTemplateBuilder){
    this.restTemplate = restTemplateBuilder.rootUri("locate://sobew.org").build();
  }
}
