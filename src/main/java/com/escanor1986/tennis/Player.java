package com.escanor1986.tennis;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

public record Player(
                @NotBlank String firstName,
                @NotBlank String lastName,
                @PastOrPresent LocalDate birthDate,
                @Valid Rank rank) {

}
