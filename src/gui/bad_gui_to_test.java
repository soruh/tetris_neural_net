package gui;

import com.sun.webkit.ThemeClient;
import game_logic.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.event.*;
import javafx.stage.WindowEvent;

import java.util.Random;

public class bad_gui_to_test extends Application {
    private Game tetris;
    private Canvas canvas;
    private GraphicsContext gc;
    private Stage stage;

    private boolean oddFrame = false;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage pStage) throws Exception {
        stage = pStage;
        stage.setTitle("Tetris AI");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        Group root = new Group();
        stage.setScene(new Scene(root));

        canvas = new Canvas(500, 1000);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        Random r = new Random();
        tetris = new Game(r.nextLong());

        //gc.setFill(Color.BLUE);
        //gc.fillRect(5, 5, 40, 40);

        new AnimationTimer() {
            public void handle(long now) {
                if (oddFrame) {
                    runGame();
                }
                oddFrame = !oddFrame;
            }
        }.start();

        stage.show();
    }

    public void runGame() {
        if (!tetris.tick(Action.DOWN)) {
            System.out.println("Your final score is: " + tetris.getGameState().getScore());
            Platform.exit();
            System.exit(0);
            return;
        }
        GameState state = tetris.getGameState();

        System.out.println(state.getScore());

        int[][] grid = state.getGrid();

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                int color = 0;
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
                gc.fillRect(2 + x * 50, 952 - (y * 50), 46, 46);
            }

            stage.show();
        }
    }

}
