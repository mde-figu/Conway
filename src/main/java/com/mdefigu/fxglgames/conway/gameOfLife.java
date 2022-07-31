package com.mdefigu.fxglgames.conway;

import com.almasb.fxgl.app.GameSettings;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;


public class gameOfLife extends Application {
    private static final int GRID_SIZE_CELLS = 30;
    private static final int CELLS_SIZE = 30;
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(mainMenu()));
        //stage.setScene(new Scene(createContent()));
        stage.show();

        //playSound("src/main/resources/assets/music/NoTimeForCaution.wav");

    }
    private Parent mainMenu() {
        Pane mainMenuRoot = new Pane();
        Stage returnStage = new Stage();
        Rectangle bg = new Rectangle(1280, 720);
        Font font = Font.font(72);

        javafx.scene.control.Button btnSet = new Button("Set");
        btnSet.setFont(font);
        btnSet.setFont(font);
        btnSet.setOnAction(event -> {
            try {
                returnStage.setScene(new Scene(createContent()));
                returnStage.show();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        VBox vbox = new VBox(50, btnSet);
        vbox.setTranslateX(400);
        vbox.setTranslateY(200);
        mainMenuRoot.getChildren().addAll(bg, vbox);
        return mainMenuRoot;
    }

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(GRID_SIZE_CELLS * CELLS_SIZE, GRID_SIZE_CELLS * CELLS_SIZE );
        ObjectProperty<Border> border = root.borderProperty();


        for (int y = 0; y < GRID_SIZE_CELLS; y++) {
            for (int x = 0; x < GRID_SIZE_CELLS; x++){
                Cell cell = new Cell(x, y);

                cell.setOnMouseClicked(e -> {
                    cell.flip();
                });
                root.getChildren().add(cell);
            }
        }
        root.setOnKeyPressed(ke -> {
            KeyCode keyCode = ke.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                //TODO
                //playSound("src/main/resources/assets/music/NoTimeForCaution.wav");
            }
        });
        return root;
    }

    private static class Cell extends StackPane {

        private Boolean isFlipped = false;

        private Rectangle bg;
        Cell(int x, int y) {
            setTranslateX(x * CELLS_SIZE);
            setTranslateY(y * CELLS_SIZE);

            bg = new Rectangle(CELLS_SIZE, CELLS_SIZE);
            bg.setStroke(Color.WHITE);
            getChildren().add(bg);
        }
        void flip() {
            isFlipped = !isFlipped;

            bg.setFill(isFlipped ? Color.ANTIQUEWHITE : Color.BLACK);
        }
    }

    private final int BUFFER_SIZE = 128000;
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;

    /**
     * @param filename the name of the file that is going to be played
     */
    public void playSound(String filename){

        String strFilename = filename;

        try {
            soundFile = new File(strFilename);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        sourceLine.start();

        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }

        sourceLine.drain();
        sourceLine.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
