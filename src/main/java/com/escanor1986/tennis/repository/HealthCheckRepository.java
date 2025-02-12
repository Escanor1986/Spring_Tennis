package com.escanor1986.tennis.repository;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

@Repository
public class HealthCheckRepository {
  
  private final EntityManager entityManager;

    HealthCheckRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

  public Long countApplicationConnections() {
    String applicationConnectionsQuery = "SELECT COUNT(*) FROM pg_stat_activity WHERE application_name = 'PostgreSQL JDBC Driver'";
    return (Long) entityManager.createNativeQuery(applicationConnectionsQuery).getSingleResult();
  }
}
