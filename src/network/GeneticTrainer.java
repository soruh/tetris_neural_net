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
