package network;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class GeneticTrainer {
    private FitnessFunction func;
    private Random rng;
    private double mutationRate;
    private long seed = -420691337;
    private int currentGeneration = 0;

    public GeneticTrainer(FitnessFunction func, double mutationRate) {
        this.func = func;
        this.rng = new Random();

        this.mutationRate = mutationRate;
    }

    public NeuralNetwork[] trainGeneration(NeuralNetwork[] pGeneration) {
        double[] fitness = new double[pGeneration.length];
        // seed = rng.nextLong();

        Thread[] threads = new Thread[pGeneration.length];
        for (int i = 0; i < pGeneration.length; i++) {
            final int generation = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    fitness[generation] = func.evaluate(pGeneration[generation], seed);
                }
            });

            threads[i].start();
        }

        for (int i = 0; i < pGeneration.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        parallelSort(fitness, pGeneration);


        /*
        for(int i=0;i<fitness.length;i++){
            System.out.println("fitness["+i+"]: "+fitness[i]);
        }
        */

        System.out.println("Fitness: " + fitness[0]);
        NeuralNetwork[] newGeneration = this.createNewGeneration(pGeneration);

        currentGeneration++;

        return newGeneration;
    }

    public NeuralNetwork[] createNewGeneration(NeuralNetwork[] pOldGeneration){
        NeuralNetwork[] newGeneration = new NeuralNetwork[pOldGeneration.length];
        int alphaCutoff = (int)(pOldGeneration.length * ((double)1 / 10));
        int i = 0;
        for (; i < alphaCutoff; i++) {
            // System.out.println("alpha: i="+i);
            newGeneration[i] = pOldGeneration[i];
        }

        int betaCutoff = (int)(pOldGeneration.length * ((double)8 / 10));
        {
            int j = 0;
            int k = 0;
            int childrenPerAlpha = (betaCutoff - alphaCutoff) / alphaCutoff;
            // System.out.println("childrenPerAlpha: "+childrenPerAlpha);
            for (; i < betaCutoff && j < betaCutoff; i += 2) {
                if (k >= j + childrenPerAlpha || k >= betaCutoff) {
                    j++;
                    k = j + 1;
                } else {
                    k++;
                }

                // System.out.println("beta: i="+i+" j="+j+" k="+k);

                NeuralNetwork[] children = crossover(rng.nextDouble(), pOldGeneration[j], pOldGeneration[k]);


                if(rng.nextDouble() < mutationRate) mutate(children[0]);
                if(rng.nextDouble() < mutationRate) mutate(children[1]);

                newGeneration[i] = children[0];
                newGeneration[i + 1] = children[1];
            }
        }

        int gammaCutoff = pOldGeneration.length;
        for (int j=0; i + j < gammaCutoff; j++) {
            // System.out.println("gamma: i="+i+" j="+j+" i+j="+(i+j));

            newGeneration[i + j] = pOldGeneration[j].clone();
            mutate(newGeneration[i + j]);
        }


        return newGeneration;
    }

    public NeuralNetwork[] crossover(double pCrack, NeuralNetwork parentNetwork1, NeuralNetwork parentNetwork2) {
        NeuralNetwork childNetwork1 = new NeuralNetwork();
        NeuralNetwork childNetwork2 = new NeuralNetwork();

        Layer network1Layer = parentNetwork1.getInputLayer();
        Layer network2Layer = parentNetwork2.getInputLayer();

        while (network1Layer != null) {

            childNetwork1.addLayer(new Layer(network1Layer.getcInputs(), network1Layer.getcOutputs()));
            childNetwork2.addLayer(new Layer(network2Layer.getcInputs(), network2Layer.getcOutputs()));

            network1Layer = network1Layer.getNextLayer();
            network2Layer = network2Layer.getNextLayer();
        }

        double[] childNetwork1Weights = parentNetwork1.getWeightsAsArray().clone();
        double[] childNetwork2Weights = parentNetwork2.getWeightsAsArray().clone();

        int crackIndex = (int) pCrack * childNetwork1Weights.length;

        for (int i = crackIndex; i < childNetwork1Weights.length; i++) {
            double temp = childNetwork1Weights[i];
            childNetwork1Weights[i] = childNetwork2Weights[i];
            childNetwork2Weights[i] = temp;
        }

        childNetwork1.setWeightsFromArray(childNetwork1Weights);
        childNetwork2.setWeightsFromArray(childNetwork2Weights);

         return new NeuralNetwork[]{childNetwork1, childNetwork2};
    }


    public void mutate(NeuralNetwork pNetwork){
        double[] networkWeights;

        networkWeights = pNetwork.getWeightsAsArray();

        for(int i=0;i<5;i++){
            int mutatedWeight = rng.nextInt(networkWeights.length);
            networkWeights[mutatedWeight] = (rng.nextDouble() * 20) - 10;
        }

        pNetwork.setWeightsFromArray(networkWeights);
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
                if(this.sortBy < i2.sortBy) return 1;
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

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    public long getSeed() {
        return seed;
    }
}
