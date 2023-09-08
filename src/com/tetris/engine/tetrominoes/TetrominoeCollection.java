/**
 * File:        TetrominoeCollection.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2023
 *
 * Summary of File:
 *      This file stores all the tetrominoe properties that remain static. It creates a hashmap,
 *      that keeps track of all the rotation coordinates for each block, alongside the width, height, x and y
 *      coordinates, which are needed for collision detection.
 *
 */

package com.tetris.engine.tetrominoes;

import java.util.HashMap;

public class TetrominoeCollection {

    public final static Tetrominoe.ShapeType ISHAPE = Tetrominoe.ShapeType.IShape;
    public final static Tetrominoe.ShapeType JSHAPE = Tetrominoe.ShapeType.JShape;
    public final static Tetrominoe.ShapeType LSHAPE = Tetrominoe.ShapeType.LShape;
    public final static Tetrominoe.ShapeType OSHAPE = Tetrominoe.ShapeType.OShape;
    public final static Tetrominoe.ShapeType SSHAPE = Tetrominoe.ShapeType.SShape;
    public final static Tetrominoe.ShapeType TSHAPE = Tetrominoe.ShapeType.TShape;
    public final static Tetrominoe.ShapeType ZSHAPE = Tetrominoe.ShapeType.ZShape;

    private final static int[][] ICOORDS =     {{0, 0, 0, 0},
                                                {1, 1, 1, 1},
                                                {0, 0, 0, 0},
                                                {0, 0, 0, 0}};
    private final static int[][] JCOORDS =     {{1, 0, 0},
                                                {1, 1, 1},
                                                {0, 0, 0}};
    private final static int[][] LCOORDS =     {{0, 0, 1},
                                                {1, 1, 1},
                                                {0, 0, 0}};
    private final static int[][] OCOORDS =     {{1, 1},
                                                {1, 1}};
    private final static int[][] SCOORDS =      {{0, 1, 1},
                                                {1, 1, 0},
                                                {0, 0, 0}};
    private final static int[][] TCOORDS =     {{0, 1, 0},
                                                {1, 1, 1},
                                                {0, 0, 0}};
    private final static int[][] ZCOORDS =      {{1, 1, 0},
                                                {0, 1, 1},
                                                {0, 0, 0} };

    //J, L, T, S, Z Wall Kick Offset Table
    public final static int[][][][] OFFSETS1 = {
            {   //FIRST: 0>>1 (Clockwise)     SECOND: 0>>3 (Counter-Clockwise)
                {{-1, 0}, {-1, 1}, {0, -2}, {-1, -2}},
                {{1, 0}, {1, 1}, {0, -2}, {1, -2}}
            },
            {   //FIRST: 1>>2 (Clockwise)     SECOND: 1>>0 (Counter-Clockwise)
                {{1, 0}, {1, -1}, {0, 2}, {1, 2}},
                {{1, 0}, {1, -1}, {0, 2}, {1, 2}}
            },
            {   //FIRST: 2>>3 (Clockwise)     SECOND: 2>>1 (Counter-Clockwise)
                {{1, 0}, {1, 1}, {0, -2}, {1, -2}},
                {{-1, 0}, {-1, 1}, {0, -2}, {-1, -2}}
            },
            {   //FIRST: 3>>0 (Clockwise)     SECOND: 3>>2 (Counter-Clockwise)
                {{-1, 0}, {-1, -1}, {0, 2}, {-1, 2}},
                {{-1, 0}, {-1, -1}, {0, 2}, {-1, 2}}
            }};

    //I Wall Kick Offset Table
    public final static int[][][][] OFFSETS2 = {
            {   //FIRST: 0>>1 (Clockwise)     SECOND: 0>>3 (Counter-Clockwise)
                    {{-2, 0}, {1, 0}, {1, 2}, {-2, -1}},
                    {{2, 0}, {-1, 0}, {-1, 2}, {2, -1}}
            },
            {   //FIRST: 1>>2 (Clockwise)     SECOND: 1>>0 (Counter-Clockwise)
                    {{-1, 0}, {2, 0}, {-1, 2}, {2, -1}},
                    {{2, 0}, {-1, 0}, {2, 1}, {-1, -2}}
            },
            {   //FIRST: 2>>3 (Clockwise)     SECOND: 2>>1 (Counter-Clockwise)
                    {{2, 0}, {-1, 0}, {2, 1}, {-1, -1}},
                    {{-2, 0}, {1, 0}, {-2, 1}, {1, -1}}
            },
            {   //FIRST: 3>>0 (Clockwise)     SECOND: 3>>2 (Counter-Clockwise)
                    {{-2, 0}, {1, 0}, {-2, 1}, {1, -2}},
                    {{1, 0}, {-2, 0}, {1, 2}, {-2, -1}}
            }};

    public static final HashMap<String, HashMap<Integer, TetrominoeProperties>>
                          TETROMINOES_PROPERTIES  = initTetrisBlockProperties();

    private TetrominoeCollection() {
        throw new RuntimeException(("You cannot instantiate me!"));
    }

    private static HashMap<String, HashMap<Integer, TetrominoeProperties>>
                    initTetrisBlockProperties() {
        final HashMap<String, HashMap<Integer, TetrominoeProperties>>
                tetrominoesProperties = new HashMap<>();

        tetrominoesProperties.put("I", initRotationProperties(ICOORDS));
        tetrominoesProperties.put("J", initRotationProperties(JCOORDS));
        tetrominoesProperties.put("L", initRotationProperties(LCOORDS));
        tetrominoesProperties.put("O", initRotationProperties(OCOORDS));
        tetrominoesProperties.put("S", initRotationProperties(SCOORDS));
        tetrominoesProperties.put("T", initRotationProperties(TCOORDS));
        tetrominoesProperties.put("Z", initRotationProperties(ZCOORDS));

        return tetrominoesProperties;
    }

    private static HashMap<Integer, TetrominoeProperties> initRotationProperties(int[][] coords) {
        final HashMap<Integer, TetrominoeProperties> rotationProperties = new HashMap<>();

        int matrixSize = coords.length;

        int[][] tempCoords = new int[matrixSize][matrixSize];
        clone2DSquareArray(matrixSize, coords,tempCoords);

        rotationProperties.put(0, new TetrominoeProperties(tempCoords, getMaxWidth(tempCoords), getMaxHeight(tempCoords),
                                                            getLeftMostX(tempCoords), getTopMostY(tempCoords)));
        rotationProperties.put(3, rotateMatrix(tempCoords));
        rotationProperties.put(2, rotateMatrix(tempCoords));
        rotationProperties.put(1, rotateMatrix(tempCoords));

        return rotationProperties;
    }

    private static TetrominoeProperties rotateMatrix(int[][] coords) {
        int matrixSize = coords.length;

        //Reverse every row
        for (int i = 0; i < matrixSize; i++) {
            int start = 0;
            int end = matrixSize - 1;
            while (start < end) {
                int temp = coords[i][start];
                coords[i][start] = coords[i][end];
                coords[i][end] = temp;

                start++;
                end--;
            }
        }
        //Transpose Matrix
        for (int i = 0; i < matrixSize; i++) {
            for (int j = i; j < matrixSize; j++) {
                int temp = coords[i][j];
                coords[i][j] = coords[j][i];
                coords[j][i] = temp;
            }
        }

        return new TetrominoeProperties(coords, getMaxWidth(coords), getMaxHeight(coords),
                                        getLeftMostX(coords), getTopMostY(coords));
    }
    private static int getMaxWidth(int[][] matrix) {
        int maxWidth = 0;

        //array that tracks to see which columns contain a 1
        int[] cellColumns = new int[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if(matrix[i][j] != 0) {
                    if (cellColumns[j] == 0) {
                        cellColumns[j] = 1;
                        maxWidth += matrix[i][j];
                    }

                }
            }
        }

        return maxWidth;
    }
    private static int getMaxHeight(int[][] matrix) {
        int maxHeight = 0;

        //array that tracks to see which rows contain a 1
        int[] cellRows = new int[matrix.length];

        for (int j = 0; j < matrix.length; j++) {
            for (int i = 0; i < matrix.length; i++) {
                if(matrix[i][j] != 0) {
                    if (cellRows[i] == 0) {
                        cellRows[i] = 1;
                        maxHeight += matrix[i][j];
                    }
                }
            }
        }

        return maxHeight;
    }
    private static int getLeftMostX(int[][] matrix) {
        for (int j = 0; j < matrix.length; j++) {
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[i][j] == 1) {
                    return j;
                }
            }
        }

        return -1;
    }
    private static int getTopMostY(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == 1) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static void clone2DSquareArray(int length, int[][] src, int[][] dest) {
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, dest[i], 0, length);
        }
    }
}

