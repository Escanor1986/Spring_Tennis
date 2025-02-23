package com.escanor1986.tennis.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.escanor1986.tennis.Player;
import com.escanor1986.tennis.PlayerToSave;
import com.escanor1986.tennis.Rank;
import com.escanor1986.tennis.data.PlayerEntity;
import com.escanor1986.tennis.data.PlayerRepository;

/**
 * Classe pour gérer les joueurs
 * 
 * @Service : annotation pour dire que cette classe est un service
 * 
 * @param playerRepository : repository pour accéder à la base de données
 * 
 * @return : retourne la liste de tous les joueurs triée par classement
 * @return : retourne un joueur par son nom de famille
 * @return : crée un nouveau joueur et retourne le joueur créé
 * @return : met à jour un joueur et retourne le joueur mis à jour
 * @return : supprime un joueur
 */
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
  public Player getByLastName(String lastName) {

    Optional<PlayerEntity> player = playerRepository.findOneByLastNameIgnoreCase(lastName);

    // Si le joueur n'existe pas, une exception est levée
    if (player.isEmpty()) {
      throw new PlayerNotFoundException(lastName);
    }

    PlayerEntity playerEntity = player.get();

    return new Player(playerEntity.getLastName(), playerEntity.getFirstName(),
        playerEntity.getBirthDate(), new Rank(playerEntity.getRank(), playerEntity.getPoints()));
  }

  // Cette méthode crée un nouveau joueur et retourne le joueur créé.
  public Player create(PlayerToSave playerToSave) {

    // On vérifie que le joueur n'existe pas déjà
    Optional<PlayerEntity> player = playerRepository.findOneByLastNameIgnoreCase(playerToSave.lastName());

    // Si le joueur existe déjà, une exception est levée
    if (player.isPresent()) {
      throw new PlayerAlreadyExistsException(playerToSave.lastName());
    }

    Integer temporaryRank = 999999999;

    // On crée un objet "entity" à partir de l'objet "player" reçu en paramètre
    PlayerEntity playerToRegister = new PlayerEntity(
        playerToSave.lastName(),
        playerToSave.firstName(),
        playerToSave.birthDate(),
        playerToSave.points(),
        temporaryRank);

    // On enregistre le joueur en base de données
    PlayerEntity registeredPlayer = playerRepository.save(playerToRegister);

    // On récupère la liste des joueurs actuels en base de données avec findAll
    RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());

    // On calcule le nouveau classement des joueurs et on le sauvegarde en base de données
    List<PlayerEntity> newRanking = rankingCalculator.getNewPlayersRanking();
    playerRepository.saveAll(newRanking);

    // On retourne le joueur créé
    return getByLastName(registeredPlayer.getLastName());
  }

  public Player updatePlayer(PlayerToSave playerToSave) {

    // On vérifie que le joueur n'existe pas déjà
    Optional<PlayerEntity> playerToUpdate = playerRepository.findOneByLastNameIgnoreCase(playerToSave.lastName());

    // Si le joueur n'existe pas, une exception est levée
    if (playerToUpdate.isEmpty()) {
      throw new PlayerAlreadyExistsException(playerToSave.lastName());
    }

    // On met à jour le joueur en base de données
    playerToUpdate.get().setFirstName(playerToSave.firstName());
    playerToUpdate.get().setBirthDate(playerToSave.birthDate());
    playerToUpdate.get().setPoints(playerToSave.points());

    // On enregistre le joueur en base de données
    playerRepository.save(playerToUpdate.get());

    // On récupère la liste des joueurs actuels en base de données avec findAll
    RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());

    // On calcule le nouveau classement des joueurs et on le sauvegarde en base de données
    List<PlayerEntity> newRanking = rankingCalculator.getNewPlayersRanking();
    playerRepository.saveAll(newRanking);

    // On retourne le joueur créé
    return getByLastName(playerToSave.lastName());

  }

  public void delete(String lastName) {
    // supprimer un joueur
    Optional<PlayerEntity> playerToDelete = playerRepository.findOneByLastNameIgnoreCase(lastName);

    // Si le joueur n'existe pas, une exception est levée
    if (playerToDelete.isEmpty()) {
      throw new PlayerNotFoundException(lastName);
    }

    playerRepository.delete(playerToDelete.get());

    // On récupère la liste des joueurs actuels en base de données avec findAll
    RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());

    // On calcule le nouveau classement des joueurs et on le sauvegarde en base de données
    List<PlayerEntity> newRanking = rankingCalculator.getNewPlayersRanking();
    playerRepository.saveAll(newRanking);
  }
}
