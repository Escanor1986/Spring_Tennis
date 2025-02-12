package com.escanor1986.tennis.service;

import org.springframework.stereotype.Service;
import com.escanor1986.tennis.ApplicationStatus;
import com.escanor1986.tennis.HealthCheck;
import com.escanor1986.tennis.repository.HealthCheckRepository;

@Service
public class HealthCheckService {

  private final HealthCheckRepository healthCheckRepository;

  // ✅ Injection du repository via le constructeur
  public HealthCheckService(HealthCheckRepository healthCheckRepository) {
    this.healthCheckRepository = healthCheckRepository;
  }

  public HealthCheck healthcheck() {
    Long activeSessions = healthCheckRepository.countApplicationConnections(); // ✅ Appel correct

    if (activeSessions > 0) {
      return new HealthCheck(ApplicationStatus.OK, "Welcome to escanor1986 Tennis! Active PostgreSQL Sessions: " + activeSessions);
    } else {
      return new HealthCheck(ApplicationStatus.KO, "Database Connection failed!");
    }
  }
}
