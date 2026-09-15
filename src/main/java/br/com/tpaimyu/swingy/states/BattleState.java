package br.com.tpaimyu.swingy.states;

import br.com.tpaimyu.swingy.controllers.BattleController;
import br.com.tpaimyu.swingy.controllers.BattleResult;
import br.com.tpaimyu.swingy.controllers.GameController;
import br.com.tpaimyu.swingy.models.Villain;

public class BattleState implements GameState {

    private final Villain villain;
    private final BattleController battleController = new BattleController();

    public BattleState(Villain villain) {
        this.villain = villain;
    }

    @Override
    public void render(GameController context) {
        context.getView().showMessage(
                "\n⚔️ Um " + villain.getName() + " bloqueia seu caminho!"
                        + "\n[1] Fight\n[2] Run"
        );
    }

    @Override
    public void handleInput(GameController context, String input) {
        switch (input) {
            case "1":
                handleFight(context);
                break;
            case "2":
                handleEscape(context);
                break;
            default:
                context.getView().showMessage("Choose 1 to fight or 2 to run.");
                break;
        }
    }

    private void handleFight(GameController context) {
        BattleResult result = battleController.fight(
                context.getView(), context.getPlayerHero(), villain);

        if (result == BattleResult.VICTORY) {
            context.getMapController().removeVillain(villain);
            context.saveHero();
            context.changeState(new MapState());
        } else {
            context.stop();
        }
    }

    private void handleEscape(GameController context) {
        if (battleController.tryToEscape(context.getView())) {
            context.getMapController().restorePreviousHeroPosition();
            context.changeState(new MapState());
        } else {
            handleFight(context);
        }
    }
}