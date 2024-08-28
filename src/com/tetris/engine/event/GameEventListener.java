/**
 * File:        GameEventListener.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2024
 *
 * Summary of File:
 *      An interface which all listeners must implement.
 *
 */

package com.tetris.engine.event;

/** GameEventListener Interface */
public interface GameEventListener {

    /** Description: Listeners will implement this method so that it can receive dispatched events. */
    void onEvent(GameEvent event);
}
