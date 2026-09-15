package br.com.tpaimyu.swingy.controllers;

import java.util.Random;

import br.com.tpaimyu.swingy.models.Artifact;
import br.com.tpaimyu.swingy.models.ArtifactFactory;
import br.com.tpaimyu.swingy.models.Hero;
import br.com.tpaimyu.swingy.models.Villain;
import br.com.tpaimyu.swingy.views.GameView;

public class BattleController {

    private final Random random;

    public BattleController() {
        this(new Random());
    }

    BattleController(Random random) {
        this.random = random;
    }

    // Método principal que o GameController chama
    public BattleResult startEncounter(GameView view, Hero hero, Villain villain) {
        view.showMessage("\n⚔️ Um " + villain.getName() + " hostil bloqueia seu caminho!");
        
        while (true) {
            view.showMessage("\nWhat do you want to do?\n[1] Fight\n[2] Run");
            String choice = view.getUserInput().trim();

            if (choice.equals("1")) {
                return executeBattle(view, hero, villain);
            } 
            else if (choice.equals("2")) {
                if (tryToRun(view)) {
                    return BattleResult.ESCAPE;
                } else {
                    // Falhou na fuga, é forçado a lutar!
                    return executeBattle(view, hero, villain);
                }
            } 
            else {
                view.showMessage("Invalid battle command!");
            }
        }
    }

    public BattleResult fight(GameView view, Hero hero, Villain villain) {
        return executeBattle(view, hero, villain);
    }

    public boolean tryToEscape(GameView view) {
        return tryToRun(view);
    }

    // --- REGRA DE FUGA (50% de chance) ---
    private boolean tryToRun(GameView view) {
        view.showMessage("You try to run...");
        boolean success = random.nextBoolean(); // Retorna true ou false (50/50)
        
        if (success) {
            view.showMessage("🏃 You barely escaped!");
            return true;
        } else {
            view.showMessage("❌ The monster blocked your escape! Get ready!");
            return false;
        }
    }

    private BattleResult executeBattle(GameView view, Hero hero, Villain villain) {
        view.showMessage("\n--- BATTLE START ---");

        while (hero.getHitPoints() > 0 && villain.getHitPoints() > 0) {
            
            int heroDmg = this.calculateDamage(
                hero.getAttack(), villain.getDefense());
            villain.takeDamage(heroDmg);
            view.showMessage("You attacked for " + heroDmg + " damage! (Monster: " + villain.getHitPoints() + " HP)");

            if (villain.getHitPoints() <= 0) break; 

            int villainDmg = calculateDamage(villain.getAttack(), hero.getDefense());
            hero.takeDamage(villainDmg); // Precisa ter esse método no Character!
            view.showMessage("The " + villain.getName() + " struck back for " + villainDmg + " damage! (Your HP: " + hero.getHitPoints() + ")");
        }

        // Verifica quem ficou de pé
        if (hero.getHitPoints() > 0) {
            return handleVictory(view, hero, villain);
        } else {
            view.showMessage("\n☠️ You were defeated in battle...");
            return BattleResult.DEFEAT;
        }
    }

    private int calculateDamage(int attack, int defense) {
        int baseDamage = Math.max(0, attack - defense);
        int luck = random.nextInt(8) - 2; 
        int finalDamage = Math.max(1, baseDamage + luck); // Garante que tire no mínimo 1 de dano
        return finalDamage;
    }

    private BattleResult handleVictory(GameView view, Hero hero, Villain villain) {
        view.showMessage("\n🏆 VICTORY! The monster fell.");
        
        int xpGained = villain.getLevel() * 500;
        view.showMessage("You gained " + xpGained + " XP!");
        int previousLevel = hero.getLevel();
        hero.addExperience(xpGained);

        if (hero.getLevel() > previousLevel) {
            view.showMessage("\nLEVEL UP! You reached level " + hero.getLevel() + "!");
            showHeroStats(view, hero);
        }

        if (random.nextInt(100) < 40) {
            Artifact loot = ArtifactFactory.generateLoot(villain.getLevel());
            handleLootDrop(view, hero, loot);
        }

        return BattleResult.VICTORY;
    }

    private void showHeroStats(GameView view, Hero hero) {
        view.showMessage("Current status:"
            + "\nLevel: " + hero.getLevel()
                + "\nXP: " + hero.getExperience()
                + "\nAtaque: " + hero.getAttack()
                + "\nDefesa: " + hero.getDefense()
                + "\nHP: " + hero.getHitPoints());
    }

    private void handleLootDrop(GameView view, Hero hero, Artifact loot) {
        view.showMessage("\n✨ The monster dropped an item!");
        view.showMessage("Item: " + loot.getName() + " (+ " + loot.getBonus() + " " + loot.getType() + ")");
        view.showMessage("Equip this item? [Y/N]");

        while (true) {
            String input = view.getUserInput().trim().toLowerCase();
            if (input.equals("y")) {
                switch (loot.getType()) {
                    case WEAPON: hero.setWeapon(loot); break;
                    case ARMOR: hero.setArmor(loot); break;
                    case HELMET: hero.setHelmet(loot); break;
                }
                view.showMessage("Item equipped successfully!");
                break;
            } else if (input.equals("n")) {
                view.showMessage("You left the item on the ground.");
                break;
            }
        }
    }
}