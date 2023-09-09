/**
 * File:        Board.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2023
 *
 * Summary of File:
 *      This file contains information regarding the Tetris grid (the playing field). Collisions, background blocks,
 *      and clearing lines are executed here.
 *
 */

package com.tetris.engine.board;

import com.tetris.engine.tetrominoes.Tetrominoe;
import com.tetris.engine.tetrominoes.TetrominoeProperties;

import java.awt.*;

public class Board {

    //Initialize Variables
    private final int gridColumns = 10;
    private final int gridCellSize;
    private final int gridRows;
    private final Color[][] background;

    /** CONSTRUCTOR */
    public Board (final int panelWidth, final int panelHeight) {
        this.gridCellSize = panelWidth / gridColumns;
        this.gridRows = panelHeight/gridCellSize;
        this.background = new Color[gridRows][gridColumns];
    }

    /** GETTER METHODS */
    public int getGridCellSize() {
        return this.gridCellSize;
    }
    public int getGridRows() {
        return this.gridRows;
    }
    public int getGridColumns() {
        return this.gridColumns;
    }
    public Color getBackgroundColor(int row, int column) {
        return this.background[row][column];
    }

    /** SETTER METHODS */
    public void setBackgroundColor(Color color, int row, int column) {
        background[row][column] = color;
    }

    /** Description: Detect collision when moving to new coordinates */
    public boolean checkBlockCollision(TetrominoeProperties tp, int newX, int newY) {

        //Check to see if rotating a block collides with other blocks
        int [][] coords = tp.getCoords();
        int h = tp.getPointY()+tp.getHeight();
        int w = tp.getPointX()+tp.getWidth();

        int xPos = newX;
        int yPos = newY;

        for (int r = tp.getPointY(); r < h; r++) {
            for (int c = tp.getPointX(); c < w; c++) {
                if (coords[r][c] == 1) {
                    if ((xPos + c >= 0 && xPos + c < this.getGridColumns()) &&
                            yPos + r > 0 && yPos + r < this.getGridRows()) {
                        if (this.getBackgroundColor(r + yPos, c + xPos)!= null) {
                            //System.out.println("Block Collision");
                            return true;
                        }
                    } else if (xPos + c < 0 || xPos + c >= this.getGridColumns()) {
                        //System.out.println("Side Wall Collision");
                        return true;
                    }
                    else if (yPos + r >= this.getGridRows()) {
                        //System.out.println("Vertical Wall Collision");
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /** Description: When the block reaches the bottom, push block to the background of the grid */
    public void moveBlockToBackground(Tetrominoe t) {
        int [][] coords = t.getCoords();
        int h = t.getPointY()+t.getHeight();
        int w = t.getPointX()+t.getWidth();

        int xPos = t.getX();
        int yPos = t.getY();

        Color color = t.getColour();

        for (int r = t.getPointY(); r < h; r++) {
            for (int c = t.getPointX(); c < w; c++) {
                if (coords[r][c] == 1) {
                    if ((xPos + c >= 0 && xPos < gridColumns) &&
                            yPos + (r+1) > 0 && yPos < gridRows) {
                        this.setBackgroundColor(color, r + yPos, c + xPos);
                    }
                }
            }
        }
    }

    /** CLEARING THE LINES */
    public void clearLine(int r) {
        for (int i = 0; i < this.gridColumns; i++) {
            this.setBackgroundColor(null, r, i);
        }
    }
    public void shiftDown(int r) {
        for (int row = r; row > 0; row--) {
            for (int col = 0; col < this.gridColumns; col++) {
                this.setBackgroundColor(getBackgroundColor(row-1, col), row, col);
            }
        }
    }
}
