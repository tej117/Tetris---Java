package com.tetris.engine.gui;

import com.tetris.engine.logic.MarathonController;
import com.tetris.engine.model.board.Board;
import com.tetris.engine.model.tetrominoes.Tetrominoe;

import javax.swing.*;
import java.awt.*;

/** GameArea Class -- Create a Panel that displays the game */
public class GameArea extends JPanel {

    //Initialize Variables
    private Tetrominoe block;
    private Board tetrisGrid;
    private int gridCellSize;
    private int gridColumns;
    private int gridRows;

    //Initialize Variables - Pause Screen (Move to a Controller)
    private JPanel pauseScreen;

    private GameScreen gameScreen;
    private MarathonController mc;

    /** CONSTRUCTOR -- Sets up the JPanel by defining size, border and all possible tetrominoes */
    public GameArea(GameScreen gameScreen, MarathonController mc) {
        this.gameScreen = gameScreen;
        this.mc = mc;

        this.setPreferredSize(gameScreen.BOARD_PANEL_DIMENSION);
        this.setBorder(BorderFactory.createMatteBorder(2,2,2,2, Color.black));

        initPauseScreen();
        validate();
    }

    public void setBlock(Tetrominoe block) {
        this.block = block;
    }
    public void setBoardProperties(Board tetrisGrid) {
        this.tetrisGrid = tetrisGrid;
        gridCellSize = tetrisGrid.getGridCellSize();
        gridColumns = tetrisGrid.getGridColumns();
        gridRows = tetrisGrid.getGridRows();
    }

    public void initPauseScreen() {
        //Ensures that New JPanel is aligned on top of gameArea
        this.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0,0,0,125));
        panel.setPreferredSize(gameScreen.BOARD_PANEL_DIMENSION);

        JLabel text = new JLabel("Paused", SwingConstants.CENTER);
        text.setForeground(Color.WHITE);

        panel.add(text);
        pauseScreen = panel;
    }

    public void setPauseScreen(boolean pauseState) {
        if (pauseState) {
            this.add(pauseScreen);
            this.repaint();
            this.revalidate();
        } else {
            this.remove(pauseScreen);
        }
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
    private void drawTheoreticalDropPosition(Graphics g, int theoreticalDropY) {
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
        if (mc.getBlockController().getBlock() != null) {
            drawTheoreticalDropPosition(g, mc.getBlockController().getTheoreticalDropY());
            drawBlock(g);
        }
    }

}
