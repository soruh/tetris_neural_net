package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;

public class Gui extends Application {

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

        gc.setFill(Color.BLUE);
        gc.fillRect(100, 200, 300, 400);

        stage.show();



    }
}
