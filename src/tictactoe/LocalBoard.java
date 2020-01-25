/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.io.Serializable;

enum LocalBoardState {
    UNDECIDED,
    CLOSE,
    WIN,
    DRAW
}

/**
 *
 * @author pasan
 */
public class LocalBoard implements Serializable {
    public Cell[] cells = new Cell[9];
    public LocalBoardState state;
    public Player winner;
    
    public LocalBoard() {
        this.state = LocalBoardState.UNDECIDED;
        
    }
    
    public void reset() {
        this.state = LocalBoardState.UNDECIDED;
        this.winner = null;
    }
    
}
