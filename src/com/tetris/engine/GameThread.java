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

import com.tetris.engine.gui.*;
import com.tetris.engine.logic.BlockController;
import com.tetris.engine.logic.MarathonController;

/** GameThread Class -- New Thread for the game loop */
public class GameThread extends Thread {

    //Initialize Variables
    private final GameScreen gs;
    private final MarathonController mc;
    private final BlockController bc;
    private int score;
    private int level = 1;
    private int currentLines = 0;
    private int lastClear = 0;
    private int combo = 0;
    private final int levelCounter = 10;

    private int gameSpeed = 1000;
    private final int speedupPerLevel = 20; //5%

    private int linesClearedTimer = 1;

    /** Constructor - Grab Game Objects */
    public GameThread(MarathonController mc, GameScreen gs, BlockController bc) {
        this.mc = mc;
        this.bc = bc;
        this.gs = gs;
    }

    /** Description: Execute thread */
    @Override
    public void run() {
        //This the game loop and for right now, it will always remain true
        while(true) {

            //Spawn a Block
            bc.spawnBlock();

            //Have the block move down until it reaches the bottom
            while (!bc.checkBottom()) {
                try {
                    if (!mc.getPauseState()) {
                        bc.moveBlockDown();
                        Thread.sleep(gameSpeed);

                        if (linesClearedTimer == 0) mc.updateLinesCleared("");
                        else linesClearedTimer--;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //Exit while loop if block exceeds game screen height (gets here when block touches 'bottom')
            if(bc.isBlockOutOfBounds()) {
                gs.displayGameOverScreen();
                System.out.println("Game Over");
                break;
            }

            mc.moveBlockToBackground();

            //Update Score
            int linesCleared = mc.clearLines();
            currentLines += linesCleared;
            lastClear = linesCleared;
            linesClearedTimer = 1;

            switch(linesCleared) {
                case 1: { // Single
                    score += 80*(level+combo);
                    mc.updateLinesCleared("Single");
                    System.out.println("Single");
                    break;
                }
                case 2: { // Double
                    score += 200*(level+combo);
                    mc.updateLinesCleared("Double");
                    System.out.println("Double");
                    break;
                }
                case 3: { // Triple
                    score += 600*(level+combo);
                    mc.updateLinesCleared("Triple");
                    System.out.println("Triple");
                    break;
                }
                case 4: { //Tetris
                    score += 2400*(level+combo);
                    mc.updateLinesCleared("Tetris!");
                    System.out.println("Tetris!");
                    break;
                }
                case 8: { //Perfect Clear
                    score += 7600*(level+combo);
                    mc.updateLinesCleared("Perfect Clear");
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

            mc.updateScore(score);

            //Update Level
            if (currentLines > levelCounter) {
                level++;
                mc.updateLevel(level);
                gameSpeed -= gameSpeed/speedupPerLevel; //Speedup by 5%
                currentLines = 0;
            }
        }
    }
}
