package br.com.tpaimyu.swingy.views;

import javax.swing.JOptionPane;

public class GuiView implements GameView {


    @Override
    public void start() {
        // Implement GUI start logic here
        this.showMessage("Welcome to Swingy RPG!!!");
    }

    @Override
    public void showMessage(String message) {
        // Implement GUI message display logic here
        JOptionPane.showMessageDialog(null, message);
    }

    @Override
    public String getUserInput() {
        String input = JOptionPane.showInputDialog(
            null,
            "Enter your input:");
        return input;
    }

    @Override
    public void hide() {
        // Implement GUI hide logic here
    }

    @Override
    public void close() {
        // Implement GUI close logic here
        this.hide();
    }

}
