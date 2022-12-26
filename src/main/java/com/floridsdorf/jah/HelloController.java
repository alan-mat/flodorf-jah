package com.floridsdorf.jah;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private AnchorPane rootPane;


    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void onHomeButtonClick(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("templates/home.fxml")));
        rootPane.getChildren().setAll(anchorPane);
    }

    public void onLobbyButtonClick(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("templates/lobby.fxml")));
        rootPane.getChildren().setAll(anchorPane);
    }

    public void onMainGameButtonClick(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("templates/mainGame.fxml")));
        rootPane.getChildren().setAll(anchorPane);
    }

    public void onResultButtonClick(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("templates/result.fxml")));
        rootPane.getChildren().setAll(anchorPane);
    }

    public void onVoteButtonClick(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("templates/vote.fxml")));
        rootPane.getChildren().setAll(anchorPane);
    }
}