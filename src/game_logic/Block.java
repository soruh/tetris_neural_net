package game_logic;

import java.awt.geom.Point2D;

public abstract class Block {

    private int[][] cells;
    private int type;


    private void moveLeft(){
        for(int[] cell : cells){
            cell[0]  --;
        }
    }

    private void moveRight(){
        for(int[] cell : cells){
            cell[0]  ++;
        }
    }

    private void moveDown(){
        for(int[] cell : cells) {
            cell[1] -= 1;
        }
    }

    public abstract void turnLeft();

    public abstract void turnRight();
}
