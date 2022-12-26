package com.floridsdorf.jah;

import com.floridsdorf.jah.util.ViewSwitcher;
import com.floridsdorf.jah.view.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class JahApplication extends Application {

    private static final int INITIAL_WIDTH = 900;
    private static final int INITIAL_HEIGHT = 600;

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Pane(), INITIAL_WIDTH, INITIAL_HEIGHT);

        ViewSwitcher.setScene(scene);
        ViewSwitcher.switchTo(View.DEBUG);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}