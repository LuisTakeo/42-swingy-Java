package br.com.tpaimyu.swingy.states;

import br.com.tpaimyu.swingy.controllers.GameController;
import br.com.tpaimyu.swingy.models.Villain;

public class MapState implements GameState {

    @Override
    public void render(GameController context) {
        context.incrementTurn();
        context.getView().showMessage("\n--- MAP ---\nWhere do you want to go? (North, South, East, West)\n[switch] Switch View\n[exit] Exit");
        
        // Desenha o Grid visual na GUI ou no Console
        context.getView().renderMap(context.getMapController().generateMapGrid());
    }

    @Override
    public void handleInput(GameController context, String input) {
        if (input.equals("north") || input.equals("south") || 
            input.equals("east") || input.equals("west")) {
            
            boolean wonTheMap = context.getMapController().moveHero(input);
                Villain encounteredVillain = context.getMapController().getEncounteredVillain();

            context.getView().renderMap(
                    context.getMapController().generateMapGrid()
            );

                if (encounteredVillain != null) {
                    context.changeState(new BattleState(encounteredVillain));
                } else if (wonTheMap) {
                context.getView().showMessage(
                        "\n🎉 CONGRATULATIONS! You reached the map edge and survived!"
                );
                context.finishMapAndReturnToMenu();
            } else {
                context.getView().showMessage("You moved " + input + ".");
                context.getView().showMessage(
                        "Your current position is: X="
                        + context.getMapController().getHeroPosition().x()
                        + " | Y="
                        + context.getMapController().getHeroPosition().y()
                );
            }
                        
        } else {
            context.getView().showMessage("Invalid direction. Use north, south, east or west.");
        }
    }
}