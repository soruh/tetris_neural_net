package game_logic;

import java.util.Random;

public class Game {
    GameState gameState;
    int ticksPerDrop;

    public Game(long randomSeed){
        gameState = new GameState(new Random(randomSeed));

        ticksPerDrop = 24;
    }

    public Game(){
        gameState = new GameState(new Random());

        ticksPerDrop = 24;
    }

    public boolean tick(Action pAction){
        gameState.applyAction(pAction);

        if(gameState.getTicks() % ticksPerDrop == 0) {
            gameState.applyAction(Action.DOWN);
        }

        gameState.incrementTicks();

        return !gameState.getTerminated();
    }

    public GameState getGameState(){
        return this.gameState;

    }
}
