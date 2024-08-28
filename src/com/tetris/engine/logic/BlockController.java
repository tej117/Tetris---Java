/**
 * File:        BlockController.java
 *
 * Author:      Simran Cheema
 * Date:        Summer 2024
 *
 * Summary of File:
 *      This file is a controller that has all block logic required for the game. It uses an eventDispatcher
 *      to pass information necessary into the GUI classes to draw movement onto the screen.
 *
 */

package com.tetris.engine.logic;

import com.tetris.engine.event.GameAreaEvent;
import com.tetris.engine.event.GameEventDispatcher;
import com.tetris.engine.event.HoldAreaEvent;
import com.tetris.engine.event.QueueAreaEvent;
import com.tetris.engine.model.board.Board;
import com.tetris.engine.model.tetrominoes.Tetrominoe;
import com.tetris.engine.model.tetrominoes.TetrominoeCollection;

import java.util.ArrayList;
import java.util.LinkedList;

/** BlockController Class -- Block Controller which contains all block logic */
public class BlockController {

    //Initialize Variables - 'Mode' Controller
    private final GameEventDispatcher gameEventDispatcher;
    private final Board tetrisGrid;

    //Initialize Variables - Spawning blocks
    private int bagSize = 0;
    private final ArrayList<Tetrominoe> blockBag = new ArrayList<>();
    private boolean blockBagEmpty = true;

    //Initialize Variables - Current Block Properties
    private Tetrominoe currentBlock;
    private int theoreticalDropY = 0;   //A variable to hold potential y coordinate for block on grid

    //Initialize Variables - Hold Area Properties
    private Tetrominoe heldBlock;
    private boolean switchBlock;    //A switch variable to ensure player only exchanges held block once

    //Initialize Variables - Queue Area Properties
    private final LinkedList<Tetrominoe> queuedBlocks = new LinkedList<>();

    /** CONSTRUCTOR -- Store 'Mode' Controller variables and initialize block bag */
    public BlockController(GameEventDispatcher gameEventDispatcher, Board tetrisGrid) {
        this.gameEventDispatcher = gameEventDispatcher;
        this.tetrisGrid = tetrisGrid;

        this.initBlockBag();
    }

    /** Description: Create each tetrominoe block and put in blockBag*/
    private void initBlockBag () {
        blockBag.add(new Tetrominoe(TetrominoeCollection.ISHAPE, tetrisGrid));
        blockBag.add(new Tetrominoe(TetrominoeCollection.JSHAPE, tetrisGrid));
        blockBag.add(new Tetrominoe(TetrominoeCollection.LSHAPE, tetrisGrid));
        blockBag.add(new Tetrominoe(TetrominoeCollection.OSHAPE, tetrisGrid));
        blockBag.add(new Tetrominoe(TetrominoeCollection.SSHAPE, tetrisGrid));
        blockBag.add(new Tetrominoe(TetrominoeCollection.TSHAPE, tetrisGrid));
        blockBag.add(new Tetrominoe(TetrominoeCollection.ZSHAPE, tetrisGrid));
    }

    /** GETTER METHODS */
    public Tetrominoe getCurrentBlock() {
        return this.currentBlock;
    }

    /** MOVE BLOCKS */
    public void moveBlockRight() {
        if (currentBlock == null) return;

        int newX = currentBlock.getX()+1;

        //Check for Collision when moving to the right
        if (tetrisGrid.checkBlockCollision(currentBlock.getTP(), newX, currentBlock.getY())) return;

        currentBlock.moveRight();
        updateDropPosition();

        gameEventDispatcher.dispatchEvent(new GameAreaEvent(this.currentBlock, this.theoreticalDropY));
    }
    public void moveBlockLeft() {
        if (currentBlock == null) return;

        int newX = currentBlock.getX() - 1;

        //Check for Collision when moving to the left
        if (tetrisGrid.checkBlockCollision(currentBlock.getTP(), newX, currentBlock.getY())) return;

        currentBlock.moveLeft();
        updateDropPosition();

        gameEventDispatcher.dispatchEvent(new GameAreaEvent(this.currentBlock, this.theoreticalDropY));
    }
    public void hardDrop() {
        if (currentBlock == null) return;

        int newY = currentBlock.getY()+1;

        //Check for Collision when dropping block
        while (!tetrisGrid.checkBlockCollision(currentBlock.getTP(), currentBlock.getX(), newY)) {
            currentBlock.moveDown();
            newY++;
        }
        gameEventDispatcher.dispatchEvent(new GameAreaEvent(this.currentBlock, this.theoreticalDropY));
    }
    public void softDrop() {
        moveBlockDown();
    }
    public void rotateBlock(int direction) {
        if (currentBlock == null) return;

        currentBlock.rotate(direction);
        updateDropPosition();

        gameEventDispatcher.dispatchEvent(new GameAreaEvent(this.currentBlock, this.theoreticalDropY));
    }
    public void moveBlockDown() {

        if (!checkBottom()) {
            currentBlock.moveDown();
            gameEventDispatcher.dispatchEvent(new GameAreaEvent(this.currentBlock, this.theoreticalDropY));
        }
    }

    /** Description: Check if block has reached the bottom
     *  Return: True  - If reached the bottom
     *          False - If block hasn't reached the bottom
     */
    public boolean checkBottom() {
        int newY = currentBlock.getY() + 1;

        //Check for Collision when moving down
        if (tetrisGrid.checkBlockCollision(currentBlock.getTP(), currentBlock.getX(), newY)) {
            return true;
        }

        return false;
    }

    /** Description: Perform a collision check to find where the block will potentially drop and save the y value */
    public void updateDropPosition() {
        if (currentBlock == null) return;

        theoreticalDropY = currentBlock.getY() + 1;

        while (!tetrisGrid.checkBlockCollision(currentBlock.getTP(), currentBlock.getX(), theoreticalDropY)) {
            theoreticalDropY++;
        }

        theoreticalDropY--;
    }

    /** SPAWN BLOCK */
    public void spawnBlock() {
        //Reset variables for newly spawned block
        if (currentBlock != null) {
            currentBlock.reset();
            currentBlock = null;
        }

        switchBlock = true; //Reset switchBlock to potentially hold the next block
        theoreticalDropY = 0;

        //If bag of blocks is empty, shuffle blocks again
        if (blockBagEmpty) {
            blockBagEmpty = false; //switch

            java.util.Collections.shuffle(blockBag); //shuffle the blockBag

            //Grab block from list of shuffled blocks
            currentBlock = blockBag.get(6);

            //Blocks to get queued up
            Tetrominoe block1 = new Tetrominoe(blockBag.get(5).getShapeType(), blockBag.get(5).getBoard());
            Tetrominoe block2 = new Tetrominoe(blockBag.get(4).getShapeType(), blockBag.get(4).getBoard());
            Tetrominoe block3 = new Tetrominoe(blockBag.get(3).getShapeType(), blockBag.get(3).getBoard());

            //Events with block data to queue area
            gameEventDispatcher.dispatchEvent(new QueueAreaEvent(block1, false));
            gameEventDispatcher.dispatchEvent(new QueueAreaEvent(block2, false));
            gameEventDispatcher.dispatchEvent(new QueueAreaEvent(block3, false));

            //Add the blocks to the queued list
            queuedBlocks.add(block1);
            queuedBlocks.add(block2);
            queuedBlocks.add(block3);

            //Size of bag after 4 blocks have gotten taken out
            bagSize = 3;

            currentBlock.spawn();
        } else {
            if (bagSize == 0) { //Gone through the entire bag and needs to be re-shuffled
                java.util.Collections.shuffle(blockBag);
                bagSize = 7;
            }

            bagSize--;

            //Remove first block in queue
            currentBlock = queuedBlocks.poll();

            //Next Block to Queue
            Tetrominoe newBlock = new Tetrominoe(blockBag.get(bagSize).getShapeType(), blockBag.get(bagSize).getBoard());

            //Update the queue in QueueArea
            gameEventDispatcher.dispatchEvent(new QueueAreaEvent(newBlock, true));

            //Add the block to the queue list
            queuedBlocks.add(newBlock);

            currentBlock.spawn();
        }

        updateDropPosition();
        gameEventDispatcher.dispatchEvent(new GameAreaEvent(this.currentBlock, this.theoreticalDropY));
    }

    /** Description: Hold the block (if no blocks are currently held) or switch held block with block on grid.
     *               Also checks to make sure that the player switches the block only once per new spawned block*/
    public void holdBlock() {

        //Game newly starts
        if (heldBlock == null) {
            heldBlock = new Tetrominoe(currentBlock.getShapeType());

            gameEventDispatcher.dispatchEvent(new HoldAreaEvent(heldBlock));

            //Spawn a new block
            spawnBlock();

            switchBlock = false; //Cannot switch again until spawn new block

            gameEventDispatcher.dispatchEvent(new GameAreaEvent(this.currentBlock, this.theoreticalDropY));
            return;
        }

        //Check that player hasn't already switched a block
        if (switchBlock) {

            //Switch between holding block and block on grid
            Board tempBoard = currentBlock.getBoard();

            Tetrominoe temp = new Tetrominoe(heldBlock.getShapeType());
            heldBlock = new Tetrominoe(currentBlock.getShapeType());
            currentBlock = temp;
            currentBlock.setBoard(tempBoard);

            currentBlock.spawn();

            switchBlock = false; //Cannot switch again until spawn new block

            gameEventDispatcher.dispatchEvent(new HoldAreaEvent(heldBlock));
        }

        updateDropPosition();
        gameEventDispatcher.dispatchEvent(new GameAreaEvent(this.currentBlock, this.theoreticalDropY));
    }

    /** Description: Checks to see if the game is over */
    public boolean isBlockOutOfBounds() {
        if(currentBlock.getY() < 0) {
            currentBlock = null;
            return true;
        }
        return false;
    }
}
