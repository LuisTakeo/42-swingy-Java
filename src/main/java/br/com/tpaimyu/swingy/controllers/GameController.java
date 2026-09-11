package br.com.tpaimyu.swingy.controllers;

import br.com.tpaimyu.swingy.models.Hero;
import br.com.tpaimyu.swingy.states.GameState;
import br.com.tpaimyu.swingy.states.MenuState;
import br.com.tpaimyu.swingy.views.ConsoleView;
import br.com.tpaimyu.swingy.views.GameView;
import br.com.tpaimyu.swingy.views.GuiView;

public class GameController {
    private GameView view;
    private boolean isRunning;
    private GameState currentState; // Referência para o Estado Atual
    private int gameTurn;

    private Hero playerHero; 
    private MapController mapController;

    public GameController(GameView view) {
        this.view = view;
        this.isRunning = true;
        this.mapController = new MapController();
        this.currentState = new MenuState(); // O jogo começa no Menu
    }

    public void changeState(GameState newState) {
        this.currentState = newState;
    }

    public void switchView() {
        this.view.close();
        this.view = this.view instanceof ConsoleView
                ? new GuiView()
                : new ConsoleView();
        this.view.start();
    }

    public void run() {
        view.start();

        while (isRunning) {
            // 1. Renderiza a tela do estado atual
            currentState.render(this);
            
            // 2. Lê o input do usuário
            String input = view.getUserInput().trim().toLowerCase();
            
            // 3. Comandos Globais
            if (input.equals("exit")) {
                this.isRunning = false;
                continue;
            }
            if (input.equals("switch")) {
                switchView();
                continue;
            }

            // 4. Delega a lógica de input para o estado atual
            currentState.handleInput(this, input);
        }

        view.showMessage("Obrigado por jogar Swingy!");
        view.close();
    }

    // --- GETTERS E SETTERS ---
    public GameView getView() { return view; }
    public MapController getMapController() { return mapController; }
    public Hero getPlayerHero() { return playerHero; }
    public void setPlayerHero(Hero hero) { this.playerHero = hero; }
    public void incrementTurn() { this.gameTurn++; }
}