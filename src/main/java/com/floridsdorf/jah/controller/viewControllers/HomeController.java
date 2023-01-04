package com.floridsdorf.jah.controller.viewControllers;

import com.floridsdorf.jah.model.game.Player;
import com.floridsdorf.jah.util.ViewSwitcher;
import com.floridsdorf.jah.view.View;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Button startButton;

    @FXML
    private Button exitButton;

    @FXML
    private TextField nameInput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableStartButtonOnNameInputInvalid();
    }

    private void disableStartButtonOnNameInputInvalid() {
        startButton.disableProperty()
                   .bind(Bindings.createBooleanBinding(() -> nameInput.getText().trim().isEmpty(), nameInput.textProperty()));
    }

    @FXML
    private void onStartGameButtonClick() {
        Player player = new Player(nameInput.getText(), List.of());
        ViewSwitcher.switchTo(View.LOBBY);
        // TODO: save player name
    }

    @FXML
    private void onExitButtonClick() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
