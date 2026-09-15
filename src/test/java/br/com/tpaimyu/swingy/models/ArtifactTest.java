package br.com.tpaimyu.swingy.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolationException;

public class ArtifactTest {

    @Test
    public void testArtifactCreation() {
        Artifact artifact = new Artifact.ArtifactBuilder()
                .setType(ArtifactType.WEAPON)
                .setBonus(10)
                .setName("Excalibur")
                .build();

        assertNotNull(artifact);
        assertEquals(ArtifactType.WEAPON, artifact.getType());
        assertEquals(10, artifact.getBonus());
        assertEquals("Excalibur", artifact.getName());
    }

    @Test
    public void testArtifactBuilder() {
        Artifact artifact = new Artifact.ArtifactBuilder()
                .setType(ArtifactType.ARMOR)
                .setBonus(5)
                .setName("Dragon Scale Armor")
                .build();

        assertNotNull(artifact);
        assertEquals(ArtifactType.ARMOR, artifact.getType());
        assertEquals(5, artifact.getBonus());
        assertEquals("Dragon Scale Armor", artifact.getName());
    }

    @Test
    public void testArtifactBuilderWithInvalidBonus() {
        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> {
            Artifact artifact = new Artifact.ArtifactBuilder()
                    .setType(ArtifactType.HELMET)
                    .setBonus(0) // Invalid bonus
                    .setName("Iron Helmet")
                    .build();
                });
            assertTrue(exception
                .getMessage()
                .contains("Artifact bonus must be at least 1"));

    }

    @Test
    public void testArtifactBuilderWithInvalidName() {
        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> {
            Artifact artifact = new Artifact.ArtifactBuilder()
                    .setType(ArtifactType.WEAPON)
                    .setBonus(10)
                    .setName("") // Invalid name
                    .build();
                });
        assertTrue(exception
            .getMessage()
            .contains("Artifact name cannot be blank"));
    }

    @Test
    public void testArtifactBuilderWithNullType() {
        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> {
            Artifact artifact = new Artifact.ArtifactBuilder()
                    .setType(null) // Invalid type
                    .setBonus(10)
                    .setName("Mystic Wand")
                    .build();
                });

        assertTrue(exception
            .getMessage()
            .contains("Artifact type cannot be null"));
    }
}