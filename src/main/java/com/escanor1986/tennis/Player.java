package com.escanor1986.tennis;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

public record Player(
    @NotBlank(message = "Le prénom ne peut pas être vide.")
    String firstName,

    @NotBlank(message = "Le nom ne peut pas être vide.")
    String lastName,

    @PastOrPresent(message = "La date de naissance doit être dans le passé ou aujourd'hui.")
    LocalDate birthDate,

    @Valid
    Rank rank) {
}

