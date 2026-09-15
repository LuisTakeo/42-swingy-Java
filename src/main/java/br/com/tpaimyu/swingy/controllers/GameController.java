package br.com.tpaimyu.swingy.controllers;

import java.io.IOException;

import br.com.tpaimyu.swingy.models.Hero;
import br.com.tpaimyu.swingy.persistence.HeroRepository;
import br.com.tpaimyu.swingy.persistence.HeroStore;
import br.com.tpaimyu.swingy.persistence.JdbcHeroRepository;
import br.com.tpaimyu.swingy.states.GameState;
import br.com.tpaimyu.swingy.states.HeroLoadState;
import br.com.tpaimyu.swingy.states.MapState;
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
    private final HeroStore heroRepository;

    public GameController(GameView view) {
        this.view = view;
        this.isRunning = true;
        this.mapController = new MapController();
        this.heroRepository = createHeroStore();
        this.currentState = new MenuState(); // O jogo começa no Menu
        this.view.setCloseHandler(this::stop);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> view.close()));
    }

    public void changeState(GameState newState) {
        this.currentState = newState;
    }

    public void stop() {
        this.isRunning = false;
    }

    public void saveHero() {
        if (playerHero == null) {
            return;
        }
        try {
            heroRepository.save(playerHero);
            view.showMessage("Progress saved.");
        } catch (IOException exception) {
            view.showMessage("Could not save the hero.");
        }
    }

    public boolean loadHero() {
        try {
            var savedHero = heroRepository.load();
            if (savedHero.isEmpty()) {
                view.showMessage("No saved hero was found.");
                return false;
            }
            playerHero = savedHero.get();
            mapController.initializeMap(playerHero.getLevel());
            showLoadedHero(playerHero);
            changeState(new MapState());
            return true;
        } catch (IOException | RuntimeException exception) {
            view.showMessage("Could not load the saved hero.");
            return false;
        }
    }

    public void openHeroLoadState() {
        changeState(new HeroLoadState());
    }

    public void loadHero(int index) throws IOException {
        var savedHero = heroRepository.load(index);
        if (savedHero.isEmpty()) {
            view.showMessage("Could not load the selected hero.");
            changeState(new MenuState());
            return;
        }

        playerHero = savedHero.get();
        mapController.initializeMap(playerHero.getLevel());
        showLoadedHero(playerHero);
        changeState(new MapState());
    }

    private void showLoadedHero(Hero hero) {
        view.showMessage("\nHero " + hero.getName() + " loaded successfully!");
        view.showMessage("Restored status:"
            + "\nClass: " + hero.getHeroClass()
            + "\nLevel: " + hero.getLevel()
                + "\nXP: " + hero.getExperience()
                + "\nAtaque: " + hero.getAttack()
                + "\nDefesa: " + hero.getDefense()
                + "\nHP: " + hero.getHitPoints());
    }

    public HeroStore getHeroRepository() {
        return heroRepository;
    }

    private HeroStore createHeroStore() {
        if (!"db".equalsIgnoreCase(System.getenv("SWINGY_PERSISTENCE"))) {
            return new HeroRepository();
        }
        try {
            return new JdbcHeroRepository(
                    System.getenv().getOrDefault("SWINGY_DB_URL", "jdbc:postgresql://localhost:5432/swingy"),
                    System.getenv().getOrDefault("SWINGY_DB_USER", "swingy"),
                    System.getenv().getOrDefault("SWINGY_DB_PASSWORD", "swingy"));
        } catch (IOException exception) {
            view.showMessage("Database unavailable; using file persistence.");
            return new HeroRepository();
        }
    }

    public void finishMapAndReturnToMenu() {
        saveHero();
        changeState(new MenuState());
    }

    public void switchView() {
        this.view.close();
        this.view = this.view instanceof ConsoleView
                ? new GuiView()
                : new ConsoleView();
        this.view.setCloseHandler(this::stop);
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

        view.showMessage("Thanks for playing Swingy!");
        view.close();
    }

    // --- GETTERS E SETTERS ---
    public GameView getView() { return view; }
    public MapController getMapController() { return mapController; }
    public Hero getPlayerHero() { return playerHero; }
    public void setPlayerHero(Hero hero) { this.playerHero = hero; }
    public void incrementTurn() { this.gameTurn++; }
}