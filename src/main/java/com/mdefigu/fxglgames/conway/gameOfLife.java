package com.mdefigu.fxglgames.conway;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class gameOfLife extends Application {

    private static final int GRID_SIZE_CELLS = 3;
    private static final int CELLS_SIZE = 30;
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(GRID_SIZE_CELLS * CELLS_SIZE, GRID_SIZE_CELLS * CELLS_SIZE );

        for (int y = 0; y < GRID_SIZE_CELLS; y++) {
            for (int x = 0; x < GRID_SIZE_CELLS; x++){
                Cell cell = new Cell(x, y);

                root.getChildren().add(cell);
            }
        }
        return root;
    }

    private static class Cell extends StackPane {

        private Boolean isFlipped = false;

        private Rectangle bg;
        Cell(int x, int y) {
            setTranslateX(x * CELLS_SIZE);
            setTranslateY(y * CELLS_SIZE);

            bg = new Rectangle(CELLS_SIZE, CELLS_SIZE);

            getChildren().add(bg);
        }
        void flip() {
            isFlipped = !isFlipped;

            bg.setFill(isFlipped ? Color.ANTIQUEWHITE : Color.BLACK);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
