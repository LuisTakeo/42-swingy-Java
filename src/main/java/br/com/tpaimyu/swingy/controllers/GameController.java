package br.com.tpaimyu.swingy.controllers;

import br.com.tpaimyu.swingy.models.Hero;
import br.com.tpaimyu.swingy.views.ConsoleView;
import br.com.tpaimyu.swingy.views.GameView;
import br.com.tpaimyu.swingy.views.GuiView;

public class GameController {
    private GameView view;
    private boolean isRunning;
    private GameState currentState;
    private int gameTurn;

    private Hero playerHero; 
    private MapController mapController;

    public GameController(GameView view) {
        this.view = view;
        this.isRunning = true; // Garante que o loop inicie
        this.currentState = GameState.GAME_MENU; // O jogo sempre começa no menu
        this.mapController = new MapController();
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
            // 1. RENDERIZA A TELA DEPENDENDO DO ESTADO
            renderCurrentState();
            
            // 2. LÊ O INPUT
            String input = view.getUserInput();
            
            // 3. VAI PARA A VALIDAÇÃO CORRETA DEPENDENDO DO ESTADO
            routeInput(input);
        }

        view.showMessage("Obrigado por jogar Swingy!");
        view.close();
    }

    // --- MÉTODOS DE ROTEAMENTO (O MAESTRO) ---

    private void renderCurrentState() {
        switch (currentState) {
            case GAME_MENU:
                renderMainMenu();
                break;
            case GAME_PLAYING_MAP:
                renderMapState();
                break;
            default:
                break;
        }
    }

    private void routeInput(String input) {
        String command = input.trim().toLowerCase();

        if (handleGlobalCommand(command)) {
            return;
        }

        switch (currentState) {
            case GAME_MENU:
                handleMenuInput(command);
                break;
            case GAME_PLAYING_MAP:
                handleMapInput(command);
                break;
            default:
                view.showMessage("Estado de jogo não suportado.");
                break;
        }
    }

    private boolean handleGlobalCommand(String command) {
        switch (command) {
            case "exit":
                isRunning = false;
                return true;
            case "switch":
                switchView();
                return true;
            default:
                return false;
        }
    }

    private void renderMainMenu() {
        view.showMessage("\n--- MENU PRINCIPAL ---\n[1] Criar Herói\n[2] Carregar Herói\n[switch] Trocar Tela\n[exit] Sair");
    }

    private void renderMapState() {
        gameTurn++;
        view.showMessage("\n--- MAPA ---\nPara onde ir? (North, South, East, West)\n[switch] Trocar Tela\n[exit] Sair");
    }

    private void handleMenuInput(String command) {
        switch (command) {
            case "1":
                createHeroAndStartGame();
                break;
            case "2":
                loadHero();
                break;
            default:
                view.showMessage("Comando inválido para o menu.");
                break;
        }
    }

    private void createHeroAndStartGame() {
        view.showMessage("Iniciando criação de herói...");
        playerHero = new HeroController().startHeroCreation(view);
        mapController.initializeMap(playerHero.getLevel());
        renderMap();
        currentState = GameState.GAME_PLAYING_MAP;
    }

    private void loadHero() {
        view.showMessage("Carregando jogo... (Em breve)");
    }

    private void handleMapInput(String command) {
        if (!isMovementCommand(command)) {
            view.showMessage("Direção inválida. Use north, south, east ou west.");
            return;
        }

        boolean wonTheMap = mapController.moveHero(command);
        renderMap();

        if (wonTheMap) {
            view.showMessage("\n🎉 PARABÉNS! Você chegou à borda do mapa e sobreviveu!");
            currentState = GameState.GAME_MENU;
            return;
        }

        view.showMessage("Você avançou para " + command + ".");
        showHeroPosition();
    }

    private boolean isMovementCommand(String command) {
        return command.equals("north") || command.equals("south")
                || command.equals("east") || command.equals("west");
    }

    private void showHeroPosition() {
        view.showMessage("Sua posição agora é: X=" + mapController.getHeroPosition().x()
                + " | Y=" + mapController.getHeroPosition().y());
    }

    private void renderMap() {
        view.renderMap(mapController.generateMapGrid());
    }
}
