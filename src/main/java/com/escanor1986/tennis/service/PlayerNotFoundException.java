package com.escanor1986.tennis.service;

public class PlayerNotFoundException extends RuntimeException {
  public PlayerNotFoundException(String lastName) {
      super("Player with last name " + lastName + " could not be found.");
  }
}
