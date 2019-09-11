package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

public class Main {

    private GeneticTrainer trainer;
    private NeuralNetwork[] networks;
    private NeuralNetwork[] trainedGeneration;

    public static void main(String[] args) {
        Main main = new Main();
        main.train(10);
    }

    public Main(){
        networks = new NeuralNetwork[100];
        for (int i = 0; i < networks.length; i++) {
            networks[i] = new NeuralNetwork();
            networks[i].addLayer(new Layer(200, 400));
            networks[i].addLayer(new Layer(400, 400));
            networks[i].addLayer(new Layer(400, 40));
            networks[i].addLayer(new Layer(40, 6));
        }
        trainer = new GeneticTrainer(FitnessFunction.tetris);
    }

    public void train(int pTrainingEpisodes){
        trainedGeneration = networks.clone();
        for (int i = 0; i < pTrainingEpisodes; i++) {
            trainedGeneration = trainer.trainGeneration(trainedGeneration);
        }
        //to do: save weights
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
        } catch (Exception e) {

        }

        System.out.println("Speichern abgeschlossen.");
    }

    public void loadWeights(String pfad, NeuralNetwork pNetwork)
    {
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