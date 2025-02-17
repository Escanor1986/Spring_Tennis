package com.escanor1986.tennis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.escanor1986.tennis.repository.HealthCheckRepository;
import com.escanor1986.tennis.service.HealthCheckService;
import com.escanor1986.tennis.web.HealthCheckController;

@SpringBootTest
class TennisApplicationTests {

  @Autowired
  private HealthCheckController healthCheckController;
  @Autowired
  private HealthCheckService healthCheckService;
  @Autowired
  private HealthCheckRepository healthCheckRepository;

  @Test
  void contextLoads() {
    Assertions.assertThat(healthCheckController).isNotNull();
    Assertions.assertThat(healthCheckService).isNotNull();
    Assertions.assertThat(healthCheckRepository).isNotNull();
  }

}
