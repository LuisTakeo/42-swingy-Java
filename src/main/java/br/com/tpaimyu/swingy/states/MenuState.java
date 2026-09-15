package br.com.tpaimyu.swingy.states;

import br.com.tpaimyu.swingy.controllers.GameController;

public class MenuState implements GameState {

    @Override
    public void render(GameController context) {
        context.getView().showMessage("\n--- MAIN MENU ---\n[1] Create Hero\n[2] Load Hero\n[switch] Switch View\n[exit] Exit");
    }

    @Override
    public void handleInput(GameController context, String input) {
        switch (input) {
            case "1":
                context.changeState(new HeroCreationState()); // Vai para a tela de criação
                break;
            case "2":
                context.openHeroLoadState();
                break;
            default:
                context.getView().showMessage("Invalid menu command.");
                break;
        }
    }
}