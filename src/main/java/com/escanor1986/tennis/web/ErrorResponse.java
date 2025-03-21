package com.escanor1986.tennis.web;

/**
 * Classe qui définit la structure standardisée des réponses d'erreur de l'API
 * 
 * IMPORTANT : Cette classe est nécessaire car nos tests vérifient la présence 
 * d'un champ "errorDetails" dans la réponse JSON d'erreur.
 * Sans cette structure, Spring renvoie simplement le message d'erreur comme une chaîne,
 * ce qui ne correspond pas au format attendu par les tests.
 */
public class ErrorResponse {
    // Champ attendu par les tests via jsonPath("$.errorDetails")
    private String errorDetails;

    // Constructeur par défaut nécessaire pour la désérialisation JSON
    public ErrorResponse() {
    }

    // Constructeur avec le message d'erreur
    public ErrorResponse(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    // Getters et setters nécessaires pour la sérialisation JSON
    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }
}
