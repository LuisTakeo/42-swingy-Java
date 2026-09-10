package br.com.tpaimyu.swingy.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GuiView extends JFrame implements GameView {

    private JTextArea textArea;
    private JPanel buttonPanel;
    private BlockingQueue<String> inputQueue;
    private JTextField textField;
    private JPanel mapPanel;

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

        mapPanel = new JPanel();
        centerPanel.add(mapPanel, BorderLayout.CENTER);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 150));
        centerPanel.add(scrollPane, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);
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
        int size = mapGrid.length;
        
        mapPanel.removeAll();
        mapPanel.setLayout(new GridLayout(size, size, 2, 2)); 
        mapPanel.setBackground(Color.BLACK); 

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                JPanel cell = new JPanel();
                
                if (mapGrid[y][x] == 'H') {
                    cell.setBackground(Color.BLUE);
                } else if (mapGrid[y][x] == 'V') {
                    cell.setBackground(Color.RED);
                } else {
                    cell.setBackground(Color.WHITE);
                }
                
                mapPanel.add(cell);
            }
        }
        
        mapPanel.revalidate();
        mapPanel.repaint();
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
            renderMainMenu();
        } 
        else if (message.contains("MAPA")) {
            renderMapControls();
        }
        else if (message.contains("Digite") || message.contains("Escolha")) {
            renderTextInput();
        }
    }

    private void renderMainMenu() {
        buttonPanel.removeAll();
        buttonPanel.setLayout(new FlowLayout());
        
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
}