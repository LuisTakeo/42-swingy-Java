package br.com.tpaimyu.swingy.views;

import java.util.Scanner;

public class ConsoleView implements GameView {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void start() {
        System.out.println("Starting the game in console mode...");
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String getUserInput() {
        return scanner.nextLine();
    }

    @Override
    public void hide() {
        System.out.println("Hiding the game...");    
    }

    @Override 
    public void close() {
        System.out.println("Closing the game...");
        scanner.close();
    }
}
