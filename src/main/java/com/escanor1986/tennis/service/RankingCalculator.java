package com.escanor1986.tennis.service;

import java.util.List;
import java.util.ArrayList;

import com.escanor1986.tennis.Rank;
import com.escanor1986.tennis.Player;
import com.escanor1986.tennis.PlayerList;
import com.escanor1986.tennis.PlayerToSave;

/**
 * This class is responsible for calculating the new ranking of players after a
 * new player is registered.
 * The new player is added to the current ranking list and the list is sorted by
 * points.
 * The new ranking is calculated based on the sorted list.
 * The new ranking is returned as a list of players.
 * 
 * @param currentPlayersRanking The current ranking of players.
 * @param PlayerToSave          The new player to be registered.
 * @return The new ranking of players.
 */
public class RankingCalculator {

  private final List<Player> currentPlayersRanking;
  private final PlayerToSave playerToSave;

  public RankingCalculator(List<Player> currentPlayersRanking, PlayerToSave playerToSave) {
    this.currentPlayersRanking = currentPlayersRanking;
    this.playerToSave = playerToSave;
  }

  public RankingCalculator(List<Player> currentPlayersRanking) {
    this.currentPlayersRanking = currentPlayersRanking;
    this.playerToSave = null;
  }

  // Cette méthode calcule le nouveau classement des joueurs après l'inscription
  // d'un nouveau joueur.
  public List<Player> getNewPlayersRanking() {

    // Ajouter le nouveau joueur à la liste actuelle
    List<Player> newRankingList = new ArrayList<>(currentPlayersRanking);
    
    if (playerToSave != null) {
      newRankingList.add(new Player(
          playerToSave.firstName(),
          playerToSave.lastName(),
          playerToSave.birthDate(),
          new Rank(999999999, playerToSave.points())));
    }

    // Trier la liste par points
    newRankingList.sort((player1, player2) -> Integer.compare(player2.rank().points(), player1.rank().points()));

    // Mise à jour du classement des joueurs
    List<Player> updatedPlayers = new ArrayList<>();

    // Mise à jour du classement des joueurs
    for (int i = 0; i < newRankingList.size(); i++) {
      Player player = newRankingList.get(i);
      Player updatedPlayer = new Player(
          player.firstName(),
          player.lastName(),
          player.birthDate(),
          new Rank(i + 1, player.rank().points()));
      updatedPlayers.add(updatedPlayer);
    }

    PlayerList.ALL = updatedPlayers;

    return updatedPlayers;
  }
}
