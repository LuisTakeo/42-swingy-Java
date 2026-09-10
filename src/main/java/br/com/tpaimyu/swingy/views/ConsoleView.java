package br.com.tpaimyu.swingy.views;

import java.util.Scanner;

public class ConsoleView implements GameView {
    
    private static final Scanner SCANNER = new Scanner(System.in);

    @Override
    public void start() {
        System.out.println("Starting the game in console mode...");
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void renderMap(char[][] mapGrid) {
        System.out.println();
        for (int y = 0; y < mapGrid.length; y++) {
            for (int x = 0; x < mapGrid[y].length; x++) {
                System.out.print(mapGrid[y][x] + " ");
            }
            System.out.println(); 
        }
        System.out.println();
    }

    @Override
    public String getUserInput() {
        if (SCANNER.hasNextLine()) {
            return SCANNER.nextLine();
        }
        return "exit";
    }


    @Override 
    public void close() {
        System.out.println("Closing the game...");
    }
}