/**
 * File:        GameScreen.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2023
 *
 * Summary of File:
 *      This file contains code which creates the Java Swing GUI Interface
 *      for the Tetris Game. The file contains the GameScreen class which makes use
 *      of three other classes: GameArea, HoldArea and QueueArea which are components
 *      placed within the GameScreen display screen.
 */

package com.tetris.engine.gui;

import com.tetris.engine.logic.MarathonController;

import javax.swing.*;
import java.awt.*;

/** GameScreen Class -- Create the screen that displays the game (GUI) */
public class GameScreen {

    //Initialize Variables - JComponents
    private final JFrame gameFrame;
    //private final JMenuBar gameScreenMenuBar;
    private final JLabel level;
    private final JLabel score;
    private final JLabel linesCleared;

    //Initialize Variables - Custom GUI Components (JPanel)
    private final GameArea gameArea;
    private final HoldArea holdArea;
    private final QueueArea queueArea;

    //Initialize Variables - Controllers
    private final MarathonController mc;

    //Initialize Variables - Dimensions of the GUI Components
    protected final static Dimension BOARD_PANEL_DIMENSION = new Dimension(200, 400);
    protected final static Dimension OUTER_FRAME_DIMENSION = new Dimension(400,550);
    protected final static Dimension HOLD_PANEL_DIMENSION = new Dimension(50, 50);
    protected final static Dimension BLOCKS_PANEL_DIMENSION = new Dimension(50, 150);

    /** CONSTRUCTOR -- Sets up the JFrame by appropriately placing the containers in the frame */
    public GameScreen(MarathonController mc) {
        this.mc = mc;

        //Initialize Game Components
        this.gameFrame = new JFrame("JTetris");
        //this.gameScreenMenuBar = createGameScreenMenuBar();
        this.score = new JLabel();
        this.level = new JLabel();
        this.linesCleared = new JLabel();
        this.holdArea = new HoldArea(this);
        this.queueArea = new QueueArea(this);
        this.gameArea = new GameArea(this, mc);

        //Set up the Game Screen (The layout)
        initGameScreen();

        //Update labels to initial state
        updateScore(0);
        updateLevel(1);

        this.holdArea.setHoldGrid(1); //Rename this function to initHoldGrid
        this.queueArea.initBlocksGrid();

        //Default InputMap in GameArea only works if gameArea JPanel has focus
        this.gameArea.setFocusable(true);
    }

    /** Initialize Screen Layout  */
    private void initGameScreen() {
        //Layout of JFrame (GridBagLayout)
        this.gameFrame.setLayout(new GridBagLayout());

        //Set the JMenuBar to the JFrame
        //this.gameFrame.setJMenuBar(gameScreenMenuBar);

        //Set up the dimensions of the JLabels
        level.setPreferredSize(new Dimension(50, 10));
        score.setPreferredSize(new Dimension(50, 10));
        linesCleared.setPreferredSize(new Dimension(50, 50));
        linesCleared.setFont(new Font("Serif", Font.PLAIN, 18));

        //Add the JLabels and JPanel onto the JFrame GridBagLayout
        this.addItem(gameFrame, score, 0, 0, GridBagConstraints.REMAINDER, 1,0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH);
        this.addItem(gameFrame, level, 0, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH);
        this.addItem(gameFrame, holdArea, 0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH);
        this.addItem(gameFrame, gameArea, 1, 2, 1, 3, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH);
        this.addItem(gameFrame, queueArea, 2, 2, 1, 2, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH);
        this.addItem(gameFrame, linesCleared, 2, 4, GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        this.addItem(gameFrame, Box.createGlue(), 1, 5,1,GridBagConstraints.REMAINDER, 0.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH);

        //this.gameFrame.pack();

        //Set the parameters of the GameFrame
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setVisible(true);
        this.gameFrame.setResizable(false);
    }

    /** GETTER METHODS */
    public GameArea getGameArea() {
        return this.gameArea;
    }
    public HoldArea getHoldArea() {
        return this.holdArea;
    }
    public QueueArea getQueueArea() {
        return this.queueArea;
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

    /**
     * Description: Create a game over screen which is displayed over the final state of the current game.
     */
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

    private JMenuBar createGameScreenMenuBar() {
        return null;
    }
}
