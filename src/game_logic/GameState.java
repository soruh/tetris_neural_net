package Tetris;

import java.util.Random;

public class GameState {

    private int[][] screen, nextScreen;
    private int currentBlock, nextBlock;
    private long tick;

    Random r = new Random();

    public GameState(){
        screen = new int[22][10];
        tick = 0;
    }

    public void tick(Action pAction){
        if(currentBlock == 0){
            currentBlock = nextBlock;
            nextBlock = r.nextInt(7) + 1;
            this.spawn(currentBlock);
        }

        switch(pAction){
            case NOTHING: break;
            case LEFT: checkActionValid(pAction); break;
            case RIGHT: break;

        }

        tick ++;
    }

    public void spawn (int pBlock){

    }

    private boolean checkActionValid(Action pAction){

    }
}
