package com.escanor1986.tennis;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

public record PlayerToRegister(
    @NotBlank(message="First name is mandatory") String firstName,
    @NotBlank(message="Last name is mandatory") String lastName,
    @NotNull(message="Birth date is mandatory") @PastOrPresent(message="Birth date must be in the past or present") LocalDate birthDate,
    @PositiveOrZero(message="Points must be positive or zero") int points
) {

}
