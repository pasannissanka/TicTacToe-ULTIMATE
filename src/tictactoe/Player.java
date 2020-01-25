/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.io.Serializable;

enum PlayerState {
    CROSS, NOUGHT
}

/**
 *
 * @author pasan
 */
public abstract class Player implements Serializable {

    private PlayerState state;
    private String playerName;
    private CellState cellState;
    public int marks;

    public Player(PlayerState state, String playerName) {
        this.state = state;
        this.playerName = playerName;
        if (state == PlayerState.CROSS) {
            cellState = CellState.CROSS;
        } else {
            cellState = CellState.NOUGHT;
        }
    }

    public PlayerState getState() {
        return state;
    }

    public String getPlayerName() {
        return playerName;
    }

    public CellState getCellState() {
        return cellState;
    }
}
