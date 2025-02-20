package com.escanor1986.tennis.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.escanor1986.tennis.Player;
import com.escanor1986.tennis.PlayerList;
import com.escanor1986.tennis.PlayerToSave;
import com.escanor1986.tennis.Rank;
import com.escanor1986.tennis.data.PlayerRepository;

@Service
public class PlayerService {

  // On vient brancher le repository pour pouvoir accéder à la base de données
  private final PlayerRepository playerRepository;

    PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

  // Cette méthode retourne la liste de tous les joueurs triée par classement.
  public List<Player> getAllPlayers() {
    // On traite un objet "entity" qui est une représentation de la table "player"
    // en base de données
    // il faut donc le transformer en objet "player" utilisable dans le service
    return playerRepository.findAll().stream()
        .map(playerEntity -> new Player(playerEntity.getLastName(), playerEntity.getFirstName(),
            playerEntity.getBirthDate(), new Rank(playerEntity.getRank(), playerEntity.getPoints())))
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
    // En l'absence de joueur avec le nom de famille spécifié, une exception est
    // levée.
    Player playerToDelete = getPlayerByLastName(lastName);

    // Supprimer le joueur de la liste en mémoire en filtrant par nom de famille
    PlayerList.ALL = PlayerList.ALL.stream()
        .filter(player -> !player.lastName().equals(lastName))
        .toList();

    // Recalculer le classement des joueurs
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
