package com.escanor1986.tennis;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record Player(
        @NotBlank(message = "Last name is mandatory") String lastName,
        @NotBlank(message = "First name is mandatory") String firstName,
        @NotNull(message = "Birth date is mandatory") @PastOrPresent(message = "Birth date must be past or present") LocalDate birthDate,
        @Valid Rank rank) {
}
