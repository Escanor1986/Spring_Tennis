package com.escanor1986.tennis.web;

import com.escanor1986.tennis.service.PlayerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Gestionnaire global des exceptions pour l'API
 * Le @RestControllerAdvice capture les exceptions lancées par les contrôleurs
 * et permet de formater la réponse d'erreur de manière cohérente
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Gère les exceptions PlayerNotFoundException
     * 
     * PROBLÈME RÉSOLU : L'annotation @ResponseStatus seule ne formatait pas le corps de la réponse
     * avec la structure attendue par les tests. Ce gestionnaire transforme l'exception
     * en une réponse JSON structurée avec le champ "errorDetails" attendu dans les tests.
     *
     * @param ex L'exception capturée
     * @return Une réponse HTTP 404 avec un corps JSON structuré
     */
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePlayerNotFoundException(PlayerNotFoundException ex) {
        // Création d'une structure d'erreur avec le message de l'exception
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        // Retourne une réponse HTTP 404 avec le corps JSON structuré
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
