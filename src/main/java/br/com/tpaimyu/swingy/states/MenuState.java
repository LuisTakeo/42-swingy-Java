package br.com.tpaimyu.swingy.states;

import br.com.tpaimyu.swingy.controllers.GameController;

public class MenuState implements GameState {

    @Override
    public void render(GameController context) {
        context.getView().showMessage("\n--- MENU PRINCIPAL ---\n[1] Criar Herói\n[2] Carregar Herói\n[switch] Trocar Tela\n[exit] Sair");
    }

    @Override
    public void handleInput(GameController context, String input) {
        switch (input) {
            case "1":
                context.changeState(new HeroCreationState()); // Vai para a tela de criação
                break;
            case "2":
                context.getView().showMessage("Carregando jogo... (Em breve)");
                break;
            default:
                context.getView().showMessage("Comando inválido para o menu.");
                break;
        }
    }
}