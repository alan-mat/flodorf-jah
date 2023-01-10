package com.floridsdorf.jah.controller;

import com.floridsdorf.jah.exceptions.NotAHostException;
import com.floridsdorf.jah.model.GameClient;
import com.floridsdorf.jah.model.GameServer;
import com.floridsdorf.jah.model.entries.PlayerEntry;
import com.floridsdorf.jah.model.entries.VoteEntry;
import com.floridsdorf.jah.view.PrototypeView;

import java.io.IOException;
import java.util.List;

/**
 * Main Controller
 * Implements the Singleton pattern
 */
public class MainController {

    private static MainController mainControllerObject;
    public final static int SERVER_PORT = 7777;

    private PrototypeView view;
    private GameServer gameServer;
    private GameClient gameClient;

    /**
     * @return Singleton instance of Main Controller
     */
    public static MainController getInstance(){
        if(mainControllerObject == null){
            mainControllerObject = new MainController();
        }
        return mainControllerObject;
    }

    public MainController(){
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

    public void displayServerInfo(String message){
        StringBuilder sb = new StringBuilder("[SYSTEM] ");
        for(String s : message.split("%>")){
            sb.append(s).append(System.lineSeparator());
        }
        view.printText(sb.toString());
    }

    public void displayPlayerConnected(String playerName){
        view.printText(String.format("[SYSTEM] Player %s has connected.", playerName));
    }

    public void displayChatMsg(String playerName, String message){
        view.printText(String.format("[%s]> %s", playerName, message));
    }

    public void displayErrorMsg(String message){
        view.printText(message, true);
    }

    public void newPrompt(String prompt){
        //TODO: proper new round display | new prompt display
        view.printText(prompt);
    }

    public void startGame(){
        //TODO: go from "lobby" state to actual game state/view
        view.printText("[SYSTEM] Game started.");
        view.printText("WELCOME TO JOKES AGAINST HUMANITY !!!");
    }

    public void showAnswers(List<String> answers){
        view.printText("[SYSTEM] This round's answers:");
        int i = 0;
        for(String answer : answers){
            view.printText(String.format("%d: %s", ++i, answer));
        }
    }

    public void showVotes(List<VoteEntry> votes){
        view.printText("[SYSTEM] Votes are in:");
        int i = 0;
        for(VoteEntry vote : votes){
            view.printText(String.format("%d: [%s] %s (%d)", ++i, vote.playerName, vote.answer, vote.voteAmount));
        }
    }

    public void showLeaderboard(List<PlayerEntry> players){
        view.printText("[SYSTEM] Leaderboard:");
        int i = 0;
        for(PlayerEntry player : players){
            view.printText(String.format("%d: %s (%d)", ++i, player.playerName, player.points));
        }
    }

    public void showWinners(List<String> winners){
        view.printText("[SYSTEM] Winner(s):");
        int i = 0;
        for(String winner : winners){
            view.printText(String.format("%d: %s", ++i, winner));
        }
    }

    /**
     * Wird aufgerufen wenn der Server einen Timer startet
     * Nutzen: client-seitigen Timer zu starten und anzuzeigen
     * @param seconds   anzahl an sekunden für die der server wartet
     */
    public void timerStart(int seconds){
        //implement function
    }

    /**
     * Wird aufgerufen wenn der Server Timer vorbei ist,
     * ist relevant wenn der Client-seitige Timer mit dem Server-seitigen
     * desynchronisiert ist.
     */
    public void timerStop(){
        //implement function
    }

    public void gameOver(){
        view.printText("[SYSTEM] Game is over.");
        //TODO: actual game over handling
        System.exit(0);
    }

    public GameServer getGameServer() throws NotAHostException {
        if(gameServer == null)
            throw new NotAHostException();
        return gameServer;
    }

    public GameClient getGameClient(){return gameClient;}

    public static void main(String[] args) {
        MainController.getInstance();
    }

}
