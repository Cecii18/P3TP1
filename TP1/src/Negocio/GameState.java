package Negocio;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private int moveCount; // Contador de movimientos realizados por el jugador
    private List<Tupla<String, Integer>> records; // Lista de récords para cada nivel
    private int nivelActual; // Nivel actual del juego

    public GameState() {
        this.moveCount = 0;
        this.records = new ArrayList<>();
        this.nivelActual = 0;
    }

    // Obtener el número de movimientos realizados
    public int getMoveCount() {
        return this.moveCount;
    }

    // Reiniciar el contador de movimientos
    public void resetMoveCount() {
        this.moveCount = 0;
    }

    // Incrementar el contador de movimientos
    public void addMove() {
        this.moveCount++;
    }

    // Configurar el nivel actual
    public void setNivelActual(int nivel) {
        this.nivelActual = nivel;
    }

    // Verificar si existe un récord para el nivel actual, y si no, crear uno
    public void verificarSiExisteRecord() {
        String nivelKey = nivelActual + "x" + nivelActual;
        boolean existeRecord = false;

        for (Tupla<String, Integer> record : this.records) {
            if (record.getElem1().equals(nivelKey)) {
                existeRecord = true;
                break;
            }
        }

        if (!existeRecord) {
            records.add(new Tupla<>(nivelKey, Integer.MAX_VALUE)); // Inicializar con un valor alto
        }
    }

    // Obtener el récord actual del nivel
    public int getRecord() {
        for (Tupla<String, Integer> record : this.records) {
            if (record.getElem1().equals(nivelActual + "x" + nivelActual)) {
                return record.getElem2();
            }
        }
        return Integer.MAX_VALUE;
    }

    // Verificar si el jugador ha ganado (todas las piezas están en su lugar)
    public boolean checkWin(boolean playerWon) {
        if (playerWon) {
            checkRecord(moveCount);
        }
        return playerWon;
    }

    // Verificar y actualizar el récord si se ha superado
    private void checkRecord(int moveCount) {
        for (Tupla<String, Integer> record : this.records) {
            if (record.getElem1().equals(nivelActual + "x" + nivelActual)) {
                if (record.getElem2() > moveCount) {
                    record.setElem2(moveCount); // Actualizar el récord con un menor número de movimientos
                }
            }
        }
    }
}
