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
            context.getView().showMessage("\n=== CRIAÇÃO DE HERÓI ===");
            context.getView().showMessage("Digite o nome do seu Herói:");
        } else {
            context.getView().showMessage("Escolha a classe do Herói (ex: Warrior, Mage, Rogue):");
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

            context.getView().showMessage("\n✅ Herói [" + hero.getName() + " - " + hero.getHeroClass() + "] criado com sucesso!");
            
            // Salva no Maestro e prepara o mapa
            context.setPlayerHero(hero);
            context.getMapController().initializeMap(hero.getLevel());
            
            // Vai para o Mapa!
            context.changeState(new MapState());

        } catch (ConstraintViolationException e) {
            context.getView().showMessage("\n❌ Erro na criação do herói:");
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                context.getView().showMessage(" - " + violation.getMessage());
            }
            this.heroName = null; // Reseta para tentar de novo
            context.getView().showMessage("\nVamos tentar novamente...\n");
        }
    }
}