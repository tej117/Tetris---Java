package com.tetris.engine.gui;

import com.tetris.engine.model.tetrominoes.Tetrominoe;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/** BlocksArea Class -- Create a Panel that displays the upcoming blocks */
public class QueueArea extends JPanel {

    //Initialize Variables
    private int gridColumns;
    private int gridCellSize;
    private int gridRows;

    private GameScreen gameScreen;

    private LinkedList<Tetrominoe> nextBlocks = new LinkedList<>();

    public QueueArea(GameScreen gameForm) {
        this.gameScreen = gameForm;

        this.setPreferredSize(this.gameScreen.BLOCKS_PANEL_DIMENSION);
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
