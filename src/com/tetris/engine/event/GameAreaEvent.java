/**
 * File:        GameAreaEvent.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2024
 *
 * Summary of File:
 *      This file is the event for the GameArea. It contains the information that the GameArea requires
 *      from the block controller. Used to communicate between BlockController and GameArea.
 *
 */

package com.tetris.engine.event;

import com.tetris.engine.model.tetrominoes.Tetrominoe;

/** GameAreaEvent Class -- GameEvent for GameArea  */
public class GameAreaEvent extends GameEvent {

    //Initialize Variables
    private final Tetrominoe currentBlock;
    private final int theoreticalDropY;

    /** CONSTRUCTOR */
    public GameAreaEvent(Tetrominoe block, int theoreticalDropY) {
        this.currentBlock = block;
        this.theoreticalDropY = theoreticalDropY;
    }

    /** GETTER METHODS */
    public Tetrominoe getBlock() {
        return currentBlock;
    }
    public int getTheoreticalDropY() {
        return theoreticalDropY;
    }
}
