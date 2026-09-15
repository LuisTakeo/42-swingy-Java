package br.com.tpaimyu.swingy.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import br.com.tpaimyu.swingy.models.Coordinates;
import br.com.tpaimyu.swingy.models.Map;
import br.com.tpaimyu.swingy.models.Villain;

public class MapController {
    private static final int VILLAINS_PER_LEVEL = 2;

    private Map actualMap;
    private Coordinates heroPosition;
    private Coordinates previousHeroPosition;
    private final List<Villain> villains = new ArrayList<>();
    private final List<Coordinates> villainPositions = new ArrayList<>();
    private final Random random = new Random();
    private Villain encounteredVillain;

    public void initializeMap(int heroLevel) {
        this.actualMap = Map.generateMap(heroLevel);
        int center = actualMap.getCenterCoordinate();
        this.heroPosition = new Coordinates(center, center);
        this.previousHeroPosition = this.heroPosition;
        this.encounteredVillain = null;
        generateVillains(heroLevel);
    }

    public char[][] generateMapGrid() {
        char[][] grid = new char[actualMap.size()][actualMap.size()];

        for (int y = 0; y < actualMap.size(); y++) {
            for (int x = 0; x < actualMap.size(); x++) {
                grid[y][x] = '.';
            }
        }

        for (Coordinates position : villainPositions) {
            grid[position.y()][position.x()] = 'V';
        }

        Coordinates h = getHeroPosition();
        if (h.y() >= 0 && h.y() < actualMap.size() && h.x() >= 0 && h.x() < actualMap.size()) {
            grid[h.y()][h.x()] = 'H';
        }
        
        return grid;
    }
    public boolean moveHero(String direction) {
        int newX = heroPosition.x();
        int newY = heroPosition.y();

        // 1. Calcula a nova posição
        switch (direction.toLowerCase()) {
            case "north": newY -= 1; break;
            case "south": newY += 1; break;
            case "east":  newX += 1; break;
            case "west":  newX -= 1; break;
            default:
                // Ignora comandos inválidos
                return false; 
        }

        this.previousHeroPosition = this.heroPosition;
        this.heroPosition = new Coordinates(newX, newY);
        this.encounteredVillain = findVillainAt(heroPosition);
        return isAtBorder();
    }

    public void restorePreviousHeroPosition() {
        this.heroPosition = this.previousHeroPosition;
        this.encounteredVillain = null;
    }

    private void generateVillains(int heroLevel) {
        villains.clear();
        villainPositions.clear();

        int villainCount = heroLevel * VILLAINS_PER_LEVEL;
        int center = actualMap.getCenterCoordinate();

        for (int index = 0; index < villainCount; index++) {
            Coordinates position = randomFreeInteriorPosition(center);
            villainPositions.add(position);
            villains.add(new Villain.VillainBuilder()
                    .setName("Goblin " + (index + 1))
                    .setLevel(heroLevel)
                    .setAttack(Math.max(1, heroLevel * 2))
                    .setDefense(Math.max(0, heroLevel))
                    .setHitPoints(Math.max(1, heroLevel * 10))
                    .build());
        }
    }

    private Coordinates randomFreeInteriorPosition(int center) {
        int maxIndex = actualMap.size() - 1;
        Coordinates position;

        do {
            position = new Coordinates(
                    1 + random.nextInt(maxIndex - 1),
                    1 + random.nextInt(maxIndex - 1));
        } while ((position.x() == center && position.y() == center)
                || villainPositions.contains(position));

        return position;
    }

    private Villain findVillainAt(Coordinates position) {
        for (int index = 0; index < villainPositions.size(); index++) {
            Coordinates villainPosition = villainPositions.get(index);
            if (villainPosition.x() == position.x()
                    && villainPosition.y() == position.y()) {
                return villains.get(index);
            }
        }
        return null;
    }

    public List<Villain> getVillains() {
        return Collections.unmodifiableList(villains);
    }

    public Coordinates getVillainPosition(Villain villain) {
        int index = villains.indexOf(villain);
        return index >= 0 ? villainPositions.get(index) : null;
    }

    public Villain getEncounteredVillain() {
        return encounteredVillain;
    }

    public void removeVillain(Villain villain) {
        int index = villains.indexOf(villain);
        if (index >= 0) {
            villains.remove(index);
            villainPositions.remove(index);
        }
        encounteredVillain = null;
    }

    private boolean isAtBorder() {
        int maxIndex = actualMap.size() - 1;
        
        return heroPosition.x() <= 0 || 
               heroPosition.y() <= 0 || 
               heroPosition.x() >= maxIndex || 
               heroPosition.y() >= maxIndex;
    }

    public Coordinates getHeroPosition() {
        return this.heroPosition;
    }

    public Map getCurrentMap() {
        return this.actualMap;
    }
}
