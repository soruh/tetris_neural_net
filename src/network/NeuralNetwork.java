package Beispiel;

public class NeuralNetwork {

    private Layer inputLayer, outputLayer;
    private double learningRate;


    public NeuralNetwork(){}


    public void addLayer(Layer pLayer){
        if(inputLayer == null){
            inputLayer = pLayer;
            outputLayer = inputLayer;
        } else {
            outputLayer.setNextLayer(pLayer);
            pLayer.setPreviousLayer(outputLayer);
            outputLayer = pLayer;
        }
    }

    public double[] forwardPass(double[] pInput){
        return _forwardPass(inputLayer, pInput);
    }

    private double[] _forwardPass(Layer pLayer, double[] pInput){
        if(pLayer ==  null) return pInput;
        else return _forwardPass(pLayer.getNextLayer(), pLayer.forwardPass(pInput));
    }

    public void backPropagation(double[] pTargets){
        Layer currentLayer = outputLayer;
        currentLayer.backPropagation(pTargets);
        while(currentLayer != inputLayer) {
            currentLayer = currentLayer.getPreviousLayer();
            currentLayer.backPropagation();
        }
    }

    public void gradientDescend(){
        Layer currentLayer = inputLayer;
        currentLayer.gradientDescend();
        while(currentLayer != outputLayer) {
            currentLayer = currentLayer.getNextLayer();
            currentLayer.gradientDescend();
        }
    }

    public double[] gradientEpisode(double[] pInputs, double[] pTargets){
        double[] outputs;
        outputs = this.forwardPass(pInputs);
        this.backPropagation(pTargets);
        this.gradientDescend();

        return outputs;
    }

    public void print(){
        Layer currentLayer = inputLayer;
        currentLayer.print();
        while(currentLayer != outputLayer){
            currentLayer = currentLayer.getNextLayer();
            currentLayer.print();
        }
    }
}
