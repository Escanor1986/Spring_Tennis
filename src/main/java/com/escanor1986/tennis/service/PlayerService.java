package com.escanor1986.tennis.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.escanor1986.tennis.Player;
import com.escanor1986.tennis.PlayerToSave;
import com.escanor1986.tennis.Rank;
import com.escanor1986.tennis.data.PlayerEntity;
import com.escanor1986.tennis.data.PlayerRepository;

/**
 * Classe pour gérer les joueurs
 * ! Ajout des blocs try/catch/finally pour gérer les exceptions
 * ! Cela permet de gérer les exceptions de manière centralisée
 * 
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

    @Autowired
    private final PlayerRepository playerRepository;
    private static final Logger log = LoggerFactory.getLogger(PlayerService.class);

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers() {
        log.info("Récupération de la liste des joueurs");
        try {
            return playerRepository.findAll().stream()
                    .map(player -> new Player(
                            player.getFirstName(),
                            player.getLastName(),
                            player.getBirthDate(),
                            new Rank(player.getRank(), player.getPoints())))
                    .sorted(Comparator.comparing(player -> player.rank().position()))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("Erreur lors de la récupération de la liste des joueurs", e);
            throw new PlayerDataRetrievalException(e);
        } finally {
            log.info("Liste des joueurs récupérée {}", playerRepository.findAll());
        }
    }

    /**
     * @param lastName : nom de famille du joueur
     * 
     * @return : retourne un joueur par son nom de famille
     * @throws PlayerNotFoundException : exception si le joueur n'existe pas
     */
    public Player getByLastName(String lastName) {
        log.info("Récupération du joueur : {}", lastName);
        try {
            Optional<PlayerEntity> player = playerRepository.findOneByLastNameIgnoreCase(lastName);
            if (player.isEmpty()) {
                log.warn("Joueur non trouvé : {}", lastName);
                throw new PlayerNotFoundException(lastName);
            }
            return new Player(
                    player.get().getFirstName(),
                    player.get().getLastName(),
                    player.get().getBirthDate(),
                    new Rank(player.get().getRank(), player.get().getPoints()));
        } catch (DataAccessException e) {
            log.error("Erreur lors de la récupération du joueur", e);
            throw new PlayerDataRetrievalException(e);
        } finally {
            log.info("Joueur récupéré : {}", lastName);
        }
    }

    public Player create(PlayerToSave playerToSave) {
        log.info("Création du joueur : {}", playerToSave.lastName());
        try {
            Optional<PlayerEntity> player = playerRepository.findOneByLastNameIgnoreCase(playerToSave.lastName());
            if (player.isPresent()) {
                log.warn("Joueur à créer déjà existant : {}", playerToSave.lastName());
                throw new PlayerAlreadyExistsException(playerToSave.lastName());
            }

            PlayerEntity playerToRegister = new PlayerEntity(
                    playerToSave.lastName(),
                    playerToSave.firstName(),
                    playerToSave.birthDate(),
                    playerToSave.points(),
                    999999999);

            PlayerEntity registeredPlayer = playerRepository.save(playerToRegister);

            RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());
            List<PlayerEntity> newRanking = rankingCalculator.getNewPlayersRanking();
            playerRepository.saveAll(newRanking);

            return getByLastName(registeredPlayer.getLastName());
        } catch (DataAccessException e) {
            log.error("Erreur lors de la création du joueur", e);
            throw new PlayerDataRetrievalException(e);
        } finally {
            log.info("Joueur créé : {}", playerToSave.lastName());
        }
    }

    public Player update(PlayerToSave playerToSave) {
        log.info("Mise à jour du joueur : {}", playerToSave.lastName());
        try {
        Optional<PlayerEntity> playerToUpdate = playerRepository.findOneByLastNameIgnoreCase(playerToSave.lastName());
        if (playerToUpdate.isEmpty()) {
            log.warn("Joueur à modifier non trouvé : {}", playerToSave.lastName());
            throw new PlayerNotFoundException(playerToSave.lastName());
        }

        playerToUpdate.get().setFirstName(playerToSave.firstName());
        playerToUpdate.get().setBirthDate(playerToSave.birthDate());
        playerToUpdate.get().setPoints(playerToSave.points());
        PlayerEntity updatedPlayer = playerRepository.save(playerToUpdate.get());

        RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());
        List<PlayerEntity> newRanking = rankingCalculator.getNewPlayersRanking();
        playerRepository.saveAll(newRanking);

        return getByLastName(updatedPlayer.getLastName());
        } catch (DataAccessException e) {
        log.error("Erreur lors de la mise à jour du joueur", e);
        throw new PlayerDataRetrievalException(e);
        } finally {
        log.info("Joueur mis à jour : {}", playerToSave.lastName());
        }
    }

    public void delete(String lastName) {
        log.info("Suppression du joueur : {}", lastName);
        try {
            Optional<PlayerEntity> playerDelete = playerRepository.findOneByLastNameIgnoreCase(lastName);
            if (playerDelete.isEmpty()) {
                log.warn("Joueur à supprimer non trouvé : {}", lastName);
                throw new PlayerNotFoundException(lastName);
            }

            playerRepository.delete(playerDelete.get());

            RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());
            List<PlayerEntity> newRanking = rankingCalculator.getNewPlayersRanking();
            playerRepository.saveAll(newRanking);
        } catch (DataAccessException e) {
            log.error("Erreur lors de la suppression du joueur", e);
            throw new PlayerDataRetrievalException(e);
        } finally {
            log.info("Joueur supprimé : {}", lastName);
        }
    }
}
