package br.com.tpaimyu.swingy.utils;

import java.net.URL;

import javax.swing.ImageIcon;

public class AssetManager {
    // As instâncias únicas (Flyweights)
    private static ImageIcon grassIcon;
    private static ImageIcon heroIcon;
    private static ImageIcon villainIcon;
    private static ImageIcon waterIcon;

    // Construtor privado para impedir que instanciem a classe
    private AssetManager() {}

    public static ImageIcon getGrassIcon() {
        if (grassIcon == null) grassIcon = loadIcon("/assets/floor.png");
        return grassIcon;
    }

    public static ImageIcon getHeroIcon() {
        if (heroIcon == null) heroIcon = loadIcon("/assets/hero.png");
        return heroIcon;
    }

    public static ImageIcon getVillainIcon() {
        if (villainIcon == null) villainIcon = loadIcon("/assets/villain.png");
        return villainIcon;
    }

    public static ImageIcon getWaterIcon() {
        if (waterIcon == null) waterIcon = loadIcon("/assets/water.png");
        return waterIcon;
    }

    private static ImageIcon loadIcon(String path) {
        URL url = AssetManager.class.getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        }
        System.err.println("Imagem não encontrada: " + path);
        return null; 
    }
}