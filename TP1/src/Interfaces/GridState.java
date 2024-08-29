package Interfaces;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Negocio.GameState;

public class GridState {

    private GameState _gameState;
    private JButton[][] _buttons;
    private int emptyRow, emptyCol; // Posición del casillero vacío

    public GridState() {
        this._gameState = new GameState();
    }

    public void setSelectedLevel(JComboBox comboBox, JPanel panel, JLabel puntosLabel, JLabel recordLabel) {
        this.selectedLevel(comboBox, panel, puntosLabel, recordLabel);
    }

    public void setClickOnStart(JButton btnStart, JPanel panel) {
        this.clickOnStart(btnStart, panel);
    }

    public void setClickOnRestart(JButton btnRestart, JPanel panel) {
        this.clickOnRestart(btnRestart, panel);
    }

  /*  public void setClickOnWildCard(JButton wildCard, JPanel panel) {
        this.clickOnWildCard(wildCard, panel);
    }*/

    private void selectedLevel(JComboBox comboBox, JPanel panel, JLabel puntosLabel, JLabel recordLabel) {
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLevel = (String) comboBox.getSelectedItem();
                if (!selectedLevel.equals("Selecciona un nivel:")) {
                    int level = Integer.parseInt(selectedLevel.substring(0, 1));
                    gridGenerator(panel, level, puntosLabel, recordLabel);
        //            _gameState.setNivelYComodin(level);
                    _gameState.verificarSiExisteRecord();
                }
            }
        });
    }

    private void gridGenerator(JPanel panel, int level, JLabel puntosLabel, JLabel recordLabel) {
        panel.removeAll(); // Limpiamos el panel antes de agregar la nueva grilla
        panel.setLayout(new GridLayout(level, level));

        createGrid(panel, level);
        addMouseListenerToButtons(panel, puntosLabel, recordLabel);

        panel.revalidate(); // Actualizamos la disposición del panel
        panel.setVisible(false);
    }

    private void createGrid(JPanel panel, int level) {
        _buttons = new JButton[level][level];
        int number = 1;

        for (int row = 0; row < level; row++) {
            for (int col = 0; col < level; col++) {
                JButton button = new JButton();
                if (row == level - 1 && col == level - 1) {
                    button.setBackground(Color.WHITE); // Casillero vacío
                    emptyRow = row;
                    emptyCol = col;
                } else {
                    button.setText(String.valueOf(number++));
                    button.setBackground(Color.ORANGE);
                }
                panel.add(button);
                _buttons[row][col] = button; // Asigna el botón a la matriz de botones
            }
        }
    }

    private void addMouseListenerToButtons(JPanel panel, JLabel puntosLabel, JLabel recordLabel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleButtonClick(button, panel, puntosLabel, recordLabel);
                    }
                });
            }
        }
    }

    private void handleButtonClick(JButton clickedButton, JPanel panel, JLabel puntosLabel, JLabel recordLabel) {
        int clickedRow = -1, clickedCol = -1;
        outerloop:
        for (int row = 0; row < _buttons.length; row++) {
            for (int col = 0; col < _buttons[row].length; col++) {
                if (_buttons[row][col] == clickedButton) {
                    clickedRow = row;
                    clickedCol = col;
                    break outerloop;
                }
            }
        }

        // Verifica si el casillero clickeado es adyacente al casillero vacío
        if (isAdjacentToEmpty(clickedRow, clickedCol)) {
            swapWithEmpty(clickedRow, clickedCol);
            _gameState.addMove();
            puntosLabel.setText("Movimientos: " + _gameState.getMoveCount());
            recordLabel.setText("Record: " + _gameState.getRecord());
            checkPlayerWon(panel);
        }
    }

    private boolean isAdjacentToEmpty(int clickedRow, int clickedCol) {
        return (clickedRow == emptyRow && Math.abs(clickedCol - emptyCol) == 1)
            || (clickedCol == emptyCol && Math.abs(clickedRow - emptyRow) == 1);
    }

    private void swapWithEmpty(int clickedRow, int clickedCol) {
        // Intercambia el texto y colores del botón clickeado con el casillero vacío
        _buttons[emptyRow][emptyCol].setText(_buttons[clickedRow][clickedCol].getText());
        _buttons[emptyRow][emptyCol].setBackground(Color.ORANGE);
        _buttons[clickedRow][clickedCol].setText("");
        _buttons[clickedRow][clickedCol].setBackground(Color.WHITE);

        // Actualiza la posición del casillero vacío
        emptyRow = clickedRow;
        emptyCol = clickedCol;
    }

    private void checkPlayerWon(JPanel panel) {                 /*****/
    	if (isInWinningState(getCurrentGridState(panel))) {
            showWinMessage();
            restart(panel);
        }
    }
    
    private boolean isInWinningState(String[][] state) {
        int n = state.length;
        int number = 1;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (row == n - 1 && col == n - 1) {
                    if (!state[row][col].isEmpty()) {
                        return false; // La última posición debe estar vacía
                    }
                } else {
                    if (!state[row][col].equals(String.valueOf(number++))) {
                        return false; // Verifica el orden correcto
                    }
                }
            }
        }
        return true;
    } 
    
    private String[][] getCurrentGridState(JPanel panel) {
        String[][] currentState = new String[_buttons.length][_buttons.length];
        for (int row = 0; row < _buttons.length; row++) {
            for (int col = 0; col < _buttons[row].length; col++) {
                currentState[row][col] = _buttons[row][col].getText();
            }
        }
        return currentState;
    }

    private void showWinMessage() {
        JOptionPane.showMessageDialog(null, "¡Has ganado el juego!", "Victoria", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clickOnStart(JButton btnStart, JPanel panel) {
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart(panel);
                startGame(panel);
                panel.setVisible(true); // Muestra el panel
            }
        });
    }

    private void clickOnRestart(JButton btnRestart, JPanel panel) {
        btnRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart(panel);
            }
        });
    }

    private void startGame(JPanel panel) {
        // Mezcla aleatoriamente los botones para comenzar el juego
        Random random = new Random();
        for (int i = 0; i < 100; i++) { // Realiza 100 intercambios aleatorios
            int row = random.nextInt(_buttons.length);
            int col = random.nextInt(_buttons.length);
            if (isAdjacentToEmpty(row, col)) {
                swapWithEmpty(row, col);
            }
        }
    }

    private void restart(JPanel panel) {
        _gameState.resetMoveCount();
        panel.setVisible(false); // Oculta el panel
    }

    //private void clickOnWildCard(JButton btnWildCard, JPanel panel) {
        // Acción del comodín 
    public void moveUp() {
        if (emptyRow < _buttons.length - 1) {
            swapWithEmpty(emptyRow + 1, emptyCol);
            _gameState.addMove();
        }
    }

    public void moveDown() {
        if (emptyRow > 0) {
            swapWithEmpty(emptyRow - 1, emptyCol);
            _gameState.addMove();
        }
    }

    public void moveLeft() {
        if (emptyCol < _buttons.length  - 1) {
            swapWithEmpty(emptyRow, emptyCol + 1);
            _gameState.addMove();
        }
    }
    
    public void moveRight() {
        if (emptyCol > 0) {
            swapWithEmpty(emptyRow, emptyCol - 1);
            _gameState.addMove();
        }
    }
    
}
