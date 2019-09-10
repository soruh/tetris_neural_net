package game_logic;

import java.util.Random;

public class GameState {

    private int[][] state, previousState;
    private Block currentBlock, nextBlock;
    private long ticks;
    private static int[] startPosition = {4, 19};
    private boolean terminated;
    private long score;

    Random rng;

    public GameState(long pRandomSeed){
        state = new int[22][10];
        ticks = 0;
        score = 0;
        terminated = false;

        rng = new Random(pRandomSeed);

        currentBlock = randomBlock();
        currentBlock.setPosition(startPosition);
        nextBlock = randomBlock();
    }


    public void spawnBlock(){
        currentBlock = nextBlock;
        nextBlock = new Block(rng.nextInt(7));
        currentBlock.setPosition(this.startPosition);
        if(detectCollision(currentBlock.getAbsoluteCells())) terminated = true;
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
                    spawnBlock();
                }

                break;
            }
        }
    }

    private Block randomBlock(){
        return new Block(rng.nextInt(7));
    }

    public void spawn (int pBlock){
        currentBlock = nextBlock;
        currentBlock.setPosition(this.startPosition);

        nextBlock = randomBlock();
    }

    public void placeBlock() {
        int[][] cells = currentBlock.getAbsoluteCells();

        for (int[] cell : cells) {
            int x = cell[0];
            int y = cell[1];

            this.state[x][y] = currentBlock.getType();
        }
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

            if(x < 0 || x >= state.length) return true;
            if(y < 0 || y >= state[0].length) return true;


            if(state[x][y] != 0) return true;
        }

        return false;
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
