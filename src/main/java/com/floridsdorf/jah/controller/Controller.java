package com.floridsdorf.jah.controller;

import com.floridsdorf.jah.exceptions.NotAHostException;
import com.floridsdorf.jah.model.GameClient;
import com.floridsdorf.jah.model.GameHandler;
import com.floridsdorf.jah.model.GameServer;
import com.floridsdorf.jah.model.Player;
import com.floridsdorf.jah.view.PrototypeView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Prototype Controller
 */
public class Controller {

    public final static int SERVER_PORT = 7777;

    private PrototypeView view;
    private GameServer gameServer;
    private GameClient gameClient;

    public Controller(){
        view = new PrototypeView(this);

        view.start();
    }

    public void hostGame() throws IOException {
        gameServer = new GameServer(SERVER_PORT);
        new Thread(gameServer).start(); //start the server in a new thread
        //a host also requires its own game client instance
        joinGame("127.0.0.1", SERVER_PORT);
    }

    public void joinGame(String hostIP, int port) throws IOException {
        gameClient = new GameClient(hostIP, port, this);
        new Thread(gameClient).start();
    }

    public void displayPlayerConnected(String playerName){
        view.printText(String.format("[SYSTEM] Player %s has connected.", playerName));
    }

    public void displayChatMsg(String message){
        view.printText(message);
    }

    public void gameOver(){
        view.printText("[SYSTEM] Game is over.");
        //TODO: actual game over handling
        System.exit(0);
    }

    /**
     * @return  true if game is over, false otherwise
     */
    /*public boolean endRound(){
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

    public List<String> getAnswers(){ return gameHandler.getAnswers(); }*/

    public GameServer getGameServer() throws NotAHostException {
        if(gameServer == null)
            throw new NotAHostException();
        return gameServer;
    }

    public GameClient getGameClient(){return gameClient;}

    public static void main(String[] args) {
        new Controller();
    }

}
