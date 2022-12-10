package com.floridsdorf.jah.controller;

import com.floridsdorf.jah.model.GameHandler;
import com.floridsdorf.jah.model.Player;
import com.floridsdorf.jah.view.PrototypeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Prototype Controller
 */
public class Controller {

    private GameHandler gameHandler;
    private PrototypeView view;

    public Controller(){
        gameHandler = new GameHandler();
        view = new PrototypeView(this);

        view.start();
    }

    /**
     * @return  true if game is over, false otherwise
     */
    public boolean endRound(){
        List<Player> winningPlayers = gameHandler.endRound();
        if(winningPlayers == null)
            return false;
        //TODO: output some end of game messages showing winner(s) etc.
        return true;
    }

    public void addPlayer(String name){
        gameHandler.addPlayer(name);
    }

    public void addAnswer(String name, String answer){ gameHandler.addAnswer(name, answer); }
    public void addPoint(String name){ gameHandler.addPoint(name); }

    public List<String> getPlayers(){
        List<String> players = new ArrayList<>();
        for (Player p : gameHandler.getPlayers())
            players.add(p.getUserName());
        return players;
    }

    public String getPrompt(){ return gameHandler.getRandomPrompt(); }

    public List<String> getAnswers(){ return gameHandler.getAnswers(); }

    public static void main(String[] args) {
        new Controller();
    }

}
