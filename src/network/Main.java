package network;

public class Main {

    private GeneticTrainer trainer;
    private NeuralNetwork[] networks;
    private NeuralNetwork[] trainedGeneration;

    public static void main(String[] args) {
        Main main = new Main();
        main.train();
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
        trainedGeneration
        for (int i = 0; i < pTrainingEpisodes; i++) {
            trainedGeneration = trainer.trainGeneration()
        }
    }
}
