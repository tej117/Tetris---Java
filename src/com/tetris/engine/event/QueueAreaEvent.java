/**
 * File:        QueueAreaEvent.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2024
 *
 * Summary of File:
 *      This file is the event for the QueueArea. It contains the information that the QueueArea requires
 *      from the block controller. Used to communicate between BlockController and QueueArea.
 *
 */

package com.tetris.engine.event;

import com.tetris.engine.model.tetrominoes.Tetrominoe;

/** QueueAreaEvent Class -- GameEvent for QueueArea */
public class QueueAreaEvent extends GameEvent {

    //Initialize Variables
    private final Tetrominoe queueBlock;
    private final boolean removeBlock;

    /** CONSTRUCTORS */
    public QueueAreaEvent(Tetrominoe queueBlock, boolean removeBlock) {
        this.queueBlock = queueBlock;
        this.removeBlock = removeBlock;
    }

    /** GETTER METHODS */
    public Tetrominoe getBlock() {
        return this.queueBlock;
    }
    public boolean getRemoveBlock() {
        return this.removeBlock;
    }
}
