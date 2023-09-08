/**
 * File:        GameForm.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2023
 *
 * Summary of File:
 *      This file contains code which creates the Java Swing GUI Interface
 *      for the Tetris Game. The file contains two classes, the GameForm class
 *      and the GameArea sub-class. The GameArea class is where the entire
 *      logic for the tetris game is stored.
 *
 */

package com.tetris.engine;

import com.tetris.engine.board.Board;
import com.tetris.engine.tetrominoes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/** GameForm Class -- Create the screen that displays the game */
public class GameForm {

    //Initialize Variables
    private final JFrame gameFrame;

    private final JLabel level;
    private final JLabel score;
    private final JLabel linesCleared;

    private final GameArea gameArea;
    private final HoldArea holdArea;

    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(200, 400);
    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private final static Dimension HOLD_PANEL_DIMENSION = new Dimension(50, 50);
    private final static Dimension BLOCKS_PANEL_DIMENSION = new Dimension(50, 50);

    /** CONSTRUCTOR -- Sets up the JFrame by appropriately placing the containers in the frame */
    public GameForm() {
        //JFrame and Layout
        this.gameFrame = new JFrame("JTetris");
        this.gameFrame.setLayout(new GridBagLayout());

        final JMenuBar gameFormMenuBar = createGameFormMenuBar();
        //this.gameFrame.setJMenuBar(gameFormMenuBar);

        this.score = new JLabel();
        this.level = new JLabel();
        this.linesCleared = new JLabel();

        //Update labels to initial state
        updateScore(0);
        updateLevel(1);

        level.setPreferredSize(new Dimension(50, 10));
        score.setPreferredSize(new Dimension(50, 10));
        linesCleared.setPreferredSize(new Dimension(50, 50));
        linesCleared.setFont(new Font("Serif", Font.PLAIN, 20));

        //JPanels
        this.holdArea = new HoldArea();
        this.gameArea = new GameArea(this.holdArea);

        //Add the JLabels and JPanel onto the JFrame GridBagLayout
        this.addItem(gameFrame, score, 2, 0, 1, 1,0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.NONE);
        this.addItem(gameFrame, level, 2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.NONE);
        //this.addItem(gameFrame, Box.createHorizontalStrut(50), 0, 0,1,1, 100.0, 25.0,
                //GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE);
        this.addItem(gameFrame, holdArea, 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE);
        //this.addItem(gameFrame, Box.createGlue(), 0, 3,1,1, 1.0, 1.0,
          //      GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        this.addItem(gameFrame, gameArea, 1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE);
        //this.addItem(gameFrame, linesCleared, 2, 2, 1, 1, 1.0, 0.25,
          //      GridBagConstraints.CENTER, GridBagConstraints.NONE);

        //this.gameFrame.pack();

        //Initialize parameters of the GameFrame
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setVisible(true);

        //Construct initial state of Tetris Grid
        this.gameArea.initTetrisGrid();
        this.holdArea.initHoldGrid(1);

        //Default InputMap in GameArea only works if gameArea JPanel has focus
        this.gameArea.setFocusable(true);

        //Begin game loop
        this.startGame(this);
    }

    /** UPDATE JLABEL VALUES */
    public void updateScore(int score) {
        this.score.setText("Score: " + score);
    }
    public void updateLevel(int level) {
        this.level.setText("Level: " + level);
    }
    public void updateLinesCleared(String type) {
        this.linesCleared.setText(type);
    }

    /** GETTER METHODS */
    public GameArea getGameArea() {
        return this.gameArea;
    }

    /**
     * Description: Create a GridBagConstraint for Component c, set constraints, then add component and
     *              GridBagConstraint to JFrame.
     */
    private void addItem(JFrame f, Component c, int x, int y, int width, int height,double weightx, double weighty, int align, int fill) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = x;
        gc.gridy = y;
        gc.gridwidth = width;
        gc.gridheight = height;
        gc.weightx = weightx;
        gc.weighty = weighty;
        gc.insets = new Insets(5, 5, 5, 5);
        gc.anchor = align;
        gc.fill = fill;
        f.add(c, gc);
    }

    /**
     * Description: Create a separate thread for the game loop so that it doesn't mess up the Main Thread
     * Parameters:  GameForm gf ---- Gets passed into GameThread
     */
    public void startGame(GameForm gf) {
        new GameThread(gf).start();
    }

    private JMenuBar createGameFormMenuBar() {
        return null;
    }

    /** GameArea Class -- Create a Panel that displays the game */
    public class GameArea extends JPanel {

        //Initialize Variables
        private Board tetrisGrid;
        private int gridCellSize;
        private int gridColumns;
        private int gridRows;

        private int bagSize = 0;
        private final ArrayList<Tetrominoe> blocks = new ArrayList<>();
        private Tetrominoe block;

        private HoldArea holdArea;
        private Tetrominoe heldBlock;
        private boolean switchBlock;    //A switch variable to ensure player only exchanges held block once

        private int theoreticalDropY = 0;   //A variable to hold destination y coordinate for block on grid

        /** CONSTRUCTOR -- Sets up the JPanel by defining size, border and all possible tetrominoes */
        public GameArea(HoldArea holdArea) {
            this.setPreferredSize(BOARD_PANEL_DIMENSION);
            this.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.black));

            this.holdArea = holdArea;

            initControls();
            validate();
        }

        /** Description: Set up keyboard buttons by defining certain buttons with actions */
        private void initControls() {
            //LOOK INTO inputMap and ActionMap
            InputMap im = this.getInputMap();
            ActionMap am = this.getActionMap();

            im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
            im.put(KeyStroke.getKeyStroke("LEFT"), "left");
            im.put(KeyStroke.getKeyStroke("SPACE"), "space");
            im.put(KeyStroke.getKeyStroke("Z"), "z");
            im.put(KeyStroke.getKeyStroke("X"), "x");
            im.put(KeyStroke.getKeyStroke("C"), "c");
            im.put(KeyStroke.getKeyStroke("DOWN"), "down");

            am.put("right", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveBlockRight();
                }
            });
            am.put("left", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    moveBlockLeft();
                }
            });
            am.put("space", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    hardDrop();
                }
            });
            am.put("z", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    rotateBlock(1);
                }
            });
            am.put("x", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    rotateBlock(0);
                }
            });
            am.put("c", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    holdBlock();
                }
            });
            am.put("down", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    softDrop();
                }
            });
        }

        /** Description: Initialize variables related to Tetris grid, and initialize the controls */
        public void initTetrisGrid() {
            tetrisGrid = new Board(this.getWidth(), this.getHeight());

            gridCellSize = tetrisGrid.getGridCellSize();
            gridColumns = tetrisGrid.getGridColumns();
            gridRows = tetrisGrid.getGridRows();

            blocks.add(new Tetrominoe(TetrominoeCollection.ISHAPE, tetrisGrid));
            blocks.add(new Tetrominoe(TetrominoeCollection.JSHAPE, tetrisGrid));
            blocks.add(new Tetrominoe(TetrominoeCollection.LSHAPE, tetrisGrid));
            blocks.add(new Tetrominoe(TetrominoeCollection.OSHAPE, tetrisGrid));
            blocks.add(new Tetrominoe(TetrominoeCollection.SSHAPE, tetrisGrid));
            blocks.add(new Tetrominoe(TetrominoeCollection.TSHAPE, tetrisGrid));
            blocks.add(new Tetrominoe(TetrominoeCollection.ZSHAPE, tetrisGrid));
        }

        /** SPAWN BLOCK */
        public void spawnBlock() {
            //Reset variables for newly spawned block
            if (block != null) {
                block.reset();
                block = null;
            }
            switchBlock = true;
            theoreticalDropY = 0;

            //If bag of blocks is empty, shuffle blocks again
            if (bagSize == 0) {
                bagSize = 7;
                java.util.Collections.shuffle(blocks);
            }

            //Grab block from list of shuffled blocks
            block = blocks.get(--bagSize);
            block.spawn();

            updateDropPosition();
        }

        /** MOVE BLOCKS */
        public void moveBlockRight() {
            if (block == null) return;

            int newX = block.getX()+1;

            //Check for Collision when moving to the right
            if (tetrisGrid.checkBlockCollision(block.getTP(), newX, block.getY())) return;

            block.moveRight();
            updateDropPosition();

            repaint();
        }
        public void moveBlockLeft() {
            if (block == null) return;

            int newX = block.getX() - 1;

            //Check for Collision when moving to the left
            if (tetrisGrid.checkBlockCollision(block.getTP(), newX, block.getY())) return;

            block.moveLeft();
            updateDropPosition();

            repaint();
        }
        public void hardDrop() {
            if (block == null) return;

            int newY = block.getY()+1;

            //Check for Collision when dropping block
            while (!tetrisGrid.checkBlockCollision(block.getTP(), block.getX(), newY)) {
                block.moveDown();
                newY++;
            }
            repaint();
        }
        public void softDrop() {
            moveBlockDown();
        }
        public void rotateBlock(int direction) {
            if (block == null) return;

            block.rotate(direction);
            updateDropPosition();

            repaint();
        }
        public boolean moveBlockDown() {

            int newY = block.getY() + 1;

            //Check for Collision when moving down
            if (tetrisGrid.checkBlockCollision(block.getTP(), block.getX(), newY)) {
                return true;
            }

            block.moveDown();
            repaint();

            return false;
        }

        /** Description: Hold the block (if no blocks are currently held) or switch held block with block on grid.
         *               Also checks to make sure that the player switches the block only once per new spawned block*/
        public void holdBlock() {

            //Game newly starts
            if (heldBlock == null) {
                heldBlock = new Tetrominoe(block.getShapeType());

                holdArea.setBlock(heldBlock);
                holdArea.setDrawBlock(block.getShapeType());

                switchBlock = false;

                //Spawn a new block
                spawnBlock();

                System.out.println(holdArea.getBlock().getShapeType());

                repaint();
                return;
            }

            //Check that player hasn't already switched a block
            if (switchBlock) {

                //Switch between holding block and block on grid
                Board tempBoard = block.getBoard();

                Tetrominoe temp = new Tetrominoe(heldBlock.getShapeType());
                heldBlock = new Tetrominoe(block.getShapeType());
                block = temp;
                block.setBoard(tempBoard);

                holdArea.setBlock(heldBlock);
                holdArea.setDrawBlock(block.getShapeType());

                block.spawn();

                switchBlock = false;
            }

            System.out.println(holdArea.getBlock().getShapeType());
            updateDropPosition();
            repaint();
        }

        /** Description: Perform a collision check to find where the block will potentially drop and save the y value */
        private void updateDropPosition() {
            if (block == null) return;

            theoreticalDropY = block.getY() + 1;

            while (!tetrisGrid.checkBlockCollision(block.getTP(), block.getX(), theoreticalDropY)) {
                theoreticalDropY++;
            }

            theoreticalDropY--;
        }

        /** Description: Checks to see if the game is over */
        public boolean isBlockOutOfBounds() {
            if(block.getY() < 0) {
                block = null;
                return true;
            }
            return false;
        }

        /** CLEARING LINES */
        public int clearLines() {
            boolean lineFilled;
            int linesCleared = 0;

            for (int r = gridRows - 1; r >= 0; r--) {
                lineFilled = true;
                for (int c = 0; c < gridColumns; c++) {
                    if (tetrisGrid.getBackgroundColor(r, c) == null) {
                        lineFilled = false;
                        break;
                    }
                }
                if (lineFilled) {
                    linesCleared++;
                    tetrisGrid.clearLine(r);
                    tetrisGrid.shiftDown(r);
                    tetrisGrid.clearLine(0); //Make sure top row is cleared when shifting down
                    r++;
                    repaint();
                }
            }

            //Check if bottom row is NOT empty (AKA NOT a Perfect Clear)
            for (int c = 0; c < gridColumns; c++) {
                if (tetrisGrid.getBackgroundColor(gridRows-1, c)!= null) {
                    return linesCleared;
                }
            }

            //Return Perfect Clear
            return 8;
        }

        /** Description: Push block to background using method in board */
        public void moveBlockToBackground() {
            tetrisGrid.moveBlockToBackground(block);
        }

        /** DRAW BLOCKS */
        private void drawBlock(Graphics g) {
            for (int row = block.getPointY(); row < block.getPointY()+block.getHeight(); row++) {
                for (int col = block.getPointX(); col < block.getPointX()+block.getWidth(); col++) {
                    if (block.getCoords()[row][col] == 1) {
                        if (block.getX() + col >= 0 && block.getX() < gridColumns &&
                                (block.getY() + (row+1) > 0 && block.getY() < gridRows)) {
                            int x = (block.getX() + col) * gridCellSize;
                            int y = (block.getY() + row) * gridCellSize;

                            drawGridSquare(g, block.getColour(), x, y);
                        }
                    }
                }
            }
        }
        private void drawBackground(Graphics g) {
            for (int r = 0; r < gridRows; r++) {
                for (int c = 0; c < gridColumns; c++) {
                    Color color = tetrisGrid.getBackgroundColor(r, c);

                    if (color != null) {
                        int x = c * gridCellSize;
                        int y = r * gridCellSize;

                        drawGridSquare(g, color, x, y);
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
        private void drawTheoreticalDropPosition(Graphics g) {
            for (int row = block.getPointY(); row < block.getPointY()+block.getHeight(); row++) {
                for (int col = block.getPointX(); col < block.getPointX()+block.getWidth(); col++) {
                    if (block.getCoords()[row][col] == 1) {
                        if (block.getX() + col >= 0 && block.getX() < gridColumns &&
                                (theoreticalDropY + (row+1) > 0 && theoreticalDropY < gridRows)) {
                            int x = (block.getX() + col) * gridCellSize;
                            int y = (theoreticalDropY + row) * gridCellSize;

                            drawGridSquare(g, Color.GRAY, x, y);
                        }
                    }
                }
            }
        }

        /** TESTING FUNCTIONS */
        private void testWallKicks(int x, int y, Graphics g) {
            tetrisGrid.setBackgroundColor(Color.BLACK, y, x);
            drawGridSquare(g, Color.BLACK, x*gridCellSize, y*gridCellSize);
        }

        //NOTE: Read up on paintComponent
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            drawBackground(g);
            if (this.block != null) {
                drawTheoreticalDropPosition(g);
                drawBlock(g);
            }
        }

    }
    public class BlocksArea extends JPanel {

    }

    public class HoldArea extends JPanel {

        //Initialize Variables
        private int gridColumns;
        private int gridCellSize;
        private int gridRows;

        private Tetrominoe block;

        private int blockX = 0;
        private int blockY = 0;

        public HoldArea() {
            this.setPreferredSize(HOLD_PANEL_DIMENSION);
            this.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.black));

            validate();
        }

        public Tetrominoe getBlock() {
            return block;
        }

        public void setBlock(Tetrominoe block) {
            this.block = block;
        }

        public void initHoldGrid(int gridColumns) {
            this.gridColumns = gridColumns;
            this.gridCellSize = this.getWidth() / gridColumns;
            this.gridRows = this.getHeight() / gridCellSize;
            System.out.println("Resizing");
        }

        public void setDrawBlock(Tetrominoe.ShapeType shapeType) {
            if (shapeType.toString().equals("I")) {
                initHoldGrid(4);
                blockX = 0;
                blockY = 0;
                System.out.println("I Block");
            } else {
                initHoldGrid(4);
                blockX = 1;
                blockY = 1;
            }
            repaint();
        }

        private void drawBlock(Graphics g) {
            System.out.println(gridColumns);
            System.out.println(gridRows);
            for (int row = block.getPointY(); row < block.getPointY()+block.getHeight(); row++) {
                for (int col = block.getPointX(); col < block.getPointX()+block.getWidth(); col++) {
                    if (block.getCoords()[row][col] == 1) {
                        int x = (blockX + col) * gridCellSize;
                        int y = (blockY + row) * gridCellSize;

                        System.out.println("Drawing");
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
    }
}
