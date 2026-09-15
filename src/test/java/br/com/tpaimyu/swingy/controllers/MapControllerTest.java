package br.com.tpaimyu.swingy.controllers;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import br.com.tpaimyu.swingy.models.Coordinates;
import br.com.tpaimyu.swingy.models.Villain;

class MapControllerTest {

    @Test
    void shouldGenerateVillainsOutsideHeroStartingCell() {
        MapController controller = new MapController();
        controller.initializeMap(1);

        int mapSize = controller.getCurrentMap().size();
        int center = controller.getCurrentMap().getCenterCoordinate();
        Set<Coordinates> positions = new HashSet<>();
        char[][] grid = controller.generateMapGrid();
        int villainCount = 0;

        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                if (grid[y][x] == 'V') {
                    villainCount++;
                    positions.add(new Coordinates(x, y));
                    assertNotEquals(new Coordinates(center, center), new Coordinates(x, y));
                }
            }
        }

        assertEquals(2, villainCount);
        assertEquals(villainCount, positions.size());
        assertEquals('H', grid[center][center]);
    }

    @Test
    void shouldExposeEncounteredVillainAfterMovingIntoItsCell() {
        MapController controller = new MapController();
        controller.initializeMap(1);

        Villain villain = controller.getVillains().get(0);
        Coordinates target = controller.getVillainPosition(villain);
        assertNull(controller.getEncounteredVillain());

        while (!controller.getHeroPosition().equals(target)) {
            Coordinates current = controller.getHeroPosition();
            if (current.x() < target.x()) {
                controller.moveHero("east");
            } else if (current.x() > target.x()) {
                controller.moveHero("west");
            } else if (current.y() < target.y()) {
                controller.moveHero("south");
            } else {
                controller.moveHero("north");
            }

            if (controller.getEncounteredVillain() != null) {
                break;
            }
        }

        assertNotNull(controller.getEncounteredVillain());
    }
}
