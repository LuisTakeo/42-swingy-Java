package br.com.tpaimyu.swingy.models;

import jakarta.validation.constraints.Positive;

public record Map(
    @Positive(message = "Map size must be positive")
    int size
) {
    

    public static Map generateMap(int heroLevel) {
        int mapSize = (heroLevel - 1) * 5 + 10 - (heroLevel % 2);
        return new Map(mapSize);
    }

    public int getSize() {
        return size;
    }

    public int getCenterCoordinate() {
        return size / 2;
    }
}
