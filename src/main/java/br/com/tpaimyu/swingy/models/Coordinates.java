package br.com.tpaimyu.swingy.models;

import jakarta.validation.constraints.PositiveOrZero;

public record Coordinates(
    @PositiveOrZero (message = "X coordinate must be zero or positive")
    int x,
    @PositiveOrZero (message = "Y coordinate must be zero or positive")
    int y
) {
}
     