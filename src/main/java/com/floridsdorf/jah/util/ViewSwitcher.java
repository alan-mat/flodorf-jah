package com.floridsdorf.jah.util;

import com.floridsdorf.jah.view.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public class ViewSwitcher {

    private static Scene scene;

    public static void setScene(Scene scene) {
        ViewSwitcher.scene = scene;
    }

    public static void switchTo(View view) {
        if (scene == null) {
            return;
        }

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(View.class.getResource(view.getFileName())));
            scene.setRoot(root);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
