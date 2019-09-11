package network;

import java.util.ArrayList;

public class NeuralNetwork {

    private Layer inputLayer;
    private Layer outputLayer;
    private double learningRate;


    public Layer getInputLayer() {
        return inputLayer;
    }


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

    public void print() {
        Layer currentLayer = inputLayer;
        currentLayer.print();
        while (currentLayer != outputLayer) {
            currentLayer = currentLayer.getNextLayer();
            currentLayer.print();
        }
    }

    public double[] getWeightsAsArray()
    {
        ArrayList<Double> w = new ArrayList<>();
        Layer currentLayer = inputLayer;

        while( currentLayer != null )
        {
            double[][] temp = currentLayer.getWeights();
            for( int i = 0; i < temp.length; i++ )
            {
                for( int j = 0; j < temp[i].length; j++)
                {
                    w.add(temp[i][j]);
                }
            }
            currentLayer = currentLayer.getNextLayer();
        }

        double[] arr =  new double[w.size()];
        for( int i = 0; i < arr.length; i++)
            arr[i] = w.get(i);

        return arr;
    }

    public void setWeightsFromArray(double[] w)
    {
        Layer currentLayer = inputLayer;
        int index = 0;

        while( currentLayer != null )
        {
            for( int i = 0; i < currentLayer.getWeights().length; i++ )
            {
                for( int j = 0; j < currentLayer.getWeights()[i].length; j++)
                {
                    currentLayer.getWeights()[i][j] = w[index];
                    index++;
                }
            }
            currentLayer = currentLayer.getNextLayer();
        }
    }
}
