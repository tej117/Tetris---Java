/**
 * File:        TetrominoeCollection.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2023
 *
 * Summary of File:
 *      This class purely acts as a wrapper class so that this can be stored in the hashmap generated in
 *      TetrominoeCollection. The class holds all the properties required for each rotation of a tetris block
 *      since each rotation will have a different coords, width, height, pointX and pointY.
 *
 */

package com.tetris.engine.model.tetrominoes;

/** TetrominoeProperties - 'Wrapper' Class */
public class TetrominoeProperties {
    int[][] coords;
    int width;
    int height;
    int pointX;
    int pointY;

    /** CONSTRUCTOR - Creates an instance with these properties */
    TetrominoeProperties(int[][] coords, int width, int height, int pointX, int pointY) {
        this.coords = new int[coords.length][coords.length];
        TetrominoeCollection.clone2DSquareArray(coords.length, coords, this.coords);
        this.width = width;
        this.height = height;
        this.pointX = pointX;
        this.pointY = pointY;
    }

    /** GETTER METHODS */
    public int[][] getCoords() {
        return coords;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getPointX() {
        return pointX;
    }
    public int getPointY() {
        return pointY;
    }
}
