/**
 * File:        HoldAreaEvent.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2024
 *
 * Summary of File:
 *      This file is the event for the HoldArea. It contains the information that the HoldArea requires
 *      from the block controller. Used to communicate between BlockController and HoldArea.
 *
 */

package com.tetris.engine.event;

import com.tetris.engine.model.tetrominoes.Tetrominoe;

/** HoldAreaEvent Class -- GameEvent for HoldArea  */
public class HoldAreaEvent extends GameEvent {

    //Initialize Variables
    private final Tetrominoe heldBlock;

    /** CONSTRUCTOR */
    public HoldAreaEvent(Tetrominoe heldBlock) {
        this.heldBlock = heldBlock;
    }

    /** GETTER METHODS */
    public Tetrominoe getBlock() {
        return this.heldBlock;
    }
}
