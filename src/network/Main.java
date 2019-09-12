package network;

import gui.Gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.util.Arrays;

public class Main {

    private GeneticTrainer trainer;
    private NeuralNetwork[] networks;

    private String sessionDir = "./weights/"+System.currentTimeMillis()+"/";


    public static void main(String[] args) {
        int trainingEpisodes = Integer.MAX_VALUE;
        int generationSize = 200;
        Main main = new Main(generationSize);
        main.train(trainingEpisodes);
    }

    public Main(int pGenerationSize){
        new File(sessionDir).mkdirs();

        networks = new NeuralNetwork[pGenerationSize];
        for (int i = 0; i < networks.length; i++) {
            networks[i] = new NeuralNetwork();
            networks[i].addLayer(new Layer(208, 100));
            networks[i].addLayer(new Layer(100, 40));
            networks[i].addLayer(new Layer(40, 40));
            networks[i].addLayer(new Layer(40, 6));
        }

        double mutationRate = 0.9;

        trainer = new GeneticTrainer(FitnessFunction.tetris, mutationRate);

        System.out.println("constructed Trainer with mutationRate: "+mutationRate+" pGenerationSize: "+pGenerationSize);
    }

    public void train(int pTrainingEpisodes) {
        NeuralNetwork[] trainedGeneration = networks.clone();

        for (int i = 0; i < pTrainingEpisodes; i++) {
            System.out.print("Generation: " + i + "; ");
            System.out.print("Seed: " + getCurrentSeed() + "; ");
            NeuralNetwork[] newTrainedGeneration = trainer.trainGeneration(trainedGeneration);


            if(trainer.getCurrentGeneration() % 10 == 0){
                String generationDir = sessionDir+trainer.getCurrentGeneration();
                System.out.println("out path: "+generationDir);
                System.out.print("saving generation... ");


                new File(generationDir).mkdirs();

                for(int n=0;n<trainedGeneration.length;n++){
                    this.saveWeights(generationDir+"/"+n, trainedGeneration[n]);
                }

                System.out.println("done");
            }

            // System.out.println("fitness of saved network: "+FitnessFunction.tetris.evaluate(trainedGeneration[0], -420691337));

            trainedGeneration = newTrainedGeneration;
        }
        // this.saveWeights(trainedGeneration[0]);
    }

    public NeuralNetwork getBestNetwork() {
        return networks[0];
    }

    public long getCurrentSeed() {
        return trainer.getSeed();
    }

    public void saveWeights(String path, NeuralNetwork pNetwork) {

        String toWrite = "";
        double[] weights = pNetwork.getWeightsAsArray();
        toWrite += Arrays.toString(weights);
        toWrite = toWrite.replace('[',' ');
        toWrite = toWrite.replace(']',' ');
        toWrite = toWrite.trim();

        try {

            FileWriter is = new FileWriter(path);
            BufferedWriter buffer = new BufferedWriter(is);

            buffer.write(toWrite);
            buffer.close();
            is.close();

            // System.out.println("saved as: "+path);
        } catch (Exception e) {
            System.err.println("failed to save file: "+e);
        }
    }

    public static void loadWeights(String path, NeuralNetwork pNetwork)
    {
        System.out.println("loading net at: "+path);
        String temp = "";
        try {
            FileReader is = new FileReader(path);
            BufferedReader buffer = new BufferedReader(is);
            temp = buffer.readLine();
            buffer.close();
            is.close();
        } catch (Exception e) {
            System.out.println("failed to load net: "+e);
        }


        String[] strValues = temp.split(",");
        double[] values = new double[strValues.length];
        for (int i = 0; i < strValues.length; i++) {
            values[i] = Double.valueOf(strValues[i]);
        }

        pNetwork.setWeightsFromArray(values);

        System.out.println("Laden abgeschlossen.");
    }
}
