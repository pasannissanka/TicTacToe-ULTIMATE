/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.io.Serializable;

/**
 *
 * @author pasan
 */
public class NoughtPlayer extends Player implements Serializable{
    
    public NoughtPlayer(String playerName) {
        super(PlayerState.NOUGHT, playerName);
    }
    
}
