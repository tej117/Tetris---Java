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
import java.util.LinkedList;

import static javax.swing.JOptionPane.showMessageDialog;

/** GameForm Class -- Create the screen that displays the game */
public class GameForm {

    //Initialize Variables
    private final JFrame gameFrame;

    private final JLabel level;
    private final JLabel score;
    private final JLabel linesCleared;

    private final GameArea gameArea;
    private final HoldArea holdArea;
    private final BlocksArea blocksArea;

    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(200, 400);
    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(400,550);
    private final static Dimension HOLD_PANEL_DIMENSION = new Dimension(50, 50);
    private final static Dimension BLOCKS_PANEL_DIMENSION = new Dimension(50, 150);

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
        linesCleared.setFont(new Font("Serif", Font.PLAIN, 18));

        //JPanels
        this.holdArea = new HoldArea();
        this.blocksArea = new BlocksArea();
        this.gameArea = new GameArea(this.holdArea, this.blocksArea);

        //Add the JLabels and JPanel onto the JFrame GridBagLayout
        this.addItem(gameFrame, score, 0, 0, GridBagConstraints.REMAINDER, 1,0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH);
        this.addItem(gameFrame, level, 0, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH);
        this.addItem(gameFrame, holdArea, 0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH);
        this.addItem(gameFrame, gameArea, 1, 2, 1, 3, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH);
        this.addItem(gameFrame, blocksArea, 2, 2, 1, 2, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH);
        this.addItem(gameFrame, linesCleared, 2, 4, GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        this.addItem(gameFrame, Box.createGlue(), 1, 5,1,GridBagConstraints.REMAINDER, 0.0, 1.0,
              GridBagConstraints.NORTH, GridBagConstraints.BOTH);

        //this.gameFrame.pack();

        //Initialize parameters of the GameFrame
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setVisible(true);
        this.gameFrame.setResizable(false);

        //Construct initial state of Tetris Grid
        this.gameArea.initTetrisGrid();
        this.holdArea.setHoldGrid(1);
        this.blocksArea.initBlocksGrid();

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

    public void displayGameOverScreen() {
        //Create New JPanel on top of Frame using the glassPane
        final JPanel glass = (JPanel) gameFrame.getGlassPane();

        //Try to figure out how to add background color onto the glass pane directly
        glass.setVisible(true);
        glass.setLayout(new BorderLayout());

        //In the background JPanel, add a button action which will trigger a new game

        JPanel background = new JPanel(new BorderLayout());
        background.setBackground(new Color(0,0,0,255/*125*/));
        background.setPreferredSize(OUTER_FRAME_DIMENSION);

        JButton newGame = new JButton("New Game");
        newGame.setPreferredSize(new Dimension(100, 50));
        background.add(newGame, BorderLayout.SOUTH);


        JLabel text = new JLabel("Game Over", SwingConstants.CENTER);
        text.setForeground(Color.WHITE);
        text.setFont(new Font("Serif", Font.PLAIN, 18));
        background.add(text, BorderLayout.CENTER);

        glass.add(background, BorderLayout.CENTER);

        glass.revalidate();
        glass.repaint();
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

        private boolean pauseState = false;
        private JPanel pauseScreen;

        private BlocksArea blocksArea;
        private int bagSize = 0;
        private final ArrayList<Tetrominoe> blocks = new ArrayList<>();
        private Tetrominoe block;
        private boolean initBlockBag = false;

        private HoldArea holdArea;
        private Tetrominoe heldBlock;
        private boolean switchBlock;    //A switch variable to ensure player only exchanges held block once

        private int theoreticalDropY = 0;   //A variable to hold destination y coordinate for block on grid

        /** CONSTRUCTOR -- Sets up the JPanel by defining size, border and all possible tetrominoes */
        public GameArea(HoldArea holdArea, BlocksArea blocksArea) {
            this.setPreferredSize(BOARD_PANEL_DIMENSION);
            this.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.black));

            this.holdArea = holdArea;
            this.blocksArea = blocksArea;

            initPauseScreen();
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
            im.put(KeyStroke.getKeyStroke("P"), "p");

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
            am.put("p", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!pauseState) {
                        pauseState = true;
                    } else pauseState = false;
                    setPauseScreen();
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

        public void initPauseScreen() {
            //Ensures that New JPanel is aligned on top of gameArea
            this.setLayout(new BorderLayout());

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(new Color(0,0,0,125));
            panel.setPreferredSize(BOARD_PANEL_DIMENSION);

            JLabel text = new JLabel("Paused", SwingConstants.CENTER);
            text.setForeground(Color.WHITE);

            panel.add(text);
            pauseScreen = panel;
        }

        public boolean getPauseState() {
            return pauseState;
        }

        private void setPauseScreen() {
            if (getPauseState()) {
                this.add(pauseScreen);
                this.repaint();
                this.revalidate();
            } else {
                this.remove(pauseScreen);
            }
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
            if (!initBlockBag) {
                initBlockBag = true;

                java.util.Collections.shuffle(blocks);

                //Grab block from list of shuffled blocks
                block = blocks.get(6);

                blocksArea.addBlock(new Tetrominoe(blocks.get(5).getShapeType(), blocks.get(5).getBoard()));
                blocksArea.addBlock(new Tetrominoe(blocks.get(4).getShapeType(), blocks.get(4).getBoard()));
                blocksArea.addBlock(new Tetrominoe(blocks.get(3).getShapeType(), blocks.get(3).getBoard()));

                bagSize = 3;

                block.spawn();
            } else {
                if (bagSize == 0) {
                    java.util.Collections.shuffle(blocks);
                    bagSize = 7;
                }

                bagSize--;
                block = blocksArea.removeBlock();
                blocksArea.addBlock(new Tetrominoe(blocks.get(bagSize).getShapeType(), blocks.get(bagSize).getBoard()));

                block.spawn();
            }

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
        public void moveBlockDown() {

            if (!checkBottom()) {
                block.moveDown();
                repaint();
            }
        }

        public boolean checkBottom() {
            int newY = block.getY() + 1;

            //Check for Collision when moving down
            if (tetrisGrid.checkBlockCollision(block.getTP(), block.getX(), newY)) {
                return true;
            }

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
                holdArea.setDrawBlock(heldBlock.getShapeType());

                block.spawn();

                switchBlock = false;
            }

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

    /** BlocksArea Class -- Create a Panel that displays the upcoming blocks */
    public class BlocksArea extends JPanel {

        //Initialize Variables
        private int gridColumns;
        private int gridCellSize;
        private int gridRows;

        private LinkedList<Tetrominoe> nextBlocks = new LinkedList<>();

        public BlocksArea() {
            this.setPreferredSize(BLOCKS_PANEL_DIMENSION);
            this.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.black));

            validate();
        }

        public void initBlocksGrid() {
            this.gridColumns = 7;
            this.gridCellSize = this.getWidth() / gridColumns;
            this.gridRows = this.getHeight() / gridCellSize;
        }

        public void addBlock(Tetrominoe t) {
            nextBlocks.add(t);
            repaint();
        }

        public Tetrominoe removeBlock() {
            return nextBlocks.poll();
        }

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
    }

    /** HoldArea Class -- Create a Panel that displays the block being held */
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

        public void setHoldGrid(int gridColumns) {
            this.gridColumns = gridColumns;
            this.gridCellSize = this.getWidth() / gridColumns;
            this.gridRows = this.getHeight() / gridCellSize;
        }

        public void setDrawBlock(Tetrominoe.ShapeType shapeType) {
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
    }
}
