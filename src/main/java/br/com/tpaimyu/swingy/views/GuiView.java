package br.com.tpaimyu.swingy.views;

import java.awt.BorderLayout; // IMPORTANTE: Ajuste se o pacote for diferente
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;

import br.com.tpaimyu.swingy.utils.AssetManager;

public class GuiView extends JFrame implements GameView {

    private JTextArea textArea;
    private JPanel buttonPanel;
    private final BlockingQueue<String> inputQueue;
    private JTextField textField;
    private JPanel mapPanel;
    private boolean mapInputEnabled;
    private int renderedMapSize;
    private JLabel[][] mapCells;
    private char[][] lastMapGrid;

    public GuiView() {
        this.inputQueue = new LinkedBlockingQueue<>();
        setupWindow();
    }

    private void setupWindow() {
        setTitle("Swingy");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new BorderLayout());

        mapPanel = new FixedMapPanel();
        centerPanel.add(new JScrollPane(mapPanel), BorderLayout.CENTER);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 150));
        centerPanel.add(scrollPane, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // ATIVANDO OS BINDINGS DE TECLADO AQUI!
        setupKeyBindings(mapPanel); 
    }

    @Override
    public void start() {
        setVisible(true);
        showMessage("Welcome to Swingy RPG!!!");
    }

    @Override
    public void showMessage(String message) {
        textArea.append(message + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
        this.updateButtonsBasedOnContext(message);
    }

    @Override
    public void renderMap(char[][] mapGrid) {
        SwingUtilities.invokeLater(() -> renderMapOnEdt(mapGrid));
    }

    private void renderMapOnEdt(char[][] mapGrid) {
        int size = mapGrid.length;
        int cellSize = 64;

        if (mapCells != null && mapCells.length == size) {
            updateChangedCells(mapGrid, cellSize);
            return;
        }

        mapPanel.removeAll();
        mapPanel.setLayout(new GridLayout(size, size, 0, 0));
        mapPanel.setBackground(Color.BLACK);
        mapPanel.setPreferredSize(new Dimension(size * cellSize, size * cellSize));
        resizeWindowForMap(size, cellSize);

        ImageIcon water = AssetManager.getWaterIcon();
        ImageIcon grass = AssetManager.getGrassIcon();
        ImageIcon hero = AssetManager.getHeroIcon();
        ImageIcon villain = AssetManager.getVillainIcon();
        mapCells = new JLabel[size][size];

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                JLabel cell = createMapCell(mapGrid[y][x], cellSize, water, grass, hero, villain);
                mapCells[y][x] = cell;
                mapPanel.add(cell);
            }
        }

        lastMapGrid = copyGrid(mapGrid);

        mapPanel.revalidate();
        mapPanel.repaint();
    }

    private void updateChangedCells(char[][] mapGrid, int cellSize) {
        ImageIcon water = AssetManager.getWaterIcon();
        ImageIcon grass = AssetManager.getGrassIcon();
        ImageIcon hero = AssetManager.getHeroIcon();
        ImageIcon villain = AssetManager.getVillainIcon();

        for (int y = 0; y < mapGrid.length; y++) {
            for (int x = 0; x < mapGrid[y].length; x++) {
                if (mapGrid[y][x] != lastMapGrid[y][x]) {
                    mapCells[y][x].setIcon(createMapIcon(
                            mapGrid[y][x], cellSize, water, grass, hero, villain));
                }
            }
        }

        lastMapGrid = copyGrid(mapGrid);
        mapPanel.repaint();
    }

    private void resizeWindowForMap(int mapSize, int cellSize) {
        if (renderedMapSize == mapSize) {
            return;
        }

        renderedMapSize = mapSize;

        Insets windowInsets = getInsets();
        int mapWidth = mapSize * cellSize;
        int controlsHeight = 230;
        int requestedWidth = mapWidth + windowInsets.left + windowInsets.right;
        int requestedHeight = mapWidth + controlsHeight
                + windowInsets.top + windowInsets.bottom;

        var screen = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();

        int width = Math.min(requestedWidth, screen.width);
        int height = Math.min(requestedHeight, screen.height);

        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private JLabel createMapCell(
            char cellValue,
            int cellSize,
            ImageIcon water,
            ImageIcon grass,
            ImageIcon hero,
            ImageIcon villain) {
        JLabel cell = new JLabel();
        cell.setOpaque(true);
        cell.setPreferredSize(new Dimension(cellSize, cellSize));
        cell.setMinimumSize(new Dimension(cellSize, cellSize));
        cell.setMaximumSize(new Dimension(cellSize, cellSize));
        cell.setHorizontalAlignment(JLabel.CENTER);
        cell.setVerticalAlignment(JLabel.CENTER);
        cell.setBorder(null);
        cell.setIcon(createMapIcon(cellValue, cellSize, water, grass, hero, villain));

        return cell;
    }

    private ImageIcon createMapIcon(
            char cellValue,
            int cellSize,
            ImageIcon water,
            ImageIcon grass,
            ImageIcon hero,
            ImageIcon villain) {
        return switch (cellValue) {
            case 'H' -> combineImages(cellSize, water, grass, hero);
            case 'V' -> combineImages(cellSize, water, grass, villain);
            default -> combineImages(cellSize, water, grass);
        };
    }

    private char[][] copyGrid(char[][] source) {
        char[][] copy = new char[source.length][];
        for (int y = 0; y < source.length; y++) {
            copy[y] = source[y].clone();
        }
        return copy;
    }

    // Método auxiliar para colar o Herói Transparente em cima da Grama
    // O novo método suporta 1, 2, 3 ou 100 camadas de forma automática!
    private ImageIcon combineImages(int size, ImageIcon... layers) {
        BufferedImage combined = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();

        // O loop desenha a primeira imagem no fundo, a segunda por cima da primeira, etc.
        for (ImageIcon layer : layers) {
            if (layer != null) {
                g.drawImage(layer.getImage(), 0, 0, size, size, null);
            }
        }

        g.dispose();
        return new ImageIcon(combined);
    }

    @Override
    public String getUserInput() {
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            return "exit";
        }
    }

    @Override
    public void close() {
        dispose();
    }

    private void updateButtonsBasedOnContext(String message) {
        if (message.contains("MENU PRINCIPAL")) {
            mapInputEnabled = false;
            renderMainMenu();
        } 
        else if (message.contains("MAPA")) {
            mapInputEnabled = true;
            renderMapControls();
        }
        else if (message.contains("Digite") || message.contains("Escolha")) {
            mapInputEnabled = false;
            renderTextInput();
        }
    }

    private void renderMainMenu() {
        buttonPanel.removeAll();
        buttonPanel.setLayout(new FlowLayout());
        
        // APAGA O MAPA FANTASMA QUANDO VOLTA PRO MENU
        if (mapPanel != null) {
            mapPanel.removeAll();
            mapPanel.revalidate();
            mapPanel.repaint();
        }
        
        addButton("Criar Herói", "1");
        addButton("Carregar Herói", "2");
        addButton("Trocar Tela", "switch");
        addButton("Sair", "exit");
        
        refreshPanel();
    }

    private void renderMapControls() {
        buttonPanel.removeAll();
        buttonPanel.setLayout(new GridLayout(2, 4));
        
        buttonPanel.add(new JLabel(""));
        addButton("Norte", "north");
        buttonPanel.add(new JLabel(""));
        addButton("Trocar Tela", "switch");
        
        addButton("Oeste", "west");
        addButton("Sul", "south");
        addButton("Leste", "east");
        addButton("Sair", "exit");
        
        refreshPanel();
    }

    private void renderTextInput() {
        buttonPanel.removeAll();
        buttonPanel.setLayout(new FlowLayout());
        
        textField = new JTextField(20);
        buttonPanel.add(textField);
        
        JButton submitButton = new JButton("Enviar");
        submitButton.addActionListener(e -> {
            try {
                inputQueue.put(textField.getText());
                textField.setText("");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        
        buttonPanel.add(submitButton);
        refreshPanel();
    }

    private void addButton(String label, String command) {
        JButton button = new JButton(label);
        button.addActionListener(e -> {
            try {
                inputQueue.put(command);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        buttonPanel.add(button);
    }

    private void refreshPanel() {
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private static class FixedMapPanel extends JPanel implements Scrollable {
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return new Dimension(640, 640);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return false;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

        @Override
        public int getScrollableUnitIncrement(
                java.awt.Rectangle visibleRect,
                int orientation,
                int direction) {
            return 64;
        }

        @Override
        public int getScrollableBlockIncrement(
                java.awt.Rectangle visibleRect,
                int orientation,
                int direction) {
            return 64 * 4;
        }
    }

    private void setupKeyBindings(JPanel panel) {
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("W"), "moveNorth");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "moveNorth");
        
        inputMap.put(KeyStroke.getKeyStroke("S"), "moveSouth");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "moveSouth");
        
        inputMap.put(KeyStroke.getKeyStroke("D"), "moveEast");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveEast");
        
        inputMap.put(KeyStroke.getKeyStroke("A"), "moveWest");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveWest");

        actionMap.put("moveNorth", createMoveAction("north"));
        actionMap.put("moveSouth", createMoveAction("south"));
        actionMap.put("moveEast", createMoveAction("east"));
        actionMap.put("moveWest", createMoveAction("west"));
    }

    private AbstractAction createMoveAction(String direction) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!mapInputEnabled) {
                    return;
                }
                try {
                    inputQueue.put(direction); 
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
}