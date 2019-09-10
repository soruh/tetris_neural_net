package network;

import java.util.Random;

public class Layer {

    private int cInputs = 0, cOutputs = 0;
    private Layer next = null, previous = null;
    private double[][] weights;
    private ActivationFunction func;
    private double bias = 0;
    private double[] lastOutputs;
    private double[] lastInputs;
    private double[] deltas;
    private double[][] gradient;
    private double learningRate;

    Random r = new Random();


    public Layer(int pInputs, int pOutputs){
        this.func = ActivationFunction.sigmoid;

        this.cInputs = pInputs;
        this.cOutputs = pOutputs;
        this.weights = new double[cOutputs][cInputs];
        this.gradient = new double[cOutputs][cInputs];
        this.deltas = new double[cOutputs];
        this.lastInputs = new double[cInputs];
        this.lastOutputs = new double[cOutputs];

        this.randomize();
    }

    public Layer(double[][] pWeights, double pBias){
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


    public void randomize(){
        this.bias = (r.nextDouble() * 2) - 1;
        for(double[] values: weights){
            for(double value: values){
                value = (r.nextDouble() * 2) - 1;
            }
        }
    }

    public double getBias() {
        return this.bias;
    }

    public double[][] getWeights() {
        return weights;
    }

    public Layer getNextLayer() {
        return next;
    }

    public Layer getPreviousLayer() {
        return previous;
    }

    public ActivationFunction getFunc() {
        return func;
    }

    public double[] getLastOutputs() {
        return lastOutputs;
    }

    public double[] getDeltas() {
        return deltas;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setBias(double pBias) {
        this.bias = pBias;
    }

    public void setWeights(double[][] pWeights) {
        this.weights = pWeights;
    }

    public void setNextLayer(Layer pNext) {
        this.next = pNext;
    }

    public void setPreviousLayer(Layer pPrevious){
        this.previous = pPrevious;
    }

    public void setFunc(ActivationFunction pFunc) {
        this.func = pFunc;
    }

    public void setLearningRate(double pLearningRate) {
        this.learningRate = pLearningRate;
    }

    public double[] forwardPass(double[] pInput){
        lastInputs = pInput;

        if(pInput.length == cInputs){
            double[] result = new double[this.cOutputs];
            for(int i = 0; i < this.cOutputs; i++){
                for(int j = 0; j < this.cInputs; j++){
                    result[i] += weights[i][j] * pInput[j];
                }
                result[i] += bias;
                result[i] = func.compute(result[i]);
            }
            lastOutputs = result;
            return result;

        } else {
            System.out.println("Improper number of input values!");
            return null;
        }
    }

    public void backPropagation(double[] pTargets){
        for (int i = 0; i < cOutputs; i++) {
            deltas[i] = (lastOutputs[i] - pTargets[i]) * lastOutputs[i] * (1 - lastOutputs[i]);
        }
        for (int i = 0; i < cOutputs; i++) {
            for (int j = 0; j < cInputs; j++) {
                gradient[i][j] = deltas[i] * this.previous.getLastOutputs()[j];
            }
        }
    }

    public void backPropagation(){
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

    public void gradientDescend() {
        for (int i = 0; i < cOutputs; i++) {
            for (int j = 0; j < cInputs; j++) {
                weights[i][j] -= gradient[i][j] * learningRate;
            }
        }
    }

    public void print(){
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
