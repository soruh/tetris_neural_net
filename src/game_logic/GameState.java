package game_logic;

import java.util.Random;

public class GameState {

    private int[][] state, previousState;
    private Block currentBlock, nextBlock;
    private long ticks;
    private static int[] startPosition = {4, 19};

    Random r;

    public GameState(long pRandomSeed){
        state = new int[22][10];
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
        currentBlock = nextBlock;
        nextBlock = new Block(r.nextInt(7));
        currentBlock.setPosition(this.startPosition);
    }

    public void applyAction(Action pAction){
        switch (pAction){
            case NOTHING: break;
            case LEFT: if(this.checkActionValid(pAction)) currentBlock.setPosition(currentBlock.getPosition()[0] - 1, currentBlock.getPosition()[1]); break;
            case RIGHT: if(this.checkActionValid(pAction)) currentBlock.setPosition(currentBlock.getPosition()[0] + 1, currentBlock.getPosition()[1]); break;
            case DOWN: if(this.checkActionValid(pAction)) currentBlock.setPosition(currentBlock.getPosition()[0], currentBlock.getPosition()[1] - 1); break;
            case TURN_LEFT: if(this.checkActionValid(pAction)) currentBlock.rotateLeft(); break;
            case TURN_RIGHT: if(this.checkActionValid(pAction)) currentBlock.rotateRight(); break;
        }
    }

    public boolean isValid(){
        return True;
    }

    public boolean checkActionValid(Action pAction){
        return True;
    }

    public boolean detectCollision(){

    }

    public int[][] simplifyState(){
        int[][] simplifiedState = state;
        for (int[] line: simplifiedState) {
            for(int cell: line){
                if(cell > 0) cell = 1;
            }
        }

        return simplifiedState;
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
