/**
 * File:        Tetrominoe.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2023
 *
 * Summary of File:
 *      This file contains code for the Tetrominoe object.
 *
 */

package com.tetris.engine.tetrominoes;

import com.tetris.engine.board.Board;

import java.awt.Color;

/** Tetrominoe Class -- Create a Tetris Block */
public class Tetrominoe {

    //Initialized Variables
    private int x, y;
    private int currentRotation;
    private int[][] coords;
    private final ShapeType shapeType;
    private Board board;

    /** CONSTRUCTORS */
    public Tetrominoe(ShapeType shapeType) {
        this.shapeType = shapeType;
        this.board = null;

        this.coords = getTP().getCoords();
        currentRotation = 0;
    }
    public Tetrominoe(ShapeType shapeType, Board board) {
        this.shapeType = shapeType;
        this.board = board;

        this.coords = getTP().getCoords();
        currentRotation = 0;
    }

    /** Description: Spawn block with default rotation at top-center of screen */
    public void spawn() {
        if (board == null) throw new NullPointerException("Tetrominoe cannot be Spawned!");

        coords = getTP().getCoords();

        y = -getTP().getHeight();                           //Spawn Block off-screen
        x = board.getGridColumns()/2 - (coords.length/2);   //Spawn in the center
    }

    /** Description: Reset xy values to 0 */
    public void reset() {
        this.setX(0);
        this.setY(0);
    }

    /** GETTER METHODS */
    public Color getColour() {
        return shapeType.getColour();
    }
    public int[][] getCoords() {
        return getTP().getCoords();
    }
    public TetrominoeProperties getTP() {
        return TetrominoeCollection.TETROMINOES_PROPERTIES.get(shapeType.toString()).get(currentRotation);
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getHeight() {
        return getTP().getHeight();
    }
    public int getWidth() {
        return getTP().getWidth();
    }
    public int getPointX() {
        return getTP().getPointX();
    }
    public int getPointY() {
        return getTP().getPointY();
    }
    public ShapeType getShapeType() {
        return shapeType;
    }
    public Board getBoard() {
        return this.board;
    }


    /** SETTER METHODS */
    private void setX(int x) {
        this.x = x;
    }
    private void setY(int y) {
        this.y = y;
    }
    public void setBoard(Board board) {
        this.board = board;
    }

    /** COLLISION POSITIONS */
    public int getCollisionX() {
        return this.x + getPointX();
    }
    public int getCollisionY() {
        return this.y + getPointY();
    }

    /** MOVE BLOCK */
    public void moveDown() {
        this.setY(++y);
    }
    public void moveLeft() {
        this.setX(--x);
    };
    public void moveRight() {
        this.setX(++x);
    }

    /** ROTATE BLOCK */
    public void rotate(int direction) {
        if (board == null) throw new NullPointerException("Tetrominoe cannot be Rotated!");

        offset(currentRotation, direction);
    }
    /** Description: Perform basic rotation, perform rotation with offset, or don't rotate */
    private void offset(int currentRotation, int direction) {
        //Initialize Variable
        int[][] offsets;

        //Grab the correct offset array
        if (this.shapeType.toString().equals("I")) {
            offsets = TetrominoeCollection.OFFSETS2[currentRotation][direction];
        } else {
            offsets = TetrominoeCollection.OFFSETS1[currentRotation][direction];
        }

        //Grab the next rotation value by checking the rotation direction
        int nextRotation = currentRotation;
        if (direction == 0) {
            nextRotation = nextRotation + 1 > 3 ? 0 : ++nextRotation;   //Ternary Operator
        } else {
            nextRotation = nextRotation - 1 < 0 ? 3 : --nextRotation;   //Ternary Operator
        }

        //Grab the block properties of the nextRotation without having to create a block
        TetrominoeProperties newRotatedBlock = TetrominoeCollection.TETROMINOES_PROPERTIES.get(this.shapeType.toString()).get(nextRotation);

        //Check collision for basic rotation
        if (!board.checkBlockCollision(newRotatedBlock, this.getX(), this.getY())) {
            this.currentRotation = nextRotation;
            coords = this.getTP().getCoords();
        } else { //Run Offset checks
            for (int[] offset : offsets) {
                //Apply offsets and grab new coordinates
                int newX = this.getX() + offset[0];
                int newY = this.getY() - offset[1];

                //Check collision
                if (!board.checkBlockCollision(newRotatedBlock, newX, newY)) {
                    this.currentRotation = nextRotation;
                    coords = this.getTP().getCoords();

                    this.setX(newX);
                    this.setY(newY);

                    return;
                }
            }
        }
    }

    /** ShapeType Enum - Defines name and colour of each tetris block */
    public enum ShapeType {
        //Enums
        SShape("S", Color.GREEN),
        ZShape("Z", Color.RED),
        IShape("I", Color.CYAN),
        LShape("L", Color.ORANGE),
        JShape("J", Color.BLUE),
        OShape("O", Color.YELLOW),
        TShape("T", new Color(192, 0, 255));

        final private String shapeName;
        final private Color colour;

        ShapeType(final String shapeName, final Color colour) {
            this.shapeName = shapeName;
            this.colour = colour;
        }

        public Color getColour() {return this.colour;}

        @Override
        public String toString() {
            return this.shapeName;
        }
    }
}
