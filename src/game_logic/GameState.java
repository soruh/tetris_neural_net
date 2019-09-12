package game_logic;

import java.util.Random;

public class GameState {

    private int[][] grid;
    private Block currentBlock, nextBlock;
    private long ticks;
    private static int[] startPosition = {20, 4};
    private boolean terminated;
    private int score;
    private boolean heldPiece;
    private int level;
    private int blocksPlaced = 0;

    Random rng;


    public GameState(Random _rng){
        grid = new int[22][10];
        ticks = 0;
        score = 0;
        level = 0;
        terminated = false;
        heldPiece = false;

        rng = _rng;

        currentBlock = randomBlock();
        currentBlock.setPosition(startPosition);
        nextBlock = randomBlock();
    }


    public int getBlocksPlaced() { return blocksPlaced; }


    public void spawnBlock(){
        currentBlock = nextBlock;
        nextBlock = new Block(rng.nextInt(7) + 1);
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

                    breakRows();

                    spawnBlock();
                }

                break;
            }
            case HOLD_PIECE: {
                if(heldPiece) break;


                Block tmp = currentBlock;
                currentBlock = nextBlock;
                nextBlock = tmp;

                heldPiece = true;
            }
        }
    }

    private int scoreForNRows(int rowsBroken, int scoreMultiplier) {
        int rawScore = 0;
        switch (rowsBroken) {
            case 1: rawScore = 2; break;
            case 2: rawScore = 5; break;
            case 3: rawScore = 15; break;
            case 4: rawScore = 60; // break;
        }

        return 20 * rawScore * scoreMultiplier;
    }

    private void breakRows() {
        int rowsBroken = 0;
        int consecutiveRowsBroken = 0;

        for(int rowIndex=0;rowIndex<grid.length;rowIndex++) {
            int[] row = grid[rowIndex];

            boolean hasGap = false;
            for (int cell : row) {
                if(cell == 0) {
                    hasGap = true;
                    break;
                }
            }

            if(hasGap) {
                if(consecutiveRowsBroken > 0) {
                    rowsBroken += consecutiveRowsBroken;

                    rowIndex -= consecutiveRowsBroken;

                    sliceRows(rowIndex, consecutiveRowsBroken);

                    consecutiveRowsBroken = 0;
                }
            }else{
                consecutiveRowsBroken++;
            }
        }

        if(rowsBroken > 0) {
            int scoreDelta = scoreForNRows(rowsBroken, level + 1);

            score += scoreDelta;
        }
    }

    private void sliceRows(int rowIndex, int nRows) {
        int offsetRow;
        for (int i = rowIndex; i < grid.length; i++) {
            offsetRow = i + nRows;
            if(offsetRow < grid.length) {
                grid[i] = grid[i + nRows];
            } else {
                grid[i] = new int[grid[0].length];
            }
        }
    }

    private Block randomBlock() {
        /*
        for(int i=0;i<100;i++){
            System.out.print(", "+ (rng.nextInt(7) +1));
        }
        System.exit(-1);
        */
        int[] blocks = {6, 5, 4, 7, 1, 3, 7, 1, 1, 7, 2, 5, 5, 2, 7, 7, 4, 5, 2, 1, 6, 6, 1, 4, 5, 6, 2, 6, 1, 7, 7, 3, 1, 3, 2, 6, 7, 4, 3, 6, 5, 1, 4, 3, 7, 5, 7, 5, 5, 7, 7, 1, 7, 7, 7, 5, 4, 1, 5, 2, 5, 2, 5, 2, 4, 1, 4, 4, 1, 5, 1, 4, 7, 2, 7, 5, 1, 4, 1, 7, 7, 6, 3, 4, 4, 1, 7, 7, 5, 4, 1, 2, 7, 7, 7, 4, 5, 6, 7, 7};
        return new Block(blocks[blocksPlaced % blocks.length]);
    }

    public void spawn (int pBlock){
        currentBlock = nextBlock;
        currentBlock.setPosition(this.startPosition);

        nextBlock = randomBlock();
    }

    public void placeBlock() {
        blocksPlaced++;

        int[][] cells = currentBlock.getAbsoluteCells();

        heldPiece = false;

        for (int[] cell : cells) {
            int y = cell[0];
            int x = cell[1];

            this.grid[y][x] = currentBlock.getType();
        }
    }


    public boolean moveIfValid(int dx, int dy, int dr){
        int[] oldPosition = currentBlock.getPosition();
        int[] newPosition = new int[]{oldPosition[0] + dy, oldPosition[1] + dx};

        int oldRotation = currentBlock.getRotation();

        currentBlock.setRotation((oldRotation + dr + 4) % 4);
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
            int y = cell[0];
            int x = cell[1];

            if(y < 0 || y >= grid.length) return true;
            if(x < 0 || x >= grid[0].length) return true;


            if(grid[y][x] != 0) return true;
        }

        return false;
    }

    public int[][] simplifyState(){
        int[][] simplifiedState = new int[grid.length - 2][grid[0].length];


        for (int i=0;i<grid.length - 2;i++) {
            for(int j=0;j<grid[i].length - 2;j++){
                simplifiedState[i][j] = grid[i][j] > 0 ? 1 : 0;
            }
        }
        return simplifiedState;
    }

    public double[] flattenedState(){
        int[][] simplifiedState = this.simplifyState();
        double[] flattenedState = new double[8 +simplifiedState.length * simplifiedState[0].length];

        int[][] absoluteCells = currentBlock.getAbsoluteCells();
        for(int i=0;i<absoluteCells.length;i++){
            for(int j=0;j<absoluteCells[0].length;j++){
                flattenedState[2*i + j] = absoluteCells[i][j];
            }
        }


        for (int i = 0; i < simplifiedState.length; i++) {
            for (int j = 0; j < simplifiedState[0].length; j++) {
                flattenedState[8 + j + i*simplifiedState[0].length] = simplifiedState[i][j];
            }
        }


        return flattenedState;
    }

    public long getTicks() { return ticks; }

    public boolean getTerminated(){ return this.terminated; }

    public int getScore(){ return this.score; }

    public int[][] getGrid(){ return  this.grid; }

    public void setTicks(long pTicks) { this.ticks = pTicks; }

    public void incrementTicks(){ this.ticks ++; }

    public Block getCurrentBlock() {
        return currentBlock;
    }
}
