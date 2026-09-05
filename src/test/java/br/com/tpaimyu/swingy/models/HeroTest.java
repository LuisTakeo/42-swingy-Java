package br.com.tpaimyu.swingy.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolationException;

public class HeroTest {


    @Test
    public void testHeroBuilder() {
        Hero hero = new Hero.HeroBuilder()
                .setName("TestHero")
                .setLevel(1)
                .setAttack(10)
                .setDefense(10)
                .setHitPoints(100)
                .setHeroClass("Warrior")
                .build();
        assertNotNull(hero);
        assertEquals(hero.getHeroClass(), "Warrior");
        assertEquals(hero.getName(), "TestHero");
        assertEquals(hero.getLevel(), 1);
        assertEquals(hero.getAttack(), 10);
        assertEquals(hero.getDefense(), 10);
        assertEquals(hero.getHitPoints(), 100);
    }

    @Test
    void shouldRejectBlankHeroClass() {
        
        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> {
            Hero hero = new Hero.HeroBuilder()
                    .setName("TestHero")
                    .setLevel(1)
                    .setAttack(10)
                    .setDefense(10)
                    .setHitPoints(100)
                    .setHeroClass("") // Blank hero class
                    .build();
                });
            assertTrue(exception
                .getMessage()
                .contains("Hero class cannot be blank"));
    }

    
    @Test
    void shouldRejectNegativeLevel() {
        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> {
            Hero hero = new Hero.HeroBuilder()
                    .setName("TestHero")
                    .setLevel(-1) // Negative level
                    .setAttack(10)
                    .setHitPoints(100)
                    .setHeroClass("Warrior")
                    .build();
                });
            assertTrue(exception
                .getMessage()
                .contains("Level must be at least 1"));
                
    }

    @Test
    void shouldRejectNegativeAttack() {
        
        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> {
            Hero hero = new Hero.HeroBuilder()
                    .setName("TestHero")
                    .setLevel(1)
                    .setAttack(-1) // Negative attack
                    .setHitPoints(100)
                    .setHeroClass("Warrior")
                    .build();
                });
            assertTrue(exception
                .getMessage()
                .contains("Attack must be at least 1"));
    }

    @Test
    void shouldRejectNegativeHitPoints() {
        
        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> {
            Hero hero = new Hero.HeroBuilder()
                    .setName("TestHero")
                    .setLevel(1)
                    .setAttack(10)
                    .setHitPoints(-1) // Negative hit points
                    .setHeroClass("Warrior")
                    .build();
                });
            assertTrue(exception
                .getMessage()
                .contains("Hit points must be at least 1"));
    }


    @Test
    void shouldRejectNegativeDefense() {
        
        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> {
            Hero hero = new Hero.HeroBuilder()
                    .setName("TestHero")
                    .setLevel(1)
                    .setAttack(10)
                    .setHitPoints(100)
                    .setDefense(-1) // Negative defense
                    .setHeroClass("Warrior")
                    .build();
                });
            assertTrue(exception
                .getMessage()
                .contains("Defense must be zero or positive"));
    }

}
