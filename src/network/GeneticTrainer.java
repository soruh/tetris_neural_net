package network;

import java.util.Random;

public class GeneticTrainer {
    private NeuralNetwork[] networks;
    private FitnessFunction func;
    private Random r;

    public GeneticTrainer(int networks, FitnessFunction func) {
        this.networks = new NeuralNetwork[networks];
    }

    public void addLayer(int inputs, int outputs) {
        for (NeuralNetwork net : networks) {
            net.addLayer(new Layer(inputs, outputs));
        }
    }

    public double[] trainGeneration() {

    }
}
