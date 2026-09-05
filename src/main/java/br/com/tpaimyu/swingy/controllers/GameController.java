package br.com.tpaimyu.swingy.controllers;

import br.com.tpaimyu.swingy.views.ConsoleView;
import br.com.tpaimyu.swingy.views.GameView;
import br.com.tpaimyu.swingy.views.GuiView;

public class GameController {
    private GameView view;

    public GameController(GameView view) {
        this.view = view;
    }

    public void switchView() {
        this.view.hide(); 
        
        // Se a view atual for de console, instanciamos a GUI, e vice-versa
        if (this.view instanceof ConsoleView) {
            this.view = new GuiView();
        } else {
            this.view = new ConsoleView();
        }
        
        this.view.start();
    }

    public void run() {
        view.start();
        view.showMessage("Press [1] to create a Hero, "
         + "[2] for select a existing one and [3] to exit.");
        
        String input = view.getUserInput();
        
        if (input.equals("1")) {
            view.showMessage("Starting Hero creation...");
            // Aqui você chamaria o HeroController, por exemplo.
        }
         else if (input.equals("2")) {
            view.showMessage("Selecting existing Hero...");
            // Aqui você chamaria o HeroController, por exemplo.
        } else {
            view.showMessage("Saindo do jogo...");
        }
        
        view.close();
    }
}
