package br.com.tpaimyu.swingy.states;

import br.com.tpaimyu.swingy.controllers.GameController;

public class MapState implements GameState {

    @Override
    public void render(GameController context) {
        context.incrementTurn();
        context.getView().showMessage("\n--- MAPA ---\nPara onde ir? (North, South, East, West)\n[switch] Trocar Tela\n[exit] Sair");
        
        // Desenha o Grid visual na GUI ou no Console
        context.getView().renderMap(context.getMapController().generateMapGrid());
    }

    @Override
    public void handleInput(GameController context, String input) {
        if (input.equals("north") || input.equals("south") || 
            input.equals("east") || input.equals("west")) {
            
            boolean wonTheMap = context.getMapController().moveHero(input);

            context.getView().renderMap(
                    context.getMapController().generateMapGrid()
            );

            if (wonTheMap) {
                context.getView().showMessage(
                        "\n🎉 PARABÉNS! Você chegou à borda do mapa e sobreviveu!"
                );
                context.changeState(new MenuState());
            } else {
                context.getView().showMessage("Você avançou para " + input + ".");
                context.getView().showMessage(
                        "Sua posição agora é: X="
                        + context.getMapController().getHeroPosition().x()
                        + " | Y="
                        + context.getMapController().getHeroPosition().y()
                );
            }
                        
        } else {
            context.getView().showMessage("Direção inválida. Use north, south, east ou west.");
        }
    }
}