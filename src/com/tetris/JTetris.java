/**
 * File:        JTetris.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2023
 * Purpose:     Self Project to help develop good object-oriented practices
 *
 * Summary of File:
 *      This file is where the main method of the program is stored. Running
 *      the main method will create a Java Swing GUI interface that plays
 *      the classic Tetris Game.
 *
 */

package com.tetris;

import com.tetris.engine.logic.GameController;

/** JTetris Class -- Where the game begins */
public class JTetris {

    /** Main Function: Runs the Tetris Game */
    public static void main(String[] args) {
        GameController gc = new GameController();
    }
}
