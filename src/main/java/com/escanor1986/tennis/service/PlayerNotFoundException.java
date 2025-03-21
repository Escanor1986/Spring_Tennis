package com.escanor1986.tennis.service;

public class PlayerNotFoundException extends RuntimeException {
  public PlayerNotFoundException(String lastName) {
      super("Player with last name " + lastName + " could not be found.");
  }
}


/** 
 * Voici l'ordre de traitement du flux lors d'une requête HTTP qui déclenche une exception:

1. **Client HTTP** envoie une requête vers l'API

2. **DispatcherServlet** (Spring) reçoit la requête et la route vers le bon contrôleur

3. **PlayerController** traite la requête et appelle `playerService.getByLastName()`

4. **PlayerService** appelle le repository pour récupérer le joueur, s'il n'existe pas, lance `PlayerNotFoundException`

5. **GlobalExceptionHandler** (@RestControllerAdvice) intercepte l'exception et:
   - Crée une instance de `ErrorResponse` avec le message d'erreur
   - Encapsule cette instance dans un `ResponseEntity` avec statut HTTP 404
   - Retourne cette réponse

6. **Spring Framework** convertit le `ResponseEntity<ErrorResponse>` en JSON 

7. **Client HTTP** reçoit une réponse 404 avec un corps JSON structuré contenant le champ "errorDetails"

Ce flux permet de transformer une exception Java en une réponse HTTP structurée correspondant aux attentes des tests.
 */
