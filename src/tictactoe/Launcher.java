/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 *
 * @author pasan
 */
public class Launcher {

    public static MainScreen mainScreen;
    public static GameState gameState;

    public static void main(String[] args) {
        
        deserializeObjects();

        mainScreen = new MainScreen();
        mainScreen.setVisible(true);

    }

    private static void deserializeObjects() {
        try {
            FileInputStream fileInGS = new FileInputStream("./tmp/gameState.ser");
            ObjectInputStream inGS = new ObjectInputStream(fileInGS);
            gameState = (GameState) inGS.readObject();
            inGS.close();
            fileInGS.close();
        } catch (IOException i) {
            System.out.println("Save games unavailable " + i.getMessage());
        } catch (ClassNotFoundException c) {
            System.out.println("classes not found " + c.getMessage());
        }
    }

}
