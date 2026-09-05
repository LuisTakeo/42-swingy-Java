package br.com.tpaimyu.swingy;

import br.com.tpaimyu.swingy.controllers.GameController;
import br.com.tpaimyu.swingy.views.ConsoleView;
import br.com.tpaimyu.swingy.views.GameView;
import br.com.tpaimyu.swingy.views.GuiView;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Error: You must start the game with 'console' or 'gui'.");
            System.out.println("Usage: java -jar swingy.jar [console|gui]");
            System.exit(1); // Encerra o programa com erro
        }

        String mode = args[0].toLowerCase();
        GameView view = null;

        // 2. Decide qual View instanciar com base no argumento
        if (mode.equals("console")) {
            view = new ConsoleView();
        } else if (mode.equals("gui")) {
            view = new GuiView();
        } else {
            System.out.println("Error: Mode invalid. Choose 'console' or 'gui'.");
            System.exit(1);
        }

        // 3. Injeta a View escolhida no Controller e roda o jogo!
        GameController gameController = new GameController(view);
        gameController.run();
    }
}
