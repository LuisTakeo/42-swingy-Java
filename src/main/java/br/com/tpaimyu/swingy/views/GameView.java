package br.com.tpaimyu.swingy.views;

public interface GameView {
    void start();
    void showMessage(String message);
    String getUserInput();
    void close();

    void renderMap(char[][] mapGrid);
}
