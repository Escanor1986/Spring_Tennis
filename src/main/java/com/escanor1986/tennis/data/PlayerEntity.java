package com.escanor1986.tennis.data;

import jakarta.persistence.*;

import java.time.LocalDate;

// Représentation Java de la table "player" en base de données postgresql
// name = "player" : nom de la table en base de données
// schema = "public" : schéma de la table en base de données
// @Entity : annotation pour dire que cette classe est une entité
// @Table : annotation pour dire que cette classe est une table
// @Id : annotation pour dire que l'attribut est une clé primaire
// @GeneratedValue : annotation pour dire que la valeur de l'attribut est générée automatiquement
// @Column : annotation pour dire que l'attribut est une colonne
@Entity
@Table(name = "player", schema = "public")
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "rank", nullable = false)
    private Integer rank;

    public PlayerEntity() {
    }

    // Utiliser pour créer un joueur en base de données
    // Pas be soin d'id grâce à la génération automatique 
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    public PlayerEntity(String lastName, String firstName, LocalDate birthDate, Integer points, Integer rank) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.points = points;
        this.rank = rank;
    }

    public PlayerEntity(Long id, String lastName, String firstName, LocalDate birthDate, Integer points, Integer rank) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.points = points;
        this.rank = rank;
    }

    public Long getId() { return id; }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
