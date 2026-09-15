package br.com.tpaimyu.swingy.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;

import org.junit.jupiter.api.Test;

import br.com.tpaimyu.swingy.models.Artifact;
import br.com.tpaimyu.swingy.models.ArtifactType;
import br.com.tpaimyu.swingy.models.Hero;

class HeroRepositoryTest {

    @Test
    void shouldSaveAndLoadHeroWithProgressAndEquipment() throws Exception {
        var saveDirectory = Files.createTempDirectory("swingy-heroes");
        HeroRepository repository = new HeroRepository(saveDirectory);
        Artifact weapon = new Artifact.ArtifactBuilder()
                .setType(ArtifactType.WEAPON)
                .setBonus(5)
                .setName("Test Sword")
                .build();
        Hero original = new Hero.HeroBuilder()
                .setName("Saved Hero")
                .setHeroClass("Warrior")
                .setLevel(2)
                .setExperience(100)
                .setAttack(15)
                .setDefense(10)
                .setHitPoints(60)
                .setWeapon(weapon)
                .build();

        repository.save(original);
        var loaded = repository.load();

        assertTrue(loaded.isPresent());
        Hero restored = loaded.get();
        assertEquals("Saved Hero", restored.getName());
        assertEquals(2, restored.getLevel());
        assertEquals(100, restored.getExperience());
        assertEquals(15, restored.getBaseAttack());
        assertNotNull(restored.getWeapon());
        assertEquals("Test Sword", restored.getWeapon().getName());
        assertEquals(20, restored.getAttack());

        try (var files = Files.list(saveDirectory)) {
            files.forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            });
        }
        Files.deleteIfExists(saveDirectory);
    }
}
