package network;

import java.util.Random;

public class GeneticTrainer {
    private NeuralNetwork[] networks;
    private double[] fitness;
    private FitnessFunction func;

    public GeneticTrainer(int networks, FitnessFunction func) {
        this.func = func;
        this.networks = new NeuralNetwork[networks];
        this.fitness = new double[networks];
    }

    public void addLayer(int inputs, int outputs) {
        for (NeuralNetwork net : networks) {
            net.addLayer(new Layer(inputs, outputs));
        }
    }

    public double[] trainGeneration() {
        Random rng = new Random();
        long seed = rng.nextLong();
        for (int i = 0; i < networks.length; i++) {
            fitness[i] = func.evaluate(networks[i], seed);
        }
        parrallelSort(fitness, networks);
    }


    private static void parrallelSort(double[] sortBy, Object[] sortAlong) {
        parrallelSort(sortBy, sortAlong, 0, sortBy.length);
    }
    private static void parrallelSort(double[] sortBy, Object[] sortAlong, int from, int length) {

    }
}
