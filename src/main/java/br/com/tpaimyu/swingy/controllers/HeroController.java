package br.com.tpaimyu.swingy.controllers;

import br.com.tpaimyu.swingy.models.Hero;
import br.com.tpaimyu.swingy.views.GameView;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

public class HeroController {

    public Hero startHeroCreation(GameView view) {
        view.showMessage("\n=== CRIAÇÃO DE HERÓI ===");
        
        while (true) {
            try {
                view.showMessage("Digite o nome do seu Herói:");
                String name = view.getUserInput().trim();

                view.showMessage("Escolha a classe do Herói (ex: Warrior, Mage, Rogue):");
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

                view.showMessage("\n✅ Herói [" + hero.getName() + " - " + hero.getHeroClass() + "] criado com sucesso!");
                return hero; // Sai do loop e devolve o herói

            } catch (ConstraintViolationException e) {
                // Se o Jakarta achar um erro (ex: nome em branco), ele cai aqui!
                view.showMessage("\n❌ Erro na criação do herói. Verifique os dados:");
                
                // Imprime as mensagens de erro que você configurou no modelo
                for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                    view.showMessage(" - " + violation.getMessage());
                }
                
                view.showMessage("\nVamos tentar novamente...\n");
            }
        }
    }
}