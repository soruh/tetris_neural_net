package network;

import game_logic.Action;
import game_logic.Game;

import java.util.Random;

public abstract class FitnessFunction {
    public static final FitnessFunction tetris = new _tetris();

    private static class _tetris extends FitnessFunction{
        public double evaluate(NeuralNetwork pNetwork, long pRandomSeed) {

            Game game = new Game(pRandomSeed);
            boolean terminated = false;
            double[] rawOutput;

            while((!terminated) &&(game.getGameState().getScore() <= 999999)){

                int biggestOutputIndex = 0;
                rawOutput = pNetwork.forwardPass(game.getGameState().flattenedState());
                for (int i = 0; i < rawOutput.length; i++) {
                    if (rawOutput[i] >= rawOutput[biggestOutputIndex]) biggestOutputIndex = i;
                }

                Action nextAction;
                switch (biggestOutputIndex){
                    case 0: nextAction = Action.NOTHING; break;
                    case 1: nextAction = Action.LEFT; break;
                    case 2: nextAction = Action.RIGHT; break;
                    case 3: nextAction = Action.DOWN; break;
                    case 4: nextAction = Action.TURN_LEFT; break;
                    case 5: nextAction = Action.TURN_RIGHT; break;
                    case 6: nextAction = Action.HOLD_PIECE; break;
                    default: nextAction = Action.NOTHING; break;
                }

                terminated = game.tick(nextAction);
            }
            return game.getGameState().getScore();
        }
    }

    public abstract double evaluate(NeuralNetwork network);
}
