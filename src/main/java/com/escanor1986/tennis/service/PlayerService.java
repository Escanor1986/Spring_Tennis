package com.escanor1986.tennis.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(player -> new Player(
                        player.getFirstName(),
                        player.getLastName(),
                        player.getBirthDate(),
                        new Rank(player.getRank(), player.getPoints())
                ))
                .sorted(Comparator.comparing(player -> player.rank().position()))
                .collect(Collectors.toList());
    }

    /** 
     * @param lastName : nom de famille du joueur
     * 
     * @return : retourne un joueur par son nom de famille
     * @throws PlayerNotFoundException : exception si le joueur n'existe pas
     */
    public Player getByLastName(String lastName) {
        Optional<PlayerEntity> player = playerRepository.findOneByLastNameIgnoreCase(lastName);
        if (player.isEmpty()) { 
            throw new PlayerNotFoundException(lastName);
        }
        return new Player(
                player.get().getFirstName(),
                player.get().getLastName(),
                player.get().getBirthDate(),
                new Rank(player.get().getRank(), player.get().getPoints())
        );
    }

    public Player create(PlayerToSave playerToSave) {
        Optional<PlayerEntity> player = playerRepository.findOneByLastNameIgnoreCase(playerToSave.lastName());
        if (player.isPresent()) {
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
    }

    public Player update(PlayerToSave playerToSave) {
        Optional<PlayerEntity> playerToUpdate = playerRepository.findOneByLastNameIgnoreCase(playerToSave.lastName());
        if (playerToUpdate.isEmpty()) {
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
    }

    public void delete(String lastName) {
        Optional<PlayerEntity> playerDelete = playerRepository.findOneByLastNameIgnoreCase(lastName);
        if (playerDelete.isEmpty()) {
            throw new PlayerNotFoundException(lastName);
        }

        playerRepository.delete(playerDelete.get());

        RankingCalculator rankingCalculator = new RankingCalculator(playerRepository.findAll());
        List<PlayerEntity> newRanking = rankingCalculator.getNewPlayersRanking();
        playerRepository.saveAll(newRanking);
    }
}
