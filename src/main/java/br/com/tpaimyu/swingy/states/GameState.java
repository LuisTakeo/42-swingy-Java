package br.com.tpaimyu.swingy.states;

import br.com.tpaimyu.swingy.controllers.GameController;

public interface GameState {
    void render(GameController context);
    void handleInput(GameController context, String input);
}