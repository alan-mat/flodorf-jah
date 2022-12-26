package com.floridsdorf.jah.view.javafxView;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class FxView extends Application {

    private BorderPane root1;
    private BorderPane root2;
    private Scene scene1;
    private Scene scene2;
    public int scorePlayer1 = 0;
    public int scorePlayer2 = 0;
    Label labelScorePlayer1 = new Label("Spieler 1: " + Integer.toString(scorePlayer1));
    Label labelScorePlayer2 = new Label("Spieler 1: " + Integer.toString(scorePlayer2));
    Stage window;

    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        window.setTitle("Jokes Against Humanity"); //name of window
        // Layout for scene 1 and 2
        root1 = new BorderPane();
        root2 = new BorderPane();
        Button testButton = new Button("Test");
        Label label1 = new Label("Jokes");
        Label label2 = new Label("Scoreboard");

        //label for scoreboard
        labelScorePlayer1.setFont(Font.font(17));
        labelScorePlayer1.setTextFill(Color.CORNFLOWERBLUE);
        labelScorePlayer2.setFont(Font.font(17));
        labelScorePlayer2.setTextFill(Color.BURLYWOOD);

        //label 1 settings
        label1.setFont(new Font("Times new Roman", 25));
        BorderPane.setAlignment(label1, Pos.TOP_CENTER);
        BorderPane.setMargin(label1, new Insets(5, 0, 5,0));
        //label 2 settings
        label2.setFont(new Font("Times new Roman", 25));
        BorderPane.setAlignment(label2, Pos.TOP_CENTER);
        BorderPane.setMargin(label2, new Insets(5, 0, 5,0));

        //buttons define
        Button button1 = createButton("switch to scoreboard");
        Button button2 = createButton("switch to jokes");
        Button buttonSpieler1 = createButton("I'm for player 1");
        Button buttonSpieler2 = createButton("I'm for player 2");
        Button buttonClose = createButton("end");
        BorderPane.setMargin(buttonClose, new Insets(12, 12, 12, 12));


        // scene 1
        VBox verticalLayout1 = new VBox(50);
        verticalLayout1.getChildren().addAll(button1, buttonSpieler1, buttonSpieler2, labelScorePlayer1);
        root1.setTop(label1);
        root1.setCenter(verticalLayout1);
        root1.setBottom(buttonClose);
        scene1 = new Scene(root1, 500, 500);
        button1.setOnAction(e -> window.setScene(scene2));
        buttonSpieler1.setOnAction(e -> incrementScorePlayer1());
        buttonSpieler2.setOnAction(e -> incrementScorePlayer2());
        buttonClose.setOnAction(e -> window.close());

        // scene 2
        VBox verticalLayout2 = new VBox(50);
        verticalLayout2.getChildren().addAll(button2, labelScorePlayer1, labelScorePlayer2);
        button2.setOnAction(e -> window.setScene(scene1));
        root2.setTop(label2);
        root2.setCenter(verticalLayout2);
        scene2 = new Scene(root2, 600, 600);

        window.setScene(scene1);
        window.show();
    }

    private Button createButton(String text){
        Button button = new Button();
        button.setText(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);
        button.setMinWidth(150);
        button.setFont(new Font("Arial", 12));
        return button;
    }

    private void incrementScorePlayer1(){
        scorePlayer1 += 100;
        labelScorePlayer1.setText("Player 1: " + Integer.toString(scorePlayer1));
    }

    private void incrementScorePlayer2(){
        scorePlayer2 += 100;
        labelScorePlayer2.setText("Player 2: " + Integer.toString(scorePlayer2));
    }

}
