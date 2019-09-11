package gui;

import game_logic.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;

import java.util.Random;

public class bad_gui_to_test extends Application {
    private Game tetris;
    private Canvas canvas;
    private GraphicsContext gc;
    private Stage stage;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Tetris AI");

        Group root = new Group();
        stage.setScene(new Scene(root));

        Canvas canvas = new Canvas(500, 1000);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        Random r = new Random();
        tetris = new Game(r.nextLong());

        gc.setFill(Color.BLUE);
        gc.fillRect(5, 5, 40, 40);

        stage.show();

        //while (true) {
            //tetris.tick(Action.NOTHING);
            //GameState state = tetris.getGameState();

            //System.out.println(state.getScore());

            //int[][] grid = state.getGrid();

            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 20; y++) {
                    int color = 0;
                    /*if (grid[y][x] < 0) {
                        color = state.getCurrentBlock().getType();
                    } else {
                        color = grid[x][y];
                    }*/


                    // DEBUGGING !!!!!!!!!!

                    color = r.nextInt(8);

                    // DEBUGGING !!!!!!!!!!!!!


                    int brightness = 110;
                    switch (color) {
                        case 0:
                            gc.setFill(Color.rgb(240, 240, 240));
                            break;
                        case 1:
                            gc.setFill(Color.rgb(0, 2*brightness, 2*brightness));
                            break;
                        case 2:
                            gc.setFill(Color.rgb(2*brightness, 2*brightness, 0));
                            break;
                        case 3:
                            gc.setFill(Color.rgb(2*brightness, 0, 2*brightness));
                            break;
                        case 4:
                            gc.setFill(Color.rgb(0, 2*brightness, 0));
                            break;
                        case 5:
                            gc.setFill(Color.rgb(2*brightness, 0, 0));
                            break;
                        case 6:
                            gc.setFill(Color.rgb(0, 0, 2*brightness));
                            break;
                        case 7:
                            gc.setFill(Color.rgb(2*brightness, brightness, 0));
                            break;

                    }
                    gc.fillRect(2 + x * 50, 952 - (y * 50), 46, 46);
                }
                stage.show();
                try {
                    Thread.sleep(33);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        //}
    }

    private void run() {

    }
}
