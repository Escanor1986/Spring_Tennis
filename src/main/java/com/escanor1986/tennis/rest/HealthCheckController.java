package com.escanor1986.tennis.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/* 
 * This is a controller class that handles the health check request.
 * The @RestController annotation tells Spring that this class is a controller that handles web requests.
 * The controller class has a single method that handles the health check request.
 * The method returns a HealthCheck object that contains the health check status and message.
 * The method is annotated with @GetMapping to map the /healthcheck endpoint to the method.
 */
@RestController
public class HealthCheckController {

    @GetMapping("/healthcheck")
    public HealthCheck healthcheck() {
        return new HealthCheck(ApplicationStatus.OK, "Welcome to Dyma Tennis!");
    }
}
