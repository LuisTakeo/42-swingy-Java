package br.com.tpaimyu.swingy.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.Test;

import br.com.tpaimyu.swingy.models.Hero;
import br.com.tpaimyu.swingy.models.Villain;
import br.com.tpaimyu.swingy.views.GameView;

class BattleControllerTest {

    @Test
    void shouldWinBattleAndGrantExperience() {
        Hero hero = hero(100, 100, 10);
        Villain villain = villain(1, 1, 1, 1);

        BattleResult result = new BattleController(new FixedRandom(2, 99))
                .fight(new TestView(), hero, villain);

        assertEquals(BattleResult.VICTORY, result);
        assertTrue(villain.isDead());
        assertEquals(500, hero.getExperience());
    }

    @Test
    void shouldLoseBattleWhenHeroDies() {
        Hero hero = hero(1, 1, 1);
        Villain villain = villain(1, 100, 1, 100);

        BattleResult result = new BattleController(new FixedRandom(2, 99))
                .fight(new TestView(), hero, villain);

        assertEquals(BattleResult.DEFEAT, result);
        assertTrue(hero.isDead());
    }

    @Test
    void shouldReturnEscapeWhenRandomAllowsIt() {
        boolean escaped = new BattleController(new FixedRandom(2, 99, true))
                .tryToEscape(new TestView());

        assertTrue(escaped);
    }

    private Hero hero(int hitPoints, int attack, int defense) {
        return new Hero.HeroBuilder()
                .setName("Hero")
                .setHeroClass("Warrior")
                .setLevel(1)
                .setAttack(attack)
                .setDefense(defense)
                .setHitPoints(hitPoints)
                .build();
    }

    private Villain villain(int level, int hitPoints, int attack, int defense) {
        return new Villain.VillainBuilder()
                .setName("Goblin")
                .setLevel(level)
                .setAttack(attack)
                .setDefense(defense)
                .setHitPoints(hitPoints)
                .build();
    }

    private static class FixedRandom extends Random {
        private final int[] values;
        private final boolean booleanValue;
        private int index;

        FixedRandom(int first, int second) {
            this(first, second, false);
        }

        FixedRandom(int first, int second, boolean booleanValue) {
            this.values = new int[] { first, second };
            this.booleanValue = booleanValue;
        }

        @Override
        public int nextInt(int bound) {
            return values[Math.min(index++, values.length - 1)] % bound;
        }

        @Override
        public boolean nextBoolean() {
            return booleanValue;
        }
    }

    private static class TestView implements GameView {
        private final BlockingQueue<String> input = new LinkedBlockingQueue<>();

        @Override public void start() { }
        @Override public void showMessage(String message) { }
        @Override public String getUserInput() { return input.poll(); }
        @Override public void close() { }
        @Override public void renderMap(char[][] mapGrid) { }
    }
}
