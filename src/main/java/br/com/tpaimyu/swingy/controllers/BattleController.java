package br.com.tpaimyu.swingy.controllers;

import java.util.Random;

import br.com.tpaimyu.swingy.models.Artifact;
import br.com.tpaimyu.swingy.models.ArtifactFactory;
import br.com.tpaimyu.swingy.models.Hero;
import br.com.tpaimyu.swingy.models.Villain;
import br.com.tpaimyu.swingy.views.GameView;

public class BattleController {

    private final Random random = new Random();

    // Método principal que o GameController chama
    public BattleResult startEncounter(GameView view, Hero hero, Villain villain) {
        view.showMessage("\n⚔️ Um " + villain.getName() + " hostil bloqueia seu caminho!");
        
        while (true) {
            view.showMessage("\nO que você deseja fazer?\n[1] Lutar (Fight)\n[2] Fugir (Run)");
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
                view.showMessage("Comando inválido na tensão da batalha!");
            }
        }
    }

    // --- REGRA DE FUGA (50% de chance) ---
    private boolean tryToRun(GameView view) {
        view.showMessage("Você tenta correr...");
        boolean success = random.nextBoolean(); // Retorna true ou false (50/50)
        
        if (success) {
            view.showMessage("🏃 Você conseguiu escapar por pouco!");
            return true;
        } else {
            view.showMessage("❌ O monstro bloqueou sua rota de fuga! Prepare-se!");
            return false;
        }
    }

    private BattleResult executeBattle(GameView view, Hero hero, Villain villain) {
        view.showMessage("\n--- INÍCIO DA BATALHA ---");

        while (hero.getHitPoints() > 0 && villain.getHitPoints() > 0) {
            
            int heroDmg = this.calculateDamage(
                hero.getAttack(), villain.getDefense());
            villain.takeDamage(heroDmg);
            view.showMessage("Você atacou causando " + heroDmg + " de dano! (Monstro: " + villain.getHitPoints() + " HP)");

            if (villain.getHitPoints() <= 0) break; 

            int villainDmg = calculateDamage(villain.getAttack(), hero.getDefense());
            hero.takeDamage(villainDmg); // Precisa ter esse método no Character!
            view.showMessage("O " + villain.getName() + " revidou causando " + villainDmg + " de dano! (Seu HP: " + hero.getHitPoints() + ")");
        }

        // Verifica quem ficou de pé
        if (hero.getHitPoints() > 0) {
            return handleVictory(view, hero, villain);
        } else {
            view.showMessage("\n☠️ Você foi derrotado na batalha...");
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
        view.showMessage("\n🏆 VITÓRIA! O monstro caiu.");
        
        int xpGained = villain.getLevel() * 500;
        view.showMessage("Você ganhou " + xpGained + " XP!");
        hero.addExperience(xpGained);

        if (random.nextInt(100) < 40) {
            Artifact loot = ArtifactFactory.generateLoot(villain.getLevel());
            handleLootDrop(view, hero, loot);
        }

        return BattleResult.VICTORY;
    }

    private void handleLootDrop(GameView view, Hero hero, Artifact loot) {
        view.showMessage("\n✨ O monstro deixou cair um item!");
        view.showMessage("Item: " + loot.getName() + " (+ " + loot.getBonus() + " " + loot.getType() + ")");
        view.showMessage("Deseja equipar este item? [Y/N]");

        while (true) {
            String input = view.getUserInput().trim().toLowerCase();
            if (input.equals("y")) {
                switch (loot.getType()) {
                    case WEAPON: hero.setWeapon(loot); break;
                    case ARMOR: hero.setArmor(loot); break;
                    case HELMET: hero.setHelmet(loot); break;
                }
                view.showMessage("Item equipado com sucesso!");
                break;
            } else if (input.equals("n")) {
                view.showMessage("Você deixou o item no chão.");
                break;
            }
        }
    }
}