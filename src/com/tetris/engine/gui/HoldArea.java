/**
 * File:        HoldArea.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2023
 *
 * Summary of File:
 *      This file contains a JPanel Class called HoldArea which draws a block being held.
 */

package com.tetris.engine.gui;

import com.tetris.engine.event.GameEvent;
import com.tetris.engine.event.GameEventListener;
import com.tetris.engine.event.HoldAreaEvent;
import com.tetris.engine.model.tetrominoes.Tetrominoe;

import javax.swing.*;
import java.awt.*;

/** HoldArea Class -- Create a Panel that displays the block being held */
public class HoldArea extends JPanel implements GameEventListener {

    //Initialize Variables
    private int gridColumns;
    private int gridCellSize;
    private int gridRows;

    private Tetrominoe block;

    private int blockX = 0;
    private int blockY = 0;

    /** CONSTRUCTOR */
    public HoldArea() {
        this.setPreferredSize(GameScreen.HOLD_PANEL_DIMENSION);
        this.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.black));

        validate();
    }

    /** SETTER METHODS */
    private void setBlock(Tetrominoe block) {
        this.block = block;
    }

    /** Description: Creates the grid for the holdArea depending on number of gridColumns wanted */
    public void setHoldGrid(int gridColumns) {
        this.gridColumns = gridColumns;
        this.gridCellSize = this.getWidth() / gridColumns;
        this.gridRows = this.getHeight() / gridCellSize;
    }

    /** Description: Defines block coordinates for block type so that blocks are centered in the HoldArea */
    private void setDrawBlock(Tetrominoe.ShapeType shapeType) {
        if (shapeType.toString().equals("I")) {
            setHoldGrid(6);
            blockX = 1;
            blockY = 2;
        } else if (shapeType.toString().equals("O")) {
            setHoldGrid(6);
            blockX = 2;
            blockY = 2;
        } else if (shapeType.toString().equals("T")) {
            setHoldGrid(7);
            blockX = 2;
            blockY = 3;
        } else if (shapeType.toString().equals("S")) {
            setHoldGrid(7);
            blockX = 2;
            blockY = 3;
        } else if (shapeType.toString().equals("Z")) {
            setHoldGrid(7);
            blockX = 2;
            blockY = 3;
        } else if (shapeType.toString().equals("J")) {
            setHoldGrid(7);
            blockX = 2;
            blockY = 3;
        } else if (shapeType.toString().equals("L")) {
            setHoldGrid(7);
            blockX = 2;
            blockY = 3;
        }
        repaint();
    }

    /** DRAW BLOCKS */
    private void drawBlock(Graphics g) {
        for (int row = block.getPointY(); row < block.getPointY()+block.getHeight(); row++) {
            for (int col = block.getPointX(); col < block.getPointX()+block.getWidth(); col++) {
                if (block.getCoords()[row][col] == 1) {
                    int x = (blockX + col) * gridCellSize;
                    int y = (blockY + row) * gridCellSize;

                    drawGridSquare(g, block.getColour(), x, y);
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

        if (this.block != null) {
            drawBlock(g);
        }
    }

    //Listen for Event in Block Controller
    @Override
    public void onEvent(GameEvent event) {
        if (event instanceof HoldAreaEvent) {
            HoldAreaEvent holdAreaEvent = (HoldAreaEvent) event;

            //Update the block in hold area
            setBlock(holdAreaEvent.getBlock());
            //Set the position of the block in the holdArea
            setDrawBlock(holdAreaEvent.getBlock().getShapeType());
        }
    }
}
