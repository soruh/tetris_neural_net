package network;

import java.util.Arrays;
import java.util.Comparator;
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

        parallelSort(fitness, networks);
    }

    public NeuralNetwork[] crossover(double crack, NeuralNetwork parentNetwork1, NeuralNetwork parentNetwork2) {

        NeuralNetwork childNetwork1 = new NeuralNetwork();
        NeuralNetwork childNetwork2 = new NeuralNetwork();

        Layer network1Layer = parentNetwork1.getInputLayer();
        Layer network2Layer = parentNetwork2.getInputLayer();

        while (network1Layer != null) {

            childNetwork1.addLayer(new Layer(network1Layer.getWeights().clone(), network1Layer.getBias()));
            childNetwork2.addLayer(new Layer(network2Layer.getWeights().clone(), network2Layer.getBias()));

            network1Layer = network1Layer.getNextLayer();
            network2Layer = network2Layer.getNextLayer();
        }



        int totalNWeights = 0;

        network1Layer = childNetwork1.getInputLayer();
        while (network1Layer != null) {
            totalNWeights += network1Layer.getNWeights();

            network1Layer = network1Layer.getNextLayer();
        }

        int crackWeight = (int) (totalNWeights * crack);

        int weightCounter = 0;

        Layer childNetwork1Layer = childNetwork1.getInputLayer();
        Layer childNetwork2Layer = childNetwork2.getInputLayer();

        while (childNetwork1Layer != null) { // this should always be true

            int nLayerWeights = childNetwork1Layer.getNWeights();

            if (weightCounter + nLayerWeights > crackWeight) break;

            weightCounter += nLayerWeights;

            childNetwork1Layer = childNetwork1Layer.getNextLayer();
            childNetwork2Layer = childNetwork2Layer.getNextLayer();
        }

        if (crackWeight - weightCounter < (childNetwork1Layer.getNWeights() / 2)) {
            double tmpBias = childNetwork1Layer.getBias();

            childNetwork1Layer.setBias(childNetwork2Layer.getBias());
            childNetwork2Layer.setBias(tmpBias);
        }

        int j = weightCounter % childNetwork1Layer.getcOutputs();

        for (int i = weightCounter / childNetwork1Layer.getcOutputs(); i < childNetwork1Layer.getWeights().length; i++) {
            for (; j < childNetwork1Layer.getWeights()[0].length; j++) {
                double tmpWeights = childNetwork1Layer.getWeights()[i][j];

                childNetwork1Layer.getWeights()[i][j] = childNetwork2Layer.getWeights()[i][j];
                childNetwork2Layer.getWeights()[i][j] = tmpWeights;
            }
            j = 0;
        }

        while (childNetwork1Layer != null) {
            double[][] tmpWeights = childNetwork1Layer.getWeights();
            childNetwork1Layer.setWeights(childNetwork2Layer.getWeights());
            childNetwork2Layer.setWeights(tmpWeights);

            childNetwork1Layer = childNetwork1Layer.getNextLayer();
            childNetwork2Layer = childNetwork2Layer.getNextLayer();

            double tmpBias = childNetwork1Layer.getBias();
            childNetwork1Layer.setBias(childNetwork2Layer.getBias());
            childNetwork2Layer.setBias(tmpBias);
        }

        NeuralNetwork[] newNetworks = new NeuralNetwork[]{childNetwork1, childNetwork1};

        return newNetworks;
    }

    private static void parallelSort(double[] sortBy, Object[] sortAlong) {
        class Item {
            double sortBy;
            Object sortAlong;

            Item (double sortBy, Object sortedValue) {
                this.sortAlong = sortedValue;
                this.sortBy = sortBy;
            }

            public double getSortBy() { return sortBy; }

            public Object getSortAlong() { return sortAlong; }

            public int compareTo(Item i2) {
                if(this.sortBy == i2.sortBy) return 0;
                if(this.sortBy > i2.sortBy) return 1;
                return -1;
            }
        }

        class ItemComparator implements Comparator {
            public int compare( Object o1, Object o2 ) {
                Item i1 = (Item)o1;
                Item i2 = (Item)o2;

                return i1.compareTo(i2);
            }
        }


        Item[] items = new Item[sortBy.length];
        for (int i = 0; i < sortBy.length; i++) items[i] = new Item(sortBy[i], sortAlong[i]);

        Arrays.sort( items, new ItemComparator() );

        for (int i = 0; i < sortBy.length; i++) {
            sortBy[i] = items[i].getSortBy();
            sortAlong[i] = items[i].getSortAlong();
        }
    }
}
