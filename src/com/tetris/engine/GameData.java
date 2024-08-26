/**
 * File:        GameData.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2024
 *
 * Summary of File:
 *      This file contains code which tracks current game data. The GameData object is created
 *      everytime a new game is started, which happens in the GameScreen class.
 *
 */

package com.tetris.engine;

public class GameData {

    //Initialize Variables
    private int score;
    private int level;

    /** Constructor - Initialize Game Data */
    public GameData() {
        this.score = 0;
        this.level = 1;
    }

    /**  GETTER METHODS */
    public int getScore() {
        return this.score;
    }
    public int getLevel() {
        return this.level;
    }

    /**  SETTER METHODS */
    public void setScore (int score) {
        this.score = score;
    }
    public void setLevel (int level) {
        this.level = level;
    }
}
