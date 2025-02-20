package com.escanor1986.tennis.web;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.escanor1986.tennis.Player;
import com.escanor1986.tennis.PlayerToSave;
import com.escanor1986.tennis.service.PlayerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Classe pour gérer les joueurs
 * 
 * @Tag : annotation pour dire que cette classe est un contrôleur
 * @RestController : annotation pour dire que cette classe est un contrôleur
 * 
 * @param playerService : service pour gérer les joueurs
 * 
 * @Operation : annotation pour décrire une opération
 * @ApiResponses : annotation pour décrire les réponses possibles
 * @ApiResponse : annotation pour décrire une réponse
 * @Content : annotation pour décrire le contenu de la réponse
 * @Schema : annotation pour décrire le schéma d'un objet
 * @ArraySchema : annotation pour décrire le schéma d'un tableau
 * @PathVariable : annotation pour dire que l'attribut est un paramètre de l'URL
 * 
 * @return : retourne la liste de tous les joueurs
 * @return : retourne un joueur par son nom de famille
 * @return : crée un nouveau joueur et retourne le joueur créé
 * @return : met à jour un joueur et retourne le joueur mis à jour
 * @return : supprime un joueur
 * 
  */
@Tag(name = "Player", description = "The player API")
@RestController
public class PlayerController {

  // ✅ Injection du service via le constructeur (recommandé) plutôt que par
  // @Autowired
  private final PlayerService playerService;

  PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  // ! Listing des joueurs
  @Operation(summary = "Finds players", description = "Retrieves a list of players")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Players list", content = {
          @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Player.class))) })
  })

  @GetMapping("/player")
  public List<Player> list() {
    // Utilisation du service PlayerService pour obtenir la liste des joueurs
    return playerService.getAllPlayers();
  }

  // ! Recherche d'un joueur par son nom
  @Operation(summary = "Finds a player", description = "Finds a player")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Player", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = Player.class)) }),
      @ApiResponse(responseCode = "404", description = "A player with the specified last name was not found", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)) })
  })
  @GetMapping("{lastName}")
  public Player getByLastName(@PathVariable("lastName") String lastName) {
    // Utilisation du service PlayerService pour obtenir un joueur par son nom
    return playerService.getByLastName(lastName);
  }

  // ! Créer un joueur
  @Operation(summary = "Creates a player", description = "Creates a player")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Created player",
                  content = {@Content(mediaType = "application/json",
                          schema = @Schema(implementation = PlayerToSave.class))}),
          @ApiResponse(responseCode = "400", description = "Player with specified last name already exists.",
                  content = {@Content(mediaType = "application/json",
                          schema = @Schema(implementation = Error.class))})

  })
  @PostMapping
  public Player createPlayer(@RequestBody @Valid PlayerToSave playerToSave) {
      return playerService.create(playerToSave);
  }

  // ! Mettre à jour un joueur
  @Operation(summary = "Update a player", description = "Update an existing player")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Player updated", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = PlayerToSave.class)) }),
      @ApiResponse(responseCode = "404", description = "A player with the specified last name was not found", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)) })

  })

  @PutMapping("/player")
  public Player updatePlayer(@RequestBody @Valid PlayerToSave playerToSave) {
    return playerService.updatePlayer(playerToSave);
  }

  // ! Supprimer un joueur
  @Operation(summary = "Deletes a player", description = "Deletes a player")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Player has been deleted"),
      @ApiResponse(responseCode = "404", description = "Player with specified last name was not found.", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class)) })

  })
  @DeleteMapping("{lastName}")
  public void deletePlayerByLastName(@PathVariable("lastName") String lastName) {
    playerService.delete(lastName);
  }
}
