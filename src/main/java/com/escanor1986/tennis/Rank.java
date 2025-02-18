package com.escanor1986.tennis;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record Rank(
    @Positive(message = "La position doit être un nombre strictement positif.")
    int position,

    @PositiveOrZero(message = "Les points doivent être un nombre positif ou nul.")
    int points) {
}

