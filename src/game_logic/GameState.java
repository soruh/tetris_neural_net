package game_logic;

import java.util.Random;

public class GameState {

    private int[][] state, previousState;
    private int currentBlock, nextBlock;
    private long ticks;

    Random r;

    public GameState(long pRandomSeed){
        screen = new int[22][10];
        ticks = 0;

        r = new Random(pRandomSeed);
    }

    /*public void tick(Action pAction){
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

        ticks ++;
    }*/

    public void spawn (int pBlock){

    }

    public void applyAction(Action pAction){
        switch (pAction){
            case NOTHING: break;
            case LEFT: this.moveLeft(); break;
            case RIGHT: this.moveRight(); break;
            case DOWN: this. moveDown(); break;
            case TURN_LEFT: this.turnLeft(); break;
            case TURN_RIGHT: this.turnRight(); break;
        }
    }

    public boolean isValid(){
        return True;
    }

    public boolean detectCollision(){

    }



    public long getTicks() {
        return ticks;
    }

    public void setTicks(long pTicks) {
        this.ticks = pTicks;
    }

    public void incrementTicks(){
        this.ticks ++;
    }
}
