package br.com.tpaimyu.swingy.states;

import java.io.IOException;
import java.util.List;

import br.com.tpaimyu.swingy.controllers.GameController;
import br.com.tpaimyu.swingy.models.Hero;

public class HeroLoadState implements GameState {
    private List<Hero> savedHeroes;

    @Override
    public void render(GameController context) {
        try {
            savedHeroes = context.getHeroRepository().loadAll();
        } catch (IOException exception) {
            savedHeroes = List.of();
        }

        if (savedHeroes.isEmpty()) {
            context.getView().showMessage("No saved hero was found.");
            context.changeState(new MenuState());
            return;
        }

        StringBuilder message = new StringBuilder("\n--- SAVED HEROES ---\n");
        for (int index = 0; index < savedHeroes.size(); index++) {
            Hero hero = savedHeroes.get(index);
            message.append('[').append(index + 1).append("] ")
                    .append(hero.getName())
                    .append(" - level ").append(hero.getLevel())
                    .append(" - ").append(hero.getHeroClass()).append('\n');
        }
        message.append("Enter the hero number or [back] to return.");
        context.getView().showMessage(message.toString());
    }

    @Override
    public void handleInput(GameController context, String input) {
        if (input.equals("back")) {
            context.changeState(new MenuState());
            return;
        }

        try {
            int selected = Integer.parseInt(input) - 1;
            if (selected < 0 || selected >= savedHeroes.size()) {
                context.getView().showMessage("Invalid selection.");
                return;
            }
            context.loadHero(selected);
        } catch (NumberFormatException | IOException exception) {
            context.getView().showMessage("Enter a valid number.");
        }
    }
}