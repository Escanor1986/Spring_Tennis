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
    return playerService.getPlayerByLastName(lastName);
  }

  // ! Créer un joueur
  @Operation(summary = "Create a player", description = "Create a new player")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Player created", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = Player.class)) })
  })

  @PostMapping("/player")
  public Player createPlayer(@RequestBody @Valid PlayerToSave playerToSave) {
    return playerService.createPlayer(playerToSave);
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
  @Operation(summary = "Delete a player", description = "Delete an existing player")
  @ApiResponses(value = {
      // Le code de réponse 204 signifie que la requête a été traitée avec succès
      // mais qu'il n'y a pas de contenu à renvoyer (corps de requête vide)
      @ApiResponse(responseCode = "204", description = "Player deleted", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = Player.class)) })
  })

  @DeleteMapping("/player/{lastName}")
  public void deletePlayer(@PathVariable("lastName") String lastName) {
    // Cette méthode est vide car elle ne retourne rien
  }
}
