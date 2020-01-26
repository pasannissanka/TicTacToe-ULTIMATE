/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

/**
 *
 * @author pasan
 */
public class GameUI extends JFrame implements ActionListener, Runnable {

    public GameState gameState;
    private Player currentPlayer;

    private Thread thread;
    private boolean running;
    private JPanel globalBoardJPanel, topPanel;
    private JPanel[] localBoardJPanels = new JPanel[9];
    private JLabel tvResult;
    private JButton reset, exit; 
    private PlayerDisplayPanel playerDisplayPanel;

    public GameUI() {
        gameState = new GameState();
        currentPlayer = gameState.player1;
    }

    public GameUI(GameState gameState) {
        this.gameState = gameState;
        currentPlayer = this.gameState.player1;
    }
    
    // Game loop
    @Override
    public void run() {
        init();
        while (running) {
            tick();
            render();
        }
    }
    
    private void tick() {
        // AI Player
        if (currentPlayer.getClass().equals(AIPlayer.class)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            ArrayList<JButton> validButtons;
            validButtons = gameState.getValidMoves();
            JButton button = validButtons.get(new Random().nextInt(validButtons.size()));
            button.doClick();
        }
    }

    private void render() {
        tvResult.setText(currentPlayer.getPlayerName());
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Cell cell = gameState.localBoards[i].cells[j];
                gameState.localBoards[i].cells[j].jb.setBackground(cell.btnColor);
                gameState.localBoards[i].cells[j].jb.setText(cell.character);
                gameState.localBoards[i].cells[j].jb.setEnabled(cell.enabled
                        && !cell.clicked);
            }
        }
        if (currentPlayer == gameState.player1) {
            playerDisplayPanel.setP1Panel();
            playerDisplayPanel.disP2Panel();
        } else {
            playerDisplayPanel.setP2Panel();
            playerDisplayPanel.disP1Panel();
        }
        
        playerDisplayPanel.setPlayer1Marks(gameState.player1.marks);
        playerDisplayPanel.setPlayer2Marks(gameState.player2.marks);
    }
    
    // Button click handler
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (gameState.localBoards[i].cells[j].jb == e.getSource()) {
                    if (currentPlayer.getState() == PlayerState.CROSS) {
                        gameState.localBoards[i].cells[j].cross();
                    } else {
                        gameState.localBoards[i].cells[j].nought();
                    }
                    gameState.updateState(i, j, currentPlayer, this);
                    System.out.println(i + " " + j + " " + gameState.localBoards[i].cells[j].state);
                    changePlayer();
                }
            }
        }
        if (e.getSource() == reset) {
            gameState.reset();
        }

//        if (e.getSource() == exit) {
//            serializeObjs();
//            System.exit(0);
//        }
    }

    private void changePlayer() {
        currentPlayer = (gameState.player1 == currentPlayer) ? gameState.player2 : gameState.player1;
    }

    // Exit operation, serialize gameState object
    private void serializeObjs() {
        try {
            FileOutputStream fileOutGS = new FileOutputStream("./tmp/gameState.ser");
            ObjectOutputStream outGS = new ObjectOutputStream(fileOutGS);
            outGS.writeObject(gameState);
            outGS.close();
            fileOutGS.close();
            System.out.printf("Serialized data is saved in /tmp/");
        } catch (IOException i) {
            System.out.println("Save game error " + i.getMessage());
        }
    }

    // gameUI INITIALIZATION
    private void init() {
        // INITIALIZE Panels
        GridLayout layoutF = new GridLayout(3, 3);
        layoutF.setHgap(5);
        layoutF.setVgap(5);
        GridLayout layoutB = new GridLayout(3, 3);
        layoutB.setHgap(2);
        layoutB.setVgap(2);

        topPanel = new JPanel();
        playerDisplayPanel = new PlayerDisplayPanel();

        globalBoardJPanel = new JPanel();
        globalBoardJPanel.setLayout(layoutF);
        globalBoardJPanel.setBackground(Color.BLACK);
        globalBoardJPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        tvResult = new JLabel();
        tvResult.setText(currentPlayer.getPlayerName());
        playerDisplayPanel.setPlayer1Name(gameState.player1.getPlayerName());
        playerDisplayPanel.setPlayer2Name(gameState.player2.getPlayerName());

        reset = new JButton("Restart");
        reset.addActionListener(this);
        reset.setFocusPainted(false);
//        exit = new JButton("Exit");
//        exit.addActionListener(this);
//        exit.setFocusPainted(false);

        topPanel.add(reset);
//        topPanel.add(exit);

        // INITIALIZE localboards and cells and add to globalboard
        for (int i = 0; i < 9; i++) {
            localBoardJPanels[i] = new JPanel();
            localBoardJPanels[i].setLayout(layoutB);

            for (int j = 0; j < 9; j++) {
                gameState.localBoards[i].cells[j].jb = new JButton();
                gameState.localBoards[i].cells[j].jb.addActionListener(this);
                gameState.localBoards[i].cells[j].jb.setFocusPainted(false);
                gameState.localBoards[i].cells[j].jb.setBackground(null);
                gameState.localBoards[i].cells[j].jb.setFont(new Font("Arial", Font.BOLD, 32));
                localBoardJPanels[i].add(gameState.localBoards[i].cells[j].jb);
            }
            globalBoardJPanel.add(localBoardJPanels[i]);
        }

        // JFRAME Initialization
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                String ObjButtons[] = {"Save and Exit", "Exit", "Cancel"};
                int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "TicTacToe ULTIMATE", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
                switch (PromptResult) {
                    case 0:
                        serializeObjs();
                        System.exit(0);
                        break;
                    case 1:
                        System.exit(0);
                        break;
                    case 2:
                    default:
                        break;
                }
            }
        });
        this.setResizable(false);
        this.setSize(800, 600);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setTitle("TicTacToe ULTIMATE");

        this.add(globalBoardJPanel, BorderLayout.CENTER);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(tvResult, BorderLayout.SOUTH);
        this.add(playerDisplayPanel, BorderLayout.WEST);
    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            thread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
