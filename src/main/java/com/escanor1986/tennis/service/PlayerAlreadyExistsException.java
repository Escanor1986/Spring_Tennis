package com.escanor1986.tennis.service;

/**
 * Exception levée si un joueur existe déjà en base de données
 * 
 * @param lastName : nom de famille du joueur
 */
public class PlayerAlreadyExistsException extends RuntimeException {
  public PlayerAlreadyExistsException(String lastName) {
      super("Player with last name " + lastName + " already exists.");
  }
}
