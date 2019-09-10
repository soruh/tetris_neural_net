package game_logic;

public abstract class Block {

    private int[][] cells;
    private int type;

    public abstract void turnLeft();

    public abstract void turnRight();

    private void moveLeft(){
        for(int[] cell : cells){
            cell[0] --;
        }
    }

    private void moveRight(){
        for(int[] cell : cells){
            cell[0] ++;
        }
    }

    private void moveDown(){
        for(int[] cell : cells) {
            cell[1] --;
        }
    }


    public class lPiece{

    }

    public class LPieceInverted{

    }

    public class TPiece{

    }

    public class IPiece{

    }

    public class BlockPiece{

    }

    public class StepPiece{

    }

    public class StepPieceInverted{

    }
}
