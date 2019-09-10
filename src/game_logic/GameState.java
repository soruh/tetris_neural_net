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
            case LEFT: moveIfValid(-1, 0, 0); break;
            case RIGHT:  moveIfValid(1, 0, 0); break;
            case TURN_LEFT: moveIfValid(0, 0, -1); break;
            case TURN_RIGHT: moveIfValid(0, 0, 1); break;
            case DOWN: {
                boolean moved = moveIfValid(0, -1, 0);

                if(!moved) {
                    placeBlock();

                    // spawn?
                }

                break;
            }
        }
    }

    public void placeBlock(){

    }


    public boolean moveIfValid(int dx, int dy, int dr){
        int[] oldPosition = currentBlock.getPosition();
        int[] newPosition = new int[]{oldPosition[0] + dx, oldPosition[1] + dy};

        int oldRotation = currentBlock.getRotation();

        currentBlock.setRotation((oldRotation + dr) % 4);
        currentBlock.setPosition(newPosition);

        int[][] cells = currentBlock.getAbsoluteCells();

        if(detectCollision(cells)){
            // the move is invalid and the current block should not be updated
            currentBlock.setPosition(oldPosition);
            currentBlock.setRotation(oldRotation);

            return false;
        }

        return true;
    }

    public boolean detectCollision(int[][] cells){
        for(int[] cell : cells){
            int x = cell[0];
            int y = cell[1];

            if(x < 0 || x >= state.length) return false;
            if(y < 0 || y >= state[0].length) return false;


            if(state[x][y] != 0) return false;
        }

        return true;
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
