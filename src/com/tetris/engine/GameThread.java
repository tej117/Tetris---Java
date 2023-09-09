/**
 * File:        GameThread.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2023
 *
 * Summary of File:
 *      This file contains code which runs the game loop. The GameThread class
 *      extends the Thread class so that the entire game can be run in
 *      an entirely new thread.
 *
 */

package com.tetris.engine;

import com.tetris.engine.GameForm.GameArea;

/**
 * GameThread Class -- New Thread for the game loop
 */
public class GameThread extends Thread {

    //Initialize Variables
    private final GameArea ga;
    private final GameForm gf;
    private int score;
    private int level = 1;
    private int currentLines = 0;
    private int lastClear = 0;
    private int combo = 0;
    private final int levelCounter = 10;

    private int gameSpeed = 1000;
    private final int speedupPerLevel = 20; //5%

    private int linesClearedTimer = 1;

    /** Constructor - Grab GameForm Object */
    public GameThread(GameForm gf) {
        this.ga = gf.getGameArea();
        this.gf = gf;
    }

    /** Description: Execute thread */
    @Override
    public void run() {
        //This the game loop and for right now, it will always remain true
        while(true) {

            //Spawn a Block
            ga.spawnBlock();

            //Have the block move down until it reaches the bottom
            while (!ga.checkBottom()) {
                try {
                    if (!ga.getPauseState()) {
                        ga.moveBlockDown();
                        Thread.sleep(gameSpeed);

                        if (linesClearedTimer == 0) gf.updateLinesCleared("");
                        else linesClearedTimer--;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //Exit while loop if block exceeds game screen height (gets here when block touches 'bottom')
            if(ga.isBlockOutOfBounds()) {
                gf.displayGameOverScreen();
                System.out.println("Game Over");
                break;
            }

            ga.moveBlockToBackground();

            //Update Score
            int linesCleared = ga.clearLines();
            currentLines += linesCleared;
            lastClear = linesCleared;
            linesClearedTimer = 1;

            switch(linesCleared) {
                case 1: { // Single
                    score += 80*(level+combo);
                    gf.updateLinesCleared("Single");
                    System.out.println("Single");
                    break;
                }
                case 2: { // Double
                    score += 200*(level+combo);
                    gf.updateLinesCleared("Double");
                    System.out.println("Double");
                    break;
                }
                case 3: { // Triple
                    score += 600*(level+combo);
                    gf.updateLinesCleared("Triple");
                    System.out.println("Triple");
                    break;
                }
                case 4: { //Tetris
                    score += 2400*(level+combo);
                    gf.updateLinesCleared("Tetris!");
                    System.out.println("Tetris!");
                    break;
                }
                case 8: { //Perfect Clear
                    score += 7600*(level+combo);
                    gf.updateLinesCleared("Perfect Clear");
                    System.out.println("Perfect Clear");
                    break;
                }
            }

            //Calculate Combo -- Try to Improve this by looking at other games
//            if (lastClear > 0) {
//                combo++;
//            } else {
//                combo = 0;
//            }

            gf.updateScore(score);

            //Update Level
            if (currentLines > levelCounter) {
                level++;
                gf.updateLevel(level);
                gameSpeed -= gameSpeed/speedupPerLevel; //Speedup by 5%
                currentLines = 0;
            }
        }
    }
}
