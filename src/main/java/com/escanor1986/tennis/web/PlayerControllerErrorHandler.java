package com.escanor1986.tennis.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.escanor1986.tennis.service.PlayerAlreadyExistsException;
import com.escanor1986.tennis.service.PlayerDataRetrievalException;
import com.escanor1986.tennis.service.PlayerNotFoundException;

/**
 * Classe pour gérer les erreurs du contrôleur des joueurs
 * 
 * @param ex : exception à gérer
 * 
 * @return : retourne une erreur
 * @return : retourne une erreur de validation
 * @return : retourne une erreur si le joueur existe déjà
 */
@RestControllerAdvice
public class PlayerControllerErrorHandler {

    @ExceptionHandler(PlayerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handlePlayerNotFoundException(PlayerNotFoundException ex) {
        return new Error(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(PlayerAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handlePlayerAlreadyExistsException(PlayerAlreadyExistsException ex) {
        return new Error(ex.getMessage());
    }

    /**
     * Gère les exceptions de récupération des données des joueurs
     * Utilise l'annotation @ExceptionHandler pour gérer les exceptions de type PlayerDataRetrievalException
     * Utilise l'annotation @ResponseStatus pour renvoyer un code d'erreur 500
     * @param ex
     * @return
     */
    @ExceptionHandler(PlayerDataRetrievalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handlePlayerDataRetrievalException(PlayerDataRetrievalException ex) {
        return new Error(ex.getMessage());
    }
}
