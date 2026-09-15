package br.com.tpaimyu.swingy.states;

import br.com.tpaimyu.swingy.controllers.GameController;
import br.com.tpaimyu.swingy.models.Hero;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

public class HeroCreationState implements GameState {

    private String heroName = null;

    @Override
    public void render(GameController context) {
        if (heroName == null) {
            context.getView().showMessage("\n=== HERO CREATION ===");
            context.getView().showMessage("Enter your hero's name:");
        } else {
            context.getView().showMessage("Choose your hero's class (e.g. Warrior, Mage, Rogue):");
        }
    }

    @Override
    public void handleInput(GameController context, String input) {
        // Passo 1: Pega o nome
        if (heroName == null) {
            this.heroName = input.trim();
            return;
        } 
        
        // Passo 2: Pega a classe e constrói
        String heroClass = input.trim();
        
        try {
            Hero hero = new Hero.HeroBuilder()
                    .setName(heroName)
                    .setHeroClass(heroClass)
                    .setLevel(1)
                    .setAttack(10)
                    .setDefense(10)
                    .setHitPoints(50)
                    .build();

            context.getView().showMessage("\n✅ Hero [" + hero.getName() + " - " + hero.getHeroClass() + "] created successfully!");
            
            // Salva no Maestro e prepara o mapa
            context.setPlayerHero(hero);
            context.getMapController().initializeMap(hero.getLevel());
            context.saveHero();
            
            // Vai para o Mapa!
            context.changeState(new MapState());

        } catch (ConstraintViolationException e) {
            context.getView().showMessage("\n❌ Hero creation failed:");
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                context.getView().showMessage(" - " + violation.getMessage());
            }
            this.heroName = null; // Reseta para tentar de novo
            context.getView().showMessage("\nVamos tentar novamente...\n");
        }
    }
}