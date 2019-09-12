package network;

import gui.Gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

public class Main {


    private GeneticTrainer trainer;
    private NeuralNetwork[] networks;
    private NeuralNetwork[] trainedGeneration;
    private long startTime;


    public static void main(String[] args) {
        Main main = new Main();
        main.train(5000);
    }

    public Main() {
        networks = new NeuralNetwork[80];
    public Main(){
        startTime = System.currentTimeMillis();
        networks = new NeuralNetwork[100];
        for (int i = 0; i < networks.length; i++) {
            networks[i] = new NeuralNetwork();
            networks[i].addLayer(new Layer(200, 100));
            networks[i].addLayer(new Layer(100, 40));
            networks[i].addLayer(new Layer(40, 40));
            networks[i].addLayer(new Layer(40, 6));
        }
        trainer = new GeneticTrainer(FitnessFunction.tetris, 0.1);
    }

    public void train(int pTrainingEpisodes) {
        trainedGeneration = networks.clone();
        for (int i = 0; i < pTrainingEpisodes; i++) {
            trainedGeneration = trainer.trainGeneration(trainedGeneration);
            System.out.println("Generation: " + i);
        }

        String fileName = startTime+"_"+trainer.getCurrentGeneration();


        networks = trainedGeneration;
        //TODO: save weights
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
        toWrite = toWrite.replace('[', ' ');
        toWrite = toWrite.replace(']', ' ');
        toWrite = toWrite.trim();

        try {
            FileWriter is = new FileWriter(path);
            BufferedWriter buffer = new BufferedWriter(is);

            buffer.write(toWrite);
            buffer.close();
            is.close();
        } catch (Exception e) {

        }

        System.out.println("Speichern abgeschlossen.");
    }

    public void loadWeights(String pfad, NeuralNetwork pNetwork) {
        String temp = "";
        try {
            FileReader is = new FileReader(pfad);
            BufferedReader buffer = new BufferedReader(is);
            temp = buffer.readLine();
            buffer.close();
            is.close();
        } catch (Exception e) {
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
