package com.escanor1986.tennis.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import com.escanor1986.tennis.Rank;
import com.escanor1986.tennis.Player;
import com.escanor1986.tennis.PlayerToRegister;

/**
 * This class is responsible for calculating the new ranking of players after a new player is registered.
 * The new player is added to the current ranking list and the list is sorted by points.
 * The new ranking is calculated based on the sorted list.
 * The new ranking is returned as a list of players.
 * @param currentPlayersRanking The current ranking of players.
 * @param playerToRegister The new player to be registered.
 * @return The new ranking of players.
  */
public class RankingCalculator {

  private final List<Player> currentPlayersRanking;
  private final PlayerToRegister playerToRegister;

  public RankingCalculator(List<Player> currentPlayersRanking, PlayerToRegister playerToRegister) {
    this.currentPlayersRanking = currentPlayersRanking;
    this.playerToRegister = playerToRegister;
  }

  // Cette méthode calcule le nouveau classement des joueurs après l'inscription d'un nouveau joueur.
  public List<Player> getNewPlayersRanking() {

    // Ajouter le nouveau joueur à la liste actuelle
    List<Player> newRankingList = new ArrayList<>(currentPlayersRanking);
    newRankingList.add(new Player(
        playerToRegister.firstName(),
        playerToRegister.lastName(),
        playerToRegister.birthDate(),
        new Rank(999999999, playerToRegister.points())));

    // Tri de la liste par points
    final List<Player> sortedPlayers = newRankingList.stream()
        .sorted(Comparator.comparing(player -> player.rank().points()))
        .toList();

    // Mise à jour du classement des joueurs    
    List<Player> updatedPlayers = new ArrayList<>();

    // Mise à jour du classement des joueurs
    for (int i = 0; i < sortedPlayers.size(); i++) {
      Player player = sortedPlayers.get(i);
      Player updatedPlayer = new Player(
          player.firstName(),
          player.lastName(),
          player.birthDate(),
          new Rank(i + 1, player.rank().points()));
      updatedPlayers.add(updatedPlayer);
    }

    return updatedPlayers;
  }
}
