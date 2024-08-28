/**
 * File:        GameEventDispatcher.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2024
 *
 * Summary of File:
 *      This file is where the listeners will be stored and dispatches the events to the listeners.
 *      NOTE: Events dispatched will go to all listeners. It is up to the listeners to know which event
 *      they will listen to.
 *
 */

package com.tetris.engine.event;

import java.util.ArrayList;
import java.util.List;

/** GameEventDispatcher Class -- Dispatches events to the listeners */
public class GameEventDispatcher {
    //Initialize Variables
    private final List<GameEventListener> listeners = new ArrayList<>();

    /** LISTENER METHODS */
    public void addListener(GameEventListener listener) {
        listeners.add(listener);
    }
    public void removeListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    /** Description: Go through every listener and pass the event into the listeners onEvent method. */
    public void dispatchEvent(GameEvent event) {
        for (GameEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
