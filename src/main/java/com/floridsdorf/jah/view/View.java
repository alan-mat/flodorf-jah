package com.floridsdorf.jah.view;

public enum View {
    DEBUG("debug.fxml"),
    HOME("home.fxml"),
    LOBBY("lobby.fxml"),
    MAIN_GAME("main-game.fxml"),
    RESULT("result.fxml"),
    VOTE("vote.fxml");

    private final String fileName;

    View(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
