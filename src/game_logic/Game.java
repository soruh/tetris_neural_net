package game_logic;

import java.util.Random;

public class Game {
    GameState state;

    public Game(long randomSeed){
        state = new GameState(randomSeed);
    }

    public void tick(Action pAction){


        state.incrementTicks();
    }
}
