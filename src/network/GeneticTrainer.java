package network;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class GeneticTrainer {
    private FitnessFunction func;

    public GeneticTrainer(FitnessFunction func) {
        this.func = func;
    }

    public NeuralNetwork[] trainGeneration(NeuralNetwork[] pNetworks) {
        double[] fitness = new double[pNetworks.length];
        Random rng = new Random();
        long seed = rng.nextLong();
        for (int i = 0; i < pNetworks.length; i++) {
            fitness[i] = func.evaluate(pNetworks[i], seed);
        }

        parallelSort(fitness, pNetworks);

        return pNetworks;
    }



    public NeuralNetwork[] crossover(double pCrack, NeuralNetwork parentNetwork1, NeuralNetwork parentNetwork2) {

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

        double[] childNetwork1Weights = childNetwork1.getWeightsAsArray();
        double[] childNetwork2Weights = childNetwork2.getWeightsAsArray();
        double[] temp = childNetwork1Weights.clone();

        int crackIndex = (int) pCrack * childNetwork1Weights.length;

        for (int i = crackIndex; i < childNetwork1Weights.length; i++) {
            childNetwork1Weights[i] = childNetwork2Weights[i];
            childNetwork2Weights[i] = temp[i];
        }
         return new NeuralNetwork[]{childNetwork1, childNetwork2};
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
