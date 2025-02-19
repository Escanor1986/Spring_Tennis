package com.escanor1986.tennis.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.escanor1986.tennis.Player;
import com.escanor1986.tennis.PlayerList;
import com.escanor1986.tennis.PlayerToSave;

@Service
public class PlayerService {

  // Cette méthode retourne la liste de tous les joueurs triée par classement.
  public List<Player> getAllPlayers() {
    return PlayerList.ALL.stream()
        .sorted(Comparator.comparing(player -> player.rank().position()))
        .collect(Collectors.toList());
  }

  // Cette méthode retourne un joueur par son nom de famille.
  public Player getPlayerByLastName(String lastName) {
    return PlayerList.ALL.stream()
        .filter(player -> player.lastName().equals(lastName))
        .findFirst()
        .orElseThrow(() -> new PlayerNotFoundException(lastName));
  }

  // Cette méthode crée un nouveau joueur et retourne le joueur créé.
  public Player createPlayer(PlayerToSave playerToSave) {
    // liste des joueurs
    return getPlayerNewRanking(PlayerList.ALL, playerToSave);
  }

  // Cette méthode met à jour un joueur et retourne le joueur mis à jour.
  public Player updatePlayer(PlayerToSave playerToSave) {

    getPlayerByLastName(playerToSave.lastName());

    List<Player> playersWithoutPlayerToUpdate = PlayerList.ALL.stream()
        .filter(player -> !player.lastName().equals(playerToSave.lastName()))
        .toList();

    return getPlayerNewRanking(playersWithoutPlayerToUpdate, playerToSave);
  }

  // Cette méthode supprime un joueur par son nom de famille.
  public void delete(String lastName) {
    Player playerToDelete = getPlayerByLastName(lastName);

    PlayerList.ALL = PlayerList.ALL.stream()
            .filter(player -> !player.lastName().equals(lastName))
            .toList();

    RankingCalculator rankingCalculator = new RankingCalculator(PlayerList.ALL);
    rankingCalculator.getNewPlayersRanking();
 }

  // Cette méthode retourne un joueur avec le nouveau classement.
  private Player getPlayerNewRanking(List<Player> existingPlayers, PlayerToSave playerToSave) {
    RankingCalculator rankingCalculator = new RankingCalculator(existingPlayers, playerToSave);
    List<Player> players = rankingCalculator.getNewPlayersRanking();

    return players.stream()
        .filter(player -> player.lastName().equals(playerToSave.lastName()))
        .findFirst()
        .orElseThrow(() -> new PlayerNotFoundException(playerToSave.lastName()));
  }

}
