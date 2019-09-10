public class Test {
    public static void main(String[] args) {
        NeuralNetwork network = new NeuralNetwork();

        network.setStepSize(0.5);

        double[][] h1_weights = {{.15, .20}, {.25, .30}};
        Layer h1 = new Layer(h1_weights, .35);
        h1.setActivationFunc(ActivationFunction.sigmoid);
        network.addLayer(h1);

        double[][] o1_weights = {{.40, .45}, {.50, .55}};
        Layer o1 = new Layer(o1_weights, .60);
        o1.setActivationFunc(ActivationFunction.sigmoid);
        network.addLayer(o1);

        // network.print();

        double[] input = {0.05, .10};
        double[] output = network.forwardPass(input);

        System.out.print("output: [");
        for(int i=0;i<output.length;i++){
            if(i!=0) System.out.print(", ");
            System.out.print(output[i]);
        }
        System.out.println("]");

        double[] target = {0.01, 0.99};

        network.backwardPass(ErrorFunction.halfSquare, target);


        network.print();
    }
}
