/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.awt.Color;
import java.io.Serializable;
import javax.swing.JButton;

enum CellState {
    EMPTY,
    NOUGHT,
    CROSS
}

/**
 *
 * @author pasan
 */
public class Cell implements Serializable{
    public transient JButton jb;
    public CellState state;
    public boolean clicked;
    public boolean enabled;
    public String character;
    public Color btnColor;

    public Cell() {
        this.character = "";
        this.state = CellState.EMPTY;
        this.clicked = false;
        this.enabled = true;
        this.btnColor = null;
    }
    
    public void reset() {
        state = CellState.EMPTY;
        character = "";
        clicked = false;
        enabled = true;
        this.btnColor = null;
    }
    
    public void cross() {
        state = CellState.CROSS;
        character = "X";
        clicked = true;
    }
    
    public void nought() {
        state = CellState.NOUGHT;
        character = "O";
        clicked = true;
    }
    
}
