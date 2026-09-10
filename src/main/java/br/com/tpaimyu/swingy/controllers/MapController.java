package br.com.tpaimyu.swingy.controllers;

import br.com.tpaimyu.swingy.models.Coordinates;
import br.com.tpaimyu.swingy.models.Map;

public class MapController {
    private Map actualMap;
    private Coordinates heroPosition;

    public void initializeMap(int heroLevel) {
        this.actualMap = Map.generateMap(heroLevel);
        int center = actualMap.getCenterCoordinate();
        this.heroPosition = new Coordinates(center, center);
    }

    // Dentro do MapController.java
    public char[][] generateMapGrid() {
        char[][] grid = new char[actualMap.size()][actualMap.size()];
        
        // 1. Preenche tudo com vazio
        for (int y = 0; y < actualMap.size(); y++) {
            for (int x = 0; x < actualMap.size(); x++) {
                grid[y][x] = '.';
            }
        }
        
        // // 2. Coloca os vilões
        // for (Villain v : villains) {
        //     Coordinates c = v.getCoordinates();
        //     // Cuidado com a orientação X e Y dependendo de como você fez o grid!
        //     if (c.y() >= 0 && c.y() < mapSize && c.x() >= 0 && c.x() < mapSize) {
        //         grid[c.y()][c.x()] = 'V';
        //     }
        // }
        
        // 3. Coloca o Herói por cima (se ele estiver na mesma casa de um vilão, ele sobrepõe)
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

        // 2. O PULO DO GATO: Substitui a coordenada antiga por uma nova
        this.heroPosition = new Coordinates(newX, newY);

        // 3. TODO: Verificar se colidiu com um vilão antes de checar a vitória
        // checkVillainEncounter();

        // 4. Retorna se a nova posição é a borda do mapa
        return isAtBorder();
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
