public class NeuralNetwork {
    Layer inputLayer;
    Layer outputLayer;

    private double stepSize;


    public NeuralNetwork() {}

    public void addLayer(Layer layer) {
        if (inputLayer == null) {
            inputLayer = outputLayer = layer;
        } else {
            // outputLayer is now the second to last layer
            outputLayer.setNextLayer(layer);
            layer.setPreviousLayer(outputLayer);

            outputLayer = layer;
        }
    }

    private double[] _forwardPass(Layer layer, double[] input) {
        if(layer == null) return input;
        return _forwardPass(layer.getNextLayer(), layer.forwardPass(input));
    }

    public double[] forwardPass(double[] input) {
        return _forwardPass(inputLayer, input);
    }

    public void backwardPass(ErrorFunction errorFunc, double[] target) {
        // if(lastInput == null) throw new Exception("");

        outputLayer.backwardPassInitial(errorFunc, target);

        Layer layer = outputLayer.getPreviousLayer();

        while (layer != null) {
            layer.backwardPass(errorFunc);

            layer = layer.getPreviousLayer();
        }

        layer = outputLayer;

        while (layer != null) {
            layer.applyAdjustments(stepSize);

            layer = layer.getPreviousLayer();
        }
    }

    public void print(){
        System.out.println("[ NeuralNetwork ]");
        Layer layer = inputLayer;

        while (layer != null) {
            layer.print();

            layer = layer.getNextLayer();
        }
    }

    public void setStepSize(double stepSize) {
        this.stepSize = stepSize;
    }
}
