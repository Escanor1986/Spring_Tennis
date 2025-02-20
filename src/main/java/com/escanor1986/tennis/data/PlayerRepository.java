package com.escanor1986.tennis.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Long : type de la clé primaire
// PlayerEntity : type de l'entité
// JpaRepository<PlayerEntity, Long> : interface qui permet de faire des opérations en base de données
// @Repository : annotation pour dire que cette classe est un repository
@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

  // Query méthode de Spring Data pour retrouver un joueur par son nom de famille
  // findOneByLastNameIgnoreCase : nom de la méthode, elle est générée automatiquement et permet de retrouver un joueur par son nom de famille
  // Optional<PlayerEntity> : type de retour de la méthode
  Optional<PlayerEntity> findOneByLastNameIgnoreCase(String lastName);
}
