package network;

public class NeuralNetwork {

    private Layer inputLayer, outputLayer;
    private double learningRate;


    public NeuralNetwork() {
    }


    public void addLayer(Layer pLayer) {
        if (inputLayer == null) {
            inputLayer = pLayer;
            outputLayer = inputLayer;
        } else {
            outputLayer.setNextLayer(pLayer);
            pLayer.setPreviousLayer(outputLayer);
            outputLayer = pLayer;
        }
    }

    public double[] forwardPass(double[] pInput) {
        return _forwardPass(inputLayer, pInput);
    }

    private double[] _forwardPass(Layer pLayer, double[] pInput) {
        if (pLayer == null) return pInput;
        else return _forwardPass(pLayer.getNextLayer(), pLayer.forwardPass(pInput));
    }

    public void backPropagation(double[] pTargets) {
        Layer currentLayer = outputLayer;
        currentLayer.backPropagation(pTargets);
        while (currentLayer != inputLayer) {
            currentLayer = currentLayer.getPreviousLayer();
            currentLayer.backPropagation();
        }
    }

    public void gradientDescend() {
        Layer currentLayer = inputLayer;
        currentLayer.gradientDescend();
        while (currentLayer != outputLayer) {
            currentLayer = currentLayer.getNextLayer();
            currentLayer.gradientDescend();
        }
    }

    public double[] gradientEpisode(double[] pInputs, double[] pTargets) {
        double[] outputs;
        outputs = this.forwardPass(pInputs);
        this.backPropagation(pTargets);
        this.gradientDescend();

        return outputs;
    }

    public void crossover(NeuralNetwork network, double crack) {
        int weights = 0;
        Layer currentLayer = inputLayer;
        while (currentLayer != null) {
            weights += currentLayer.getWeights().length * currentLayer.getWeights()[0].length;
            currentLayer = currentLayer.getNextLayer();
        }

        int crackWeight = (int) (weights * crack);
        weights = 0;

        currentLayer = inputLayer;
        Layer networkLayer = network.inputLayer;
        while (currentLayer != null) {
            int dif = currentLayer.getWeights().length * currentLayer.getWeights()[0].length;
            if (weights + dif > crackWeight) {
                break;
            }

            currentLayer = currentLayer.getNextLayer();
            networkLayer = networkLayer.getNextLayer();
        }

        if (crackWeight - weights < (currentLayer.getWeights()[0].length * currentLayer.getWeights().length / 2)) {
            double currentLayerBias = currentLayer.getBias();
            currentLayer.setBias(networkLayer.getBias());
            networkLayer.setBias(currentLayerBias);
        }

        int j = weights % currentLayer.getWeights().length;
        for (int i = weights / currentLayer.getWeights().length; i < currentLayer.getWeights().length; i++) {
            for (; j < currentLayer.getWeights()[0].length; j++) {
                double currentLayerWeight = currentLayer.getWeights()[i][j];
                currentLayer.getWeights()[i][j] = networkLayer.getWeights()[i][j];
                networkLayer.getWeights()[i][j] = currentLayerWeight;
            }
            j = 0;
        }

        while (currentLayer != null) {
            double[][] temp = currentLayer.getWeights();
            currentLayer.setWeights(networkLayer.getWeights());
            networkLayer.setWeights(temp);

            currentLayer = currentLayer.getNextLayer();
            networkLayer = networkLayer.getNextLayer();

            double currentLayerBias = currentLayer.getBias();
            currentLayer.setBias(networkLayer.getBias());
            networkLayer.setBias(currentLayerBias);
        }
    }

    public void print() {
        Layer currentLayer = inputLayer;
        currentLayer.print();
        while (currentLayer != outputLayer) {
            currentLayer = currentLayer.getNextLayer();
            currentLayer.print();
        }
    }
}
