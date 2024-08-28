/**
 * File:        QueueArea.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2023
 *
 * Summary of File:
 *      This file contains a JPanel Class called QueueArea which draws 3 blocks queued that will soon be played.
 */

package com.tetris.engine.gui;

import com.tetris.engine.event.GameEvent;
import com.tetris.engine.event.GameEventListener;
import com.tetris.engine.event.QueueAreaEvent;
import com.tetris.engine.model.tetrominoes.Tetrominoe;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/** BlocksArea Class -- Create a Panel that displays the upcoming blocks (GUI) */
public class QueueArea extends JPanel implements GameEventListener {

    //Initialize Variables
    private int gridColumns;
    private int gridCellSize;
    private int gridRows;

    // Store Blocks in Queue
    private LinkedList<Tetrominoe> nextBlocks = new LinkedList<>();

    /** CONSTRUCTOR */
    public QueueArea() {
        this.setPreferredSize(GameScreen.BLOCKS_PANEL_DIMENSION);
        this.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.black));

        validate();
    }

    /** Description: Initialize grid properties for QueueArea */
    public void initBlocksGrid() {
        this.gridColumns = 7;
        this.gridCellSize = this.getWidth() / gridColumns;
        this.gridRows = this.getHeight() / gridCellSize;
    }

    /** LIST FUNCTIONS */
    public void addBlock(Tetrominoe t) {
        nextBlocks.add(t);
        repaint();
    }
    public Tetrominoe removeBlock() {
        return nextBlocks.poll();
    }

    /** DRAW BLOCKS */
    private void drawBlocks(Graphics g) {
        int interval = 50;
        for (int i = 0; i < nextBlocks.size(); i++) {
            for (int row = nextBlocks.get(i).getPointY(); row < nextBlocks.get(i).getPointY() + nextBlocks.get(i).getHeight(); row++) {
                for (int col = nextBlocks.get(i).getPointX(); col < nextBlocks.get(i).getPointX() + nextBlocks.get(i).getWidth(); col++) {
                    if (nextBlocks.get(i).getCoords()[row][col] == 1) {
                        int x = (2 + col) * gridCellSize;
                        int y = row * gridCellSize + (20 + interval*i);

                        drawGridSquare(g, nextBlocks.get(i).getColour(), x, y);
                    }
                }
            }
        }
    }
    private void drawGridSquare(Graphics g, Color color, int x, int y) {
        g.setColor(color);
        g.fillRect(x, y, gridCellSize, gridCellSize);
        g.setColor(Color.black);
        g.drawRect(x, y, gridCellSize, gridCellSize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.nextBlocks != null) {
            drawBlocks(g);
        }
    }

    //Listen for Event in Block Controller
    @Override
    public void onEvent(GameEvent event) {
        if (event instanceof QueueAreaEvent) {
            QueueAreaEvent queueAreaEvent = (QueueAreaEvent) event;
            if (queueAreaEvent.getRemoveBlock()) {
                removeBlock();
            }

            addBlock(queueAreaEvent.getBlock());
        }
    }
}
