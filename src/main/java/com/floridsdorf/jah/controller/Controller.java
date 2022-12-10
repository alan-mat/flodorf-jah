package com.floridsdorf.jah.controller;

import com.floridsdorf.jah.model.GameHandler;

/**
 * Prototype Controller
 */
public class Controller {

    private GameHandler gameHandler;

    public Controller(){
        gameHandler = new GameHandler();
    }

    public static void main(String[] args) {
        new Controller();
    }

}
