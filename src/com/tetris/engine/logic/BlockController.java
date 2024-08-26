package com.tetris.engine.logic;

import com.tetris.engine.gui.GameArea;
import com.tetris.engine.gui.HoldArea;
import com.tetris.engine.gui.QueueArea;
import com.tetris.engine.model.board.Board;
import com.tetris.engine.model.tetrominoes.Tetrominoe;
import com.tetris.engine.model.tetrominoes.TetrominoeCollection;

import java.util.ArrayList;

public class BlockController {

    private final GameArea gameArea;
    private final HoldArea holdArea;
    private final QueueArea queueArea;
    private final Board tetrisGrid;

    private int bagSize = 0;
    private final ArrayList<Tetrominoe> blocks = new ArrayList<>();
    private boolean blockBagEmpty = true;

    //Initialize Variables - Current Block Properties
    private Tetrominoe block;
    private int theoreticalDropY = 0;   //A variable to hold destination y coordinate for block on grid

    //Initialize Variables - Hold Area Properties
    private Tetrominoe heldBlock;
    private boolean switchBlock;    //A switch variable to ensure player only exchanges held block once

    public BlockController(GameArea gameArea, HoldArea holdArea, QueueArea queueArea, Board tetrisGrid) {
        this.gameArea = gameArea;
        this.holdArea = holdArea;
        this.queueArea = queueArea;
        this.tetrisGrid = tetrisGrid;

        this.initBlockBag();
    }

    private void initBlockBag () {
        blocks.add(new Tetrominoe(TetrominoeCollection.ISHAPE, tetrisGrid));
        blocks.add(new Tetrominoe(TetrominoeCollection.JSHAPE, tetrisGrid));
        blocks.add(new Tetrominoe(TetrominoeCollection.LSHAPE, tetrisGrid));
        blocks.add(new Tetrominoe(TetrominoeCollection.OSHAPE, tetrisGrid));
        blocks.add(new Tetrominoe(TetrominoeCollection.SSHAPE, tetrisGrid));
        blocks.add(new Tetrominoe(TetrominoeCollection.TSHAPE, tetrisGrid));
        blocks.add(new Tetrominoe(TetrominoeCollection.ZSHAPE, tetrisGrid));
    }

    /** GETTER METHODS */
    public Tetrominoe getBlock() {
        return this.block;
    }
    public int getTheoreticalDropY() {
        return theoreticalDropY;
    }

    /** SETTER METHODS */
    public void setBlock(Tetrominoe block) {
        this.block = block;
    }
    public void setTheoreticalDropY(int y) {
        this.theoreticalDropY = y;
    }

    /** MOVE BLOCKS */
    public void moveBlockRight() {
        if (block == null) return;

        int newX = block.getX()+1;

        //Check for Collision when moving to the right
        if (tetrisGrid.checkBlockCollision(block.getTP(), newX, block.getY())) return;

        block.moveRight();
        updateDropPosition();

        gameArea.repaint();
    }
    public void moveBlockLeft() {
        if (block == null) return;

        int newX = block.getX() - 1;

        //Check for Collision when moving to the left
        if (tetrisGrid.checkBlockCollision(block.getTP(), newX, block.getY())) return;

        block.moveLeft();
        updateDropPosition();

        gameArea.repaint();
    }
    public void hardDrop() {
        if (block == null) return;

        int newY = block.getY()+1;

        //Check for Collision when dropping block
        while (!tetrisGrid.checkBlockCollision(block.getTP(), block.getX(), newY)) {
            block.moveDown();
            newY++;
        }
        gameArea.repaint();
    }
    public void softDrop() {
        moveBlockDown();
    }
    public void rotateBlock(int direction) {
        if (block == null) return;

        block.rotate(direction);
        updateDropPosition();

        gameArea.repaint();
    }
    public void moveBlockDown() {

        if (!checkBottom()) {
            block.moveDown();
            gameArea.repaint();
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

    /** Description: Perform a collision check to find where the block will potentially drop and save the y value */
    public void updateDropPosition() {
        if (block == null) return;

        theoreticalDropY = block.getY() + 1;

        while (!tetrisGrid.checkBlockCollision(block.getTP(), block.getX(), theoreticalDropY)) {
            theoreticalDropY++;
        }

        theoreticalDropY--;
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
        if (blockBagEmpty) {
            blockBagEmpty = false;

            java.util.Collections.shuffle(blocks);

            //Grab block from list of shuffled blocks
            block = blocks.get(6);

            queueArea.addBlock(new Tetrominoe(blocks.get(5).getShapeType(), blocks.get(5).getBoard()));
            queueArea.addBlock(new Tetrominoe(blocks.get(4).getShapeType(), blocks.get(4).getBoard()));
            queueArea.addBlock(new Tetrominoe(blocks.get(3).getShapeType(), blocks.get(3).getBoard()));

            bagSize = 3;

            block.spawn();
        } else {
            if (bagSize == 0) {
                java.util.Collections.shuffle(blocks);
                bagSize = 7;
            }

            bagSize--;
            block = queueArea.removeBlock();
            queueArea.addBlock(new Tetrominoe(blocks.get(bagSize).getShapeType(), blocks.get(bagSize).getBoard()));

            block.spawn();
        }

        gameArea.setBlock(block);
        updateDropPosition();
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

            gameArea.repaint();
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

        gameArea.setBlock(block);
        updateDropPosition();
        gameArea.repaint();
    }

    /** Description: Checks to see if the game is over */
    public boolean isBlockOutOfBounds() {
        if(block.getY() < 0) {
            block = null;
            return true;
        }
        return false;
    }
}
