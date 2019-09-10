package game_logic;

import java.awt.geom.Point2D;

public abstract class Block {

    private Point2D[] cells;
    private int type;


    private void moveLeft(){

    }

    private void moveRight(){
    }

    private void moveDown(){
        for(int[] cell : cells) {
            cell[1] -= 1;
        }
    }

    public abstract void turnLeft();

    public abstract void turnRight();
}
