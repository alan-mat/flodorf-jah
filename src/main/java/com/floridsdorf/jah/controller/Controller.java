package com.floridsdorf.jah.controller;

import com.floridsdorf.jah.model.GameHandler;
import com.floridsdorf.jah.model.Player;
import com.floridsdorf.jah.view.PrototypeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        if(winningPlayers.size() == 1)
            view.printText(String.format("%s wins the game! Congratulations!", winningPlayers.get(0).getUserName()));
        else {
            StringBuilder sb = new StringBuilder();
            sb.append("It's a tie!").append(System.lineSeparator());
            for (Player p : winningPlayers)
                sb.append(p.getUserName());
            sb.append(System.lineSeparator());
            view.printText(sb.toString());
        }
        return true;
    }

    public void addPlayer(String name){
        gameHandler.addPlayer(name);
    }

    public void addAnswer(String name, String answer){ gameHandler.addAnswer(name, answer); }
    public String addPoint(Map<String, Integer> votes){
        Player p = gameHandler.addPoint(votes);
        if(p == null){
            System.err.println("PLAYER COULD NOT BE FOUND! THIS SHOULD NOT HAPPEN");
            System.exit(1); //panic exit program
        }
        return String.format("%s gets the point%n%s now has %d points.%n",
                p.getUserName(), p.getUserName(), p.getPoints());
    }

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
