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

    Random rng;


    public GameState(long pRandomSeed){
        grid = new int[22][10];
        ticks = 0;
        score = 0;
        level = 0;
        terminated = false;
        heldPiece = false;

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
            case 1: rawScore = 2;
            case 2: rawScore = 5;
            case 3: rawScore = 15;
            case 4: rawScore = 60;
        }

        return rawScore * scoreMultiplier;
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

        int scoreMultiplier = 20 * (level + 1);

        score += scoreForNRows(rowsBroken, scoreMultiplier);
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

    private Block randomBlock(){
        return new Block(rng.nextInt(7) + 1);
    }

    public void spawn (int pBlock){
        currentBlock = nextBlock;
        currentBlock.setPosition(this.startPosition);

        nextBlock = randomBlock();
    }

    public void placeBlock() {
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
            int y = cell[0];
            int x = cell[1];

            if(x < 0 || y >= grid.length) return true;
            if(y < 0 || x >= grid[0].length) return true;


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
        for(int[] cell: currentBlock.getAbsoluteCells()){
            int y = cell[0];
            int x = cell[1];
            if(x>=0 && x<simplifiedState[0].length && y>=0 && y<simplifiedState.length) {
                simplifiedState[y][x] = -1;
            }
        }
        return simplifiedState;
    }

    public double[] flattenedState(){
        int[][] simplifiedState = this.simplifyState();
        double[] flattenedState = new double[simplifiedState.length * simplifiedState[0].length];
        for (int i = 0; i < simplifiedState.length; i++) {
            for (int j = 0; j < simplifiedState[0].length; j++) {
                flattenedState[(i*simplifiedState[0].length) + j] = simplifiedState[i][j];
            }
        }
        return  flattenedState;
    }

    public long getTicks() { return ticks; }

    public boolean getTerminated(){ return this.terminated; }

    public int getScore(){ return this.score; }

    public int[][] getGrid(){ return  this.grid; }

    public void setTicks(long pTicks) { this.ticks = pTicks; }

    public void incrementTicks(){ this.ticks ++; }
}
