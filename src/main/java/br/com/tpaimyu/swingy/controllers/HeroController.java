package br.com.tpaimyu.swingy.controllers;

import br.com.tpaimyu.swingy.models.Hero;
import br.com.tpaimyu.swingy.views.GameView;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

public class HeroController {

    public Hero startHeroCreation(GameView view) {
        view.showMessage("\n=== HERO CREATION ===");
        
        while (true) {
            try {
                view.showMessage("Enter your hero's name:");
                String name = view.getUserInput().trim();

                view.showMessage("Choose your hero's class (e.g. Warrior, Mage, Rogue):");
                String heroClass = view.getUserInput().trim();

                // Tentamos construir o herói. 
                // O método build() vai rodar a validação do Jakarta automaticamente!
                Hero hero = new Hero.HeroBuilder()
                        .setName(name)
                        .setHeroClass(heroClass)
                        .setLevel(1)
                        .setAttack(10)
                        .setDefense(10)
                        .setHitPoints(50)
                        .build();

                view.showMessage("\n✅ Hero [" + hero.getName() + " - " + hero.getHeroClass() + "] created successfully!");
                return hero; // Sai do loop e devolve o herói

            } catch (ConstraintViolationException e) {
                // Se o Jakarta achar um erro (ex: nome em branco), ele cai aqui!
                view.showMessage("\n❌ Hero creation failed. Check your data:");
                
                // Imprime as mensagens de erro que você configurou no modelo
                for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                    view.showMessage(" - " + violation.getMessage());
                }
                
                view.showMessage("\nVamos tentar novamente...\n");
            }
        }
    }
}