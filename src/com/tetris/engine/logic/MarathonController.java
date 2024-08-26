package com.tetris.engine.logic;

import com.tetris.engine.GameData;
import com.tetris.engine.GameThread;
import com.tetris.engine.gui.GameArea;
import com.tetris.engine.gui.GameScreen;
import com.tetris.engine.gui.HoldArea;
import com.tetris.engine.gui.QueueArea;
import com.tetris.engine.model.board.Board;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MarathonController {

    //Initialize Variables - JPanels
    private final GameScreen gameScreen;
    private final GameArea gameArea;
    private final HoldArea holdArea;
    private final QueueArea queueArea;

    //Initialize Variables - Game Data
    private final GameData gameData;

    //Initialize Variables - Other Controllers
    private final BlockController blockController;

    //Initialize Variables - Tetris Grid
    private Board tetrisGrid;

    //Initialize Variables - Game States
    private boolean pauseState = false;

    /** CONSTRUCTOR -- Sets up GameScreen, GameData, GameBoard, BlockController and starts the game */
    public MarathonController () {
        //Set up game screen
        this.gameScreen = new GameScreen(this);
        this.gameArea = gameScreen.getGameArea();
        this.holdArea = gameScreen.getHoldArea();
        this.queueArea = gameScreen.getQueueArea();

        this.gameData = new GameData();

        initControls();

        //Construct initial state of Tetris Grid
        this.initTetrisGrid(this.gameArea.getWidth(), this.gameArea.getHeight());

        this.blockController = new BlockController(gameArea, holdArea, queueArea, tetrisGrid);

        //Begin game loop
        this.startGame(this.gameScreen);
    }

    /** GETTER METHODS */
    public boolean getPauseState() {
        return pauseState;
    }
    public BlockController getBlockController() {
        return blockController;
    }

    /** Description: Set up keyboard buttons by defining certain buttons with actions */
    private void initControls() {
        //LOOK INTO inputMap and ActionMap
        InputMap im = this.gameArea.getInputMap();
        ActionMap am = this.gameArea.getActionMap();

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
                blockController.moveBlockRight();
            }
        });
        am.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blockController.moveBlockLeft();
            }
        });
        am.put("space", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blockController.hardDrop();
            }
        });
        am.put("z", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blockController.rotateBlock(1);
            }
        });
        am.put("x", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blockController.rotateBlock(0);
            }
        });
        am.put("c", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blockController.holdBlock();
            }
        });
        am.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blockController.softDrop();
            }
        });
        am.put("p", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!pauseState) {
                    pauseState = true;
                } else pauseState = false;
                gameArea.setPauseScreen(pauseState);
            }
        });
    }

    /** Description: Initialize variables related to Tetris grid */
    public void initTetrisGrid(int width, int height) {
        tetrisGrid = new Board(width, height);
        gameArea.setBoardProperties(tetrisGrid);
    }

    /**
     * Description: Create a separate thread for the game loop so that it doesn't mess up the Main Thread
     * Parameters:  GameScreen gs ---- Gets passed into GameThread
     */
    public void startGame(GameScreen gs) {
        new GameThread(this, gs, this.blockController).start();
    }

    /** UPDATE GAME DATA AND GAMEFORM */
    public void updateScore(int score) {
        this.gameData.setScore(score);
        this.gameScreen.updateScore(score);
    }
    public void updateLevel(int level) {
        this.gameData.setLevel(level);
        this.gameScreen.updateLevel(level);
    }
    public void updateLinesCleared(String type) {
        this.gameScreen.updateLinesCleared(type);
    }

    /** Description: Clear completed lines */
    public int clearLines() {
        return this.gameArea.clearLines();
    }

    /** Description: Push block to background using method in board AKA UPDATE BOARD STATE*/
    public void moveBlockToBackground() {
        tetrisGrid.moveBlockToBackground(blockController.getBlock());
    }
}
