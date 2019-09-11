package network;

public class Main {

    private GeneticTrainer trainer;
    private NeuralNetwork[] networks;

    public static void main(String[] args) {
        Main main = new Main();
        main.train();
    }

    public Main(){
        networks = new NeuralNetwork[100];
        for (int i = 0; i < networks.length; i++) {
            networks[i] = new NeuralNetwork();
            networks[i].addLayer(200, )
        }
        trainer = new GeneticTrainer();
    }

    public void train(int pTrainingEpisodes){

    }
}
