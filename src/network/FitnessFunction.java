package network;

import game_logic.Action;
import game_logic.Game;

import java.util.Random;

public abstract class FitnessFunction {
    public static final FitnessFunction tetris = new _tetris();

    private static class _tetris extends FitnessFunction {
        public double evaluate(NeuralNetwork pNetwork, long pRandomSeed) {

            Game game = new Game(pRandomSeed);
            double[] rawOutput;

            Action nextAction;

            do {
                rawOutput = pNetwork.forwardPass(game.getGameState().flattenedState());

                int biggestOutputIndex = 0;
                for (int i = 0; i < rawOutput.length; i++) {
                if (rawOutput[i] >= rawOutput[biggestOutputIndex]) biggestOutputIndex = i;
                }

                switch (biggestOutputIndex){
                    case 1: nextAction = Action.LEFT; break;
                    case 2: nextAction = Action.RIGHT; break;
                    case 3: nextAction = Action.DOWN; break;
                    case 4: nextAction = Action.TURN_LEFT; break;
                    case 5: nextAction = Action.TURN_RIGHT; break;
                    // case 6: nextAction = Action.HOLD_PIECE; break;
                    // we don't allow the net to use this feature, since it would be too
                    // difficult for it to learn it.
                    default:
                    case 0: nextAction = Action.NOTHING; break;
                }

            } while(game.tick(nextAction) && (game.getGameState().getScore() <= 999999));

            return game.getGameState().getBlocksPlaced() + game.getGameState().getScore();
        }
    }

    public abstract double evaluate(NeuralNetwork pNetwork, long pRandomSeed);
}
