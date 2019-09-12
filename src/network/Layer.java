package network;

import java.util.Random;

public class Layer {

    private int cInputs = 0, cOutputs = 0;
    private Layer next = null, previous = null;
    private double[][] weights;
    private ActivationFunction func;
    private double[] bias;
    private double[] lastOutputs;
    private double[] lastInputs;
    private double[] deltas;
    private double[][] gradient;
    private double learningRate;

    Random rng = new Random();


    Layer(int pInputs, int pOutputs) {
        this.func = ActivationFunction.sigmoid;

        this.cInputs = pInputs;
        this.cOutputs = pOutputs;
        this.weights = new double[cOutputs][cInputs];
        this.gradient = new double[cOutputs][cInputs];
        this.deltas = new double[cOutputs];
        this.lastInputs = new double[cInputs];
        this.lastOutputs = new double[cOutputs];
        this.bias = new double[cOutputs];

        this.randomize();
    }

    Layer(double[][] pWeights, double[] pBias) {
        this.func = ActivationFunction.sigmoid;

        this.weights = pWeights;
        this.bias = pBias;

        this.cInputs = weights[0].length;
        this.cOutputs = weights.length;
        this.gradient = new double[cOutputs][cInputs];
        this.deltas = new double[cOutputs];
        this.lastInputs = new double[cInputs];
        this.lastOutputs = new double[cOutputs];
    }


    void randomize() {
        for (int i = 0; i < bias.length; i++) {
            bias[i] = rng.nextDouble();
        }
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = (rng.nextDouble() * 2) - 1;
            }
        }
    }

    public int getcOutputs() {
        return cOutputs;
    }

    public int getcInputs() {
        return cInputs;
    }

    public int getNWeights() {
        return cInputs * cOutputs;
    }

    public double[] getBias() {
        return this.bias;
    }

    public double[][] getWeights() {
        return weights;
    }

    Layer getNextLayer() {
        return next;
    }

    Layer getPreviousLayer() {
        return previous;
    }

    ActivationFunction getFunc() {
        return func;
    }

    private double[] getLastOutputs() {
        return lastOutputs;
    }

    private double[] getDeltas() {
        return deltas;
    }

    double getLearningRate() {
        return learningRate;
    }

    void setBias(double[] pBias) {
        this.bias = pBias;
    }

    void setWeights(double[][] pWeights) {
        this.weights = pWeights;
    }

    void setNextLayer(Layer pNext) {
        this.next = pNext;
    }

    void setPreviousLayer(Layer pPrevious) {
        this.previous = pPrevious;
    }

    void setFunc(ActivationFunction pFunc) {
        this.func = pFunc;
    }

    void setLearningRate(double pLearningRate) {
        this.learningRate = pLearningRate;
    }

    double[] forwardPass(double[] pInput) {
        lastInputs = pInput;

        if (pInput.length == cInputs) {
            double[] result = new double[this.cOutputs];
            for (int i = 0; i < this.cOutputs; i++) {
                for (int j = 0; j < this.cInputs; j++) {
                    result[i] += weights[i][j] * pInput[j];
                }
                result[i] += bias[i];
                result[i] = func.compute(result[i]);
            }
            lastOutputs = result;
            return result;

        } else {
            System.out.println("Improper number of input values!");
            return null;
        }
    }

    void backPropagation(double[] pTargets) {
        for (int i = 0; i < cOutputs; i++) {
            deltas[i] = (lastOutputs[i] - pTargets[i]) * lastOutputs[i] * (1 - lastOutputs[i]);
        }
        for (int i = 0; i < cOutputs; i++) {
            for (int j = 0; j < cInputs; j++) {
                gradient[i][j] = deltas[i] * this.previous.getLastOutputs()[j];
            }
        }
    }

    void backPropagation() {
        for (int i = 0; i < cOutputs; i++) {
            double nextDeltaSum = 0;
            for (int j = 0; j < next.getDeltas().length; j++) {
                nextDeltaSum += next.getDeltas()[j] * next.getWeights()[j][i];
            }
            deltas[i] = nextDeltaSum * lastOutputs[i] * (1 - lastOutputs[i]);
        }
        for (int i = 0; i < cOutputs; i++) {
            for (int j = 0; j < cInputs; j++) {
                double partialDerivative = 0;
                for (int k = 0; k < next.getWeights().length; k++) {
                    partialDerivative += next.getDeltas()[k] * next.getWeights()[k][0];
                }
                gradient[i][j] = lastOutputs[i] * (1 - lastOutputs[i]) * this.lastInputs[j] * partialDerivative;
            }
        }
    }

    void gradientDescend() {
        for (int i = 0; i < cOutputs; i++) {
            for (int j = 0; j < cInputs; j++) {
                weights[i][j] -= gradient[i][j] * learningRate;
            }
        }
    }

    void print() {
        System.out.println("Inputs: " + cInputs);
        System.out.println("Outputs: " + cOutputs);
        System.out.print("Weights: ");
        for (int i = 0; i < cOutputs; i++) {
            for (int j = 0; j < cInputs; j++) {
                System.out.print(weights[i][j] + ", ");
            }
            System.out.print("; ");
        }
        System.out.print("\n");
    }
}
