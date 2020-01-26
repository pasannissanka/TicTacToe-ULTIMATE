/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author pasan
 */
public class GameState implements Serializable {
    // All game logic is handled by this class

    public LocalBoard[] localBoards = new LocalBoard[9];
    public Player player1, player2;
    public int round;

    public GameState() {
        // For Testing
        for (int i = 0; i < 9; i++) {
            this.localBoards[i] = new LocalBoard();
            for (int j = 0; j < 9; j++) {
                this.localBoards[i].cells[j] = new Cell();
            }
        }
        player1 = new CrossPlayer("Player 01");
        player2 = new NoughtPlayer("Player 02");
    }

    public GameState(Player player1, Player player2) {
        for (int i = 0; i < 9; i++) {
            this.localBoards[i] = new LocalBoard();
            for (int j = 0; j < 9; j++) {
                this.localBoards[i].cells[j] = new Cell();
            }
        }
        this.round = 1;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void updateState(int currentLB, int targetLB, Player currentPlayer, JFrame jframe) {
        // (current) LB win or Draw
        currentPlayer.marks -= 5;
        if (isLBWin(this.localBoards[currentLB], currentPlayer)) {
            this.localBoards[currentLB].state = LocalBoardState.WIN;
            this.localBoards[currentLB].winner = currentPlayer;
            System.out.println("LB Win " + currentLB + currentPlayer.getPlayerName());
            drawWinGraphic(localBoards[currentLB], currentPlayer);
            currentPlayer.marks += 100;
        } else if (isLBDraw(this.localBoards[currentLB])) {
            this.localBoards[currentLB].state = LocalBoardState.DRAW;
            System.out.println("LB Draw " + currentLB);
        }
        // is Game win
        if (isGameWin(currentPlayer)) {
            System.out.println("Game win " + currentPlayer.getPlayerName());
            disableAllCells();
            // Draw graphics
            WinDialog winDialog = new WinDialog(jframe, true);
            winDialog.setWinner(currentPlayer);
            winDialog.setVisible(true);
            currentPlayer.marks += 700;
            return;
        } else if (isGameDraw()) {
            System.out.println("Game Draw");
            disableAllCells();
            DrawDialog drawDialog = new DrawDialog(jframe, true);
            drawDialog.setVisible(true);
            return;
        }
        // update state of target LB
        updateLBState(targetLB);
//        System.out.println(currentPlayer.getPlayerName() + " , " +currentPlayer.marks);
        // update cells
        updateCellDisable();

    }

    private boolean isLBWin(LocalBoard localBoard, Player currentPlayer) {
        if ((localBoard.cells[0].state == currentPlayer.getCellState() && localBoard.cells[1].state == currentPlayer.getCellState() && localBoard.cells[2].state == currentPlayer.getCellState())
                || (localBoard.cells[3].state == currentPlayer.getCellState() && localBoard.cells[4].state == currentPlayer.getCellState() && localBoard.cells[5].state == currentPlayer.getCellState())
                || (localBoard.cells[6].state == currentPlayer.getCellState() && localBoard.cells[7].state == currentPlayer.getCellState() && localBoard.cells[8].state == currentPlayer.getCellState())
                || (localBoard.cells[0].state == currentPlayer.getCellState() && localBoard.cells[3].state == currentPlayer.getCellState() && localBoard.cells[6].state == currentPlayer.getCellState())
                || (localBoard.cells[1].state == currentPlayer.getCellState() && localBoard.cells[4].state == currentPlayer.getCellState() && localBoard.cells[7].state == currentPlayer.getCellState())
                || (localBoard.cells[2].state == currentPlayer.getCellState() && localBoard.cells[5].state == currentPlayer.getCellState() && localBoard.cells[8].state == currentPlayer.getCellState())
                || (localBoard.cells[0].state == currentPlayer.getCellState() && localBoard.cells[4].state == currentPlayer.getCellState() && localBoard.cells[8].state == currentPlayer.getCellState())
                || (localBoard.cells[2].state == currentPlayer.getCellState() && localBoard.cells[4].state == currentPlayer.getCellState() && localBoard.cells[6].state == currentPlayer.getCellState())) {
            return true;
        }
        return false;
    }

    private boolean isLBDraw(LocalBoard localBoard) {
        int tries = 0;
        for (Cell cell : localBoard.cells) {
            if (cell.state != CellState.EMPTY) {
                tries++;
            }
        }
        return tries == 9;
    }

    private boolean isGameWin(Player currentPlayer) {
        if ((localBoards[0].winner == currentPlayer && localBoards[1].winner == currentPlayer && localBoards[2].winner == currentPlayer)
                || (localBoards[3].winner == currentPlayer && localBoards[4].winner == currentPlayer && localBoards[5].winner == currentPlayer)
                || (localBoards[6].winner == currentPlayer && localBoards[7].winner == currentPlayer && localBoards[8].winner == currentPlayer)
                || (localBoards[0].winner == currentPlayer && localBoards[3].winner == currentPlayer && localBoards[6].winner == currentPlayer)
                || (localBoards[1].winner == currentPlayer && localBoards[4].winner == currentPlayer && localBoards[7].winner == currentPlayer)
                || (localBoards[2].winner == currentPlayer && localBoards[5].winner == currentPlayer && localBoards[8].winner == currentPlayer)
                || (localBoards[0].winner == currentPlayer && localBoards[4].winner == currentPlayer && localBoards[8].winner == currentPlayer)
                || (localBoards[2].winner == currentPlayer && localBoards[4].winner == currentPlayer && localBoards[6].winner == currentPlayer)) {
            return true;
        }
        return false;
    }

    private boolean isGameDraw() {
        int tries = 0;
        for (int i = 0; i < 9; i++) {
            if ((localBoards[i].state == LocalBoardState.WIN || localBoards[i].state == LocalBoardState.WIN)) {
                tries++;
            }
        }
        return tries == 9;
    }

    private void updateLBState(int index) {

        if (!(localBoards[index].state == LocalBoardState.DRAW || localBoards[index].state == LocalBoardState.WIN)) {
            for (int i = 0; i < 9; i++) {
                if (i == index) {
                    localBoards[index].state = LocalBoardState.UNDECIDED;
                } else if (!(localBoards[i].state == LocalBoardState.WIN || localBoards[i].state == LocalBoardState.DRAW)) {
                    localBoards[i].state = LocalBoardState.CLOSE;
                }
            }
        } else {
            for (int i = 0; i < 9; i++) {
                if (i == index) {
                } else if (!(localBoards[i].state == LocalBoardState.WIN || localBoards[i].state == LocalBoardState.DRAW)) {
                    localBoards[i].state = LocalBoardState.UNDECIDED;
                }
            }
        }
    }

    private void updateCellDisable() {
        for (int i = 0; i < 9; i++) {
            if (!(localBoards[i].state == LocalBoardState.UNDECIDED)) {
                for (int j = 0; j < 9; j++) {
                    localBoards[i].cells[j].enabled = false;
                }
            } else {
                for (int j = 0; j < 9; j++) {
                    localBoards[i].cells[j].enabled = true;
                }
            }
        }
    }

    private void drawWinGraphic(LocalBoard localBoard, Player currentPlayer) {
        if (currentPlayer.getState() == PlayerState.CROSS) {
            localBoard.cells[0].btnColor = Color.red;
            localBoard.cells[4].btnColor = Color.red;
            localBoard.cells[8].btnColor = Color.red;
            localBoard.cells[2].btnColor = Color.red;
            localBoard.cells[6].btnColor = Color.red;
        } else {
            localBoard.cells[0].btnColor = Color.blue;
            localBoard.cells[1].btnColor = Color.blue;
            localBoard.cells[2].btnColor = Color.blue;
            localBoard.cells[3].btnColor = Color.blue;
            localBoard.cells[5].btnColor = Color.blue;
            localBoard.cells[6].btnColor = Color.blue;
            localBoard.cells[7].btnColor = Color.blue;
            localBoard.cells[8].btnColor = Color.blue;
        }
    }

    private void disableAllCells() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                localBoards[i].cells[j].enabled = false;
            }
        }
    }

    public void reset() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                localBoards[i].cells[j].reset();
                localBoards[i].cells[j].jb.setBackground(null);
            }
            localBoards[i].reset();
        }
        player1.marks = 0;
        player2.marks = 0;
    }

    public void restart() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                localBoards[i].cells[j].reset();
                localBoards[i].cells[j].jb.setBackground(null);
            }
            localBoards[i].reset();
        }
    }

    public ArrayList<JButton> getValidMoves() {
        ArrayList<JButton> validButtons = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (localBoards[i].state == LocalBoardState.UNDECIDED) {
                for (int j = 0; j < 9; j++) {
                    if (!localBoards[i].cells[j].clicked && localBoards[i].cells[j].enabled) {
                        validButtons.add(localBoards[i].cells[j].jb);
                    }
                }
            }
        }
        return validButtons;
    }
}
