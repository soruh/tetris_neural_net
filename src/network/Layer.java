import java.util.Random;

public class Layer {
    private int cInputs, cOutputs;

    private Layer next;
    private Layer previous;

    private ActivationFunction activationFunc;
    private double bias;

    private double[] outputs;
    private double[] inputs;
    private double[] deltas;

    private double[][] weights;
    private double[][] adjustments;



    public Layer (int cInputs, int cNeurons) {
            this.cInputs = cInputs;
            this.cOutputs = cNeurons;

            this.weights = new double[cOutputs][cInputs];
            this.adjustments = new double[cOutputs][cInputs];
            this.deltas = new double[cOutputs];
            this.outputs = new double[cOutputs];

            this.randomize();
    }

    public Layer(double[][] weights, double bias) {
        this.weights = weights;
        this.bias = bias;

        this.cInputs = this.weights[0].length;
        this.cOutputs = this.weights.length;

        this.adjustments = new double[cOutputs][cInputs];
        this.deltas = new double[cOutputs];
        this.outputs = new double[cOutputs];
    }

    public void randomize(){
        Random r = new Random();

        for(int i=0;i<cOutputs;i++) {
            for(int j=0;j<cInputs;j++) {
                this.weights[i][j] = r.nextDouble() * 2 -1;
            }
        }

        this.bias = r.nextDouble() * 2 -1;
    }

    public double[] forwardPass(double[] inputs) {
        this.inputs = inputs;

        for(int outputIndex=0;outputIndex<this.cOutputs;outputIndex++) {
            outputs[outputIndex] = this.bias;

            for(int inputIndex=0;inputIndex<this.cInputs;inputIndex++) {
                outputs[outputIndex] += inputs[inputIndex] * this.weights[outputIndex][inputIndex];
            }
        }

        for(int outputIndex=0;outputIndex<this.cOutputs;outputIndex++) {
            outputs[outputIndex] = this.activationFunc.compute(outputs[outputIndex]);
        }

        return outputs;
    }

    public void backwardPassInitial(ErrorFunction errorFunc, double[] target) {
        for (int outputIndex=0;outputIndex<cOutputs;outputIndex++) {
            double output = outputs[outputIndex];
            double delta = errorFunc.derivative(output, target[outputIndex]) * activationFunc.derivative(output);


            deltas[outputIndex] = delta;

            for (int inputIndex = 0; inputIndex < cInputs; inputIndex++) {
                double previous_output = previous.outputs[inputIndex];

                adjustments[outputIndex][inputIndex] = delta * previous_output;
            }
        }
    }

    public void backwardPass(ErrorFunction errorFunc){
        for (int outputIndex=0;outputIndex<cOutputs;outputIndex++) {
            double output = outputs[outputIndex];

            double delta = 0;

            for (int nextNodeIndex = 0; nextNodeIndex < next.cOutputs; nextNodeIndex++) {
                double nextWeight = next.weights[nextNodeIndex][outputIndex];
                double nextDelta = next.deltas[nextNodeIndex];

                delta += nextWeight * nextDelta;
            }

            delta *= activationFunc.derivative(output);

            deltas[outputIndex] = delta;

            for (int inputIndex = 0; inputIndex < cInputs; inputIndex++) {
                double previous_output = inputs[inputIndex];

                adjustments[outputIndex][inputIndex] = delta * previous_output;
            }
        }
    }

    public void applyAdjustments(double stepSize) {
        for (int outputIndex=0;outputIndex<cOutputs;outputIndex++) {
            for (int inputIndex=0;inputIndex<cInputs;inputIndex++) {
                weights[inputIndex][outputIndex] -= adjustments[inputIndex][outputIndex] * stepSize;
            }
        }
    }


    public double getBias() {
        return bias;
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

    public ActivationFunction getActivationFunc() {
        return activationFunc;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public void setWeights(double[][] weights) {
        this.weights = weights;
    }

    public void setNextLayer(Layer next) {
        this.next = next;
    }

    public void setPreviousLayer(Layer previous) {
        this.previous = previous;
    }

    public void setActivationFunc(ActivationFunction activationFunc) {
        this.activationFunc = activationFunc;
    }

    public void print() {
        System.out.println("[ Layer ]");
        // System.out.println("previous: "+ previous);
        // System.out.println("next: "+ next);
        // System.out.println("bias: "+ bias);
        // System.out.println("cInputs: "+ cInputs);
        // System.out.println("cOutputs: "+ cOutputs);

        System.out.print("weights: [\n");
        for(int i=0;i<weights.length;i++){
            for(int j=0;j<weights[i].length;j++) {
                System.out.print(weights[i][j]+", ");
            }
            System.out.print("\n");
        }
        System.out.println("]");


        System.out.print("adjustments: [\n");
        for(int i=0;i<adjustments.length;i++){
            for(int j=0;j<adjustments[i].length;j++) {
                System.out.print(adjustments[i][j]+", ");
            }
            System.out.print("\n");
        }
        System.out.println("]");


        System.out.print("deltas: [");
        for(int i=0;i<deltas.length;i++){
            if(i!=0) System.out.print(", ");
            System.out.print(deltas[i]);
        }
        System.out.println("]");

    }
}
