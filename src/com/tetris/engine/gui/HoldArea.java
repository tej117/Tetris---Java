package com.tetris.engine.gui;

import com.tetris.engine.model.tetrominoes.Tetrominoe;

import javax.swing.*;
import java.awt.*;

/** HoldArea Class -- Create a Panel that displays the block being held */
public class HoldArea extends JPanel {

    //Initialize Variables
    private int gridColumns;
    private int gridCellSize;
    private int gridRows;

    private Tetrominoe block;

    private GameScreen gameScreen;

    private int blockX = 0;
    private int blockY = 0;

    public HoldArea(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        this.setPreferredSize(this.gameScreen.HOLD_PANEL_DIMENSION);
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
