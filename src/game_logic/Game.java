package game_logic;

import java.util.Random;

public class Game {
    GameState state;
    int ticksPerDrop;

    public Game(long randomSeed){
        state = new GameState(randomSeed);
        ticksPerDrop = 24;
    }

    public boolean tick(Action pAction){
        if(state.getTerminated()) return false;

        state.applyAction(pAction);

        if(state.getTicks() % ticksPerDrop == 0) {
            state.applyAction(Action.DOWN);
        }

        state.incrementTicks();

        return true;
    }
}
