package com.floridsdorf.jah.controller.viewControllers;

import com.floridsdorf.jah.util.ViewSwitcher;
import com.floridsdorf.jah.view.View;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DebugController {

    @FXML
    private Label welcomeText;

    public void onHomeButtonClick() {
        ViewSwitcher.switchTo(View.HOME);
    }

    public void onLobbyButtonClick() {
        ViewSwitcher.switchTo(View.LOBBY);
    }

    public void onMainGameButtonClick() {
        ViewSwitcher.switchTo(View.MAIN_GAME);
    }

    public void onResultButtonClick() {
        ViewSwitcher.switchTo(View.RESULT);
    }

    public void onVoteButtonClick() {
        ViewSwitcher.switchTo(View.VOTE);
    }
}