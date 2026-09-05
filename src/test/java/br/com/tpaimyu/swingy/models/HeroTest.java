package br.com.tpaimyu.swingy.models;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

public class HeroTest {


    @Test
    public void testHeroBuilder() {
        Hero hero = new Hero.HeroBuilder()
                .setName("TestHero")
                .setLevel(1)
                .setAttack(10)
                .setHitPoints(100)
                .setHeroClass("Warrior")
                .build();
        assertNotNull(hero);
        assertEquals(hero.getHeroClass(), "Warrior");
        assertEquals(hero.getName(), "TestHero");
        assertEquals(hero.getLevel(), 1);
        assertEquals(hero.getAttack(), 10);
        assertEquals(hero.getHitPoints(), 100);
    }

    @Test
    void shouldRejectBlankHeroClass() {
        try {
            Hero hero = new Hero.HeroBuilder()
                    .setName("TestHero")
                    .setLevel(1)
                    .setAttack(10)
                    .setHitPoints(100)
                    .setHeroClass("") // Blank hero class
                    .build();
            assertFalse(true, "Expected ConstraintViolationException was not thrown");
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            assertEquals(1, violations.size());
            ConstraintViolation<?> violation = violations.iterator().next();
            assertEquals("Hero class cannot be blank", violation.getMessage());
        }

    }

    
    @Test
    void shouldRejectNegativeLevel() {
        try {
            Hero hero = new Hero.HeroBuilder()
                    .setName("TestHero")
                    .setLevel(-1) // Negative level
                    .setAttack(10)
                    .setHitPoints(100)
                    .setHeroClass("Warrior")
                    .build();
            assertFalse(true, "Expected ConstraintViolationException was not thrown");
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            assertEquals(1, violations.size());
            ConstraintViolation<?> violation = violations.iterator().next();
            assertEquals("Level must be at least 1", violation.getMessage());
        }
    }

    @Test
    void shouldRejectNegativeAttack() {
        try {
            Hero hero = new Hero.HeroBuilder()
                    .setName("TestHero")
                    .setLevel(1)
                    .setAttack(-1) // Negative attack
                    .setHitPoints(100)
                    .setHeroClass("Warrior")
                    .build();
            assertFalse(true, "Expected ConstraintViolationException was not thrown");
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            assertEquals(1, violations.size());
            ConstraintViolation<?> violation = violations.iterator().next();
            assertEquals("Attack must be at least 1", violation.getMessage());
        }
    }

    @Test
    void shouldRejectNegativeHitPoints() {
        try {
            Hero hero = new Hero.HeroBuilder()
                    .setName("TestHero")
                    .setLevel(1)
                    .setAttack(10)
                    .setHitPoints(-1) // Negative hit points
                    .setHeroClass("Warrior")
                    .build();
            assertFalse(true, "Expected ConstraintViolationException was not thrown");
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            assertEquals(1, violations.size());
            ConstraintViolation<?> violation = violations.iterator().next();
            assertEquals("Hit points must be at least 1", violation.getMessage());
        }
    }

    
}
