package com.escanor1986.tennis.service;

/** L'exception est levée lorsqu'une erreur d'accès aux données se produit 
 * Cette class sert à gérer les exceptions de manière centralisée
 *  @param e : exception
 * @return : retourne une exception
 */
public class PlayerDataRetrievalException extends RuntimeException {

  public PlayerDataRetrievalException(Exception e) {
    super("Could not retrieve player data", e);
  }
}
