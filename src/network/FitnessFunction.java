package network;

import java.util.Random;

public abstract class FitnessFunction {
    public static final FitnessFunction tetris = new _tetris();

    private static class _tetris extends FitnessFunction{
        public double evaluate(NeuralNetwork network) {
            Random r = new Random();
            return r.nextDouble();
        }
    }

    public abstract double evaluate(NeuralNetwork network);
}
