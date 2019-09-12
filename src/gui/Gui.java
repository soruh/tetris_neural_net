package gui;

import game_logic.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import network.FitnessFunction;
import network.Main;
import network.NeuralNetwork;

import java.util.ArrayList;
import java.util.Random;

public class Gui extends Application {
    private Game tetris;
    private Canvas canvas;
    private GraphicsContext gc;
    private Stage stage;
    private Scene scene;


    private Block currentBlock;
    private ArrayList<String> inputs = new ArrayList<>();
    private Action lastUserAction;
    private double d = 50.0;

    private Main trainer;
    private boolean trainMode = true;
    private NeuralNetwork network;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage pStage) {
        stage = pStage;
        stage.setTitle("Tetris AI");
        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        Group root = new Group();
        scene = new Scene(root);
        stage.setScene(scene);

        canvas = new Canvas(250, 500);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);


        trainer = new Main(200);
        network = trainer.getBestNetwork();

        trainer.loadWeights("/home/soruh/test_weights", network);

        Random rng = new Random();

        long seed = -420691337;
        // long seed = rng.nextLong();

        System.out.println("seed: "+seed);

        double expected_score = FitnessFunction.tetris.evaluate(network, seed);

        System.out.println("expected score: "+expected_score);


        tetris = new Game(seed);


        scene.setOnKeyPressed(
                keyEvent -> {
                    String code = keyEvent.getCode().toString();

                    if (code.equals("SPACE")) {
                        //trainMode = !trainMode;
                        //tetris = new Game(trainer.getCurrentSeed());
                    }

                    if (!inputs.contains(code)) {
                        inputs.add(code);
                    }
                }
        );

        scene.setOnKeyReleased(
                keyEvent -> {
                    String code = keyEvent.getCode().toString();
                    inputs.remove(code);
                }
        );

        new AnimationTimer() {
            public void handle(long now) {

                runGame();
                runGame();


            }
        }.start();


        stage.show();
    }

    public void setNetwork(NeuralNetwork pNetwork, long pSeed) {
        tetris = new Game(-420691337);
    }

    public void runGame() {
        Action action = determineAction();



        if (!tetris.tick(action)) {

            System.out.println("resulting score: "+ tetris.getGameState().getScore());


            System.exit(-1);
            return;
        }

        GameState state = tetris.getGameState();

        int[][] grid = state.getGrid();

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                int color;
                if (grid[y][x] == 0) {
                    int[][] blockSpots = state.getCurrentBlock().getAbsoluteCells();
                    boolean onBlock = false;
                    for (int[] xy : blockSpots) {
                        if (y == xy[0] && x == xy[1]) {
                            onBlock = true;
                        }
                    }
                    if (onBlock) {
                        color = state.getCurrentBlock().getType();
                    } else {
                        color = 0;
                    }
                } else {
                    color = grid[y][x];
                }

                int brightness = 110;
                switch (color) {
                    case 0:
                        gc.setFill(Color.rgb(240, 240, 240));
                        break;
                    case 1:
                        gc.setFill(Color.rgb(0, 2 * brightness, 2 * brightness));
                        break;
                    case 2:
                        gc.setFill(Color.rgb(2 * brightness, 2 * brightness, 0));
                        break;
                    case 3:
                        gc.setFill(Color.rgb(2 * brightness, 0, 2 * brightness));
                        break;
                    case 4:
                        gc.setFill(Color.rgb(0, 2 * brightness, 0));
                        break;
                    case 5:
                        gc.setFill(Color.rgb(2 * brightness, 0, 0));
                        break;
                    case 6:
                        gc.setFill(Color.rgb(0, 0, 2 * brightness));
                        break;
                    case 7:
                        gc.setFill(Color.rgb(2 * brightness, brightness, 0));
                        break;

                }
                gc.fillRect(1 + (x * 25), 476 - (y * 25), 23, 23);
            }

            stage.show();
        }
    }

    private void reset() {
        if (trainMode) {

        } else {
            tetris = new Game(-420691337);
        }

        lastUserAction = Action.NOTHING;
        currentBlock = null;
    }

    private Action determineAction() {
        Action action = Action.NOTHING;

        if (trainMode) {
            double[] rawOutput = network.forwardPass(tetris.getGameState().flattenedState());

            int biggestOutputIndex = 0;
            for (int i = 0; i < rawOutput.length; i++) {
                if (rawOutput[i] >= rawOutput[biggestOutputIndex]) biggestOutputIndex = i;
            }

            switch (biggestOutputIndex){
                case 0: action = Action.NOTHING; break;
                case 1: action = Action.LEFT; break;
                case 2: action = Action.RIGHT; break;
                case 3: action = Action.DOWN; break;
                case 4: action = Action.TURN_LEFT; break;
                case 5: action = Action.TURN_RIGHT; break;
                // case 6: nextAction = Action.HOLD_PIECE; break; //Too difficult

            }

        } else {
            for (int i = 0; i < inputs.size(); i++) {
                switch (inputs.get(i)) {
                    case "LEFT":
                        if (lastUserAction != Action.LEFT) {
                            action = Action.LEFT;
                            lastUserAction = Action.LEFT;
                        }
                        break;
                    case "RIGHT":
                        if (lastUserAction != Action.RIGHT) {
                            action = Action.RIGHT;
                            lastUserAction = Action.RIGHT;
                        }
                        break;
                    case "UP":
                        if (lastUserAction != Action.TURN_RIGHT) {
                            action = Action.TURN_RIGHT;
                            lastUserAction = Action.TURN_RIGHT;
                        }
                        break;
                    case "DOWN":
                        Block newCurrentBlock = tetris.getGameState().getCurrentBlock();
                        if (lastUserAction != Action.DOWN) {
                            currentBlock = newCurrentBlock;
                        }
                        if (currentBlock == newCurrentBlock) {
                            action = Action.DOWN;
                            lastUserAction = Action.DOWN;
                        }
                        break;
                    case "SHIFT":
                        if (lastUserAction != Action.TURN_LEFT) {
                            action = Action.TURN_LEFT;
                            lastUserAction = Action.TURN_LEFT;
                        }
                        break;
                    default:
                        action = Action.NOTHING;
                        lastUserAction = Action.NOTHING;
                }
            }

            if (inputs.size() == 0) {
                lastUserAction = action;
            }
        }

        return action;
    }
}
