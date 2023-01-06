package com.floridsdorf.jah.model;

import com.floridsdorf.jah.controller.Controller;
import com.floridsdorf.jah.model.entries.PlayerEntry;
import com.floridsdorf.jah.model.entries.VoteEntry;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class GameClient implements Runnable{

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Controller controller;
    private List<PlayerEntry> leaderboardList;

    public GameClient(String ip, int port, Controller controller) throws IOException {
        socket = new Socket(ip, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        this.controller = controller;
        leaderboardList = new LinkedList<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        try {
            //wait for messages from server
            boolean run = true;
            while (run) {
                String input = in.readUTF();
                String command = input.split(" ")[0];   //extract command from msg
                String rem;
                try{
                    rem = input.split(" ", 2)[1];
                }catch (ArrayIndexOutOfBoundsException e){
                    rem = "";
                }
                switch (command) {
                    case "%ERROR" -> controller.displayErrorMsg(rem);
                    case "%CHAT" -> {
                        String[] split = rem.split(" ", 2);
                        controller.displayChatMsg(split[0], split[1]);
                    }
                    case "%PLAYER_CONNECTED" -> controller.displayPlayerConnected(rem);
                    case "%INFO" -> controller.displayServerInfo(rem);
                    case "%NEW_PROMPT" -> controller.newPrompt(rem);
                    case "%ANSWER_LIST" -> {
                        try {
                            List<String> answers = (List<String>) in.readObject();
                            controller.showAnswers(answers);
                        } catch (ClassNotFoundException e) {
                            controller.displayErrorMsg(String.format(
                                    "[ERROR]: Error receiving answer list%n%s", e.getMessage()));
                        }
                    }
                    case "%VOTE_LIST" -> {
                        try {
                            List<VoteEntry> votes = (List<VoteEntry>) in.readObject();
                            controller.showVotes(votes);
                        } catch (ClassNotFoundException e) {
                            controller.displayErrorMsg(String.format(
                                    "[ERROR]: Error receiving vote list%n%s", e.getMessage()));
                        }
                    }
                    case "%SEND_LEADERBOARD" -> {
                        try {
                            List<PlayerEntry> leaderboard = (List<PlayerEntry>) in.readObject();
                            leaderboardList = leaderboard;
                            controller.showLeaderboard(leaderboard);
                        } catch (ClassNotFoundException e) {
                            controller.displayErrorMsg(String.format(
                                    "[ERROR]: Error receiving leaderboard%n%s", e.getMessage()));
                        }
                    }
                    case "%WINNER_LIST" -> {
                        try {
                            List<String> winners = (List<String>) in.readObject();
                            controller.showWinners(winners);
                        } catch (ClassNotFoundException e) {
                            controller.displayErrorMsg(String.format(
                                    "[ERROR]: Error receiving leaderboard%n%s", e.getMessage()));
                        }
                    }
                    case "%TIMER_START" -> controller.timerStart(Integer.parseInt(rem));
                    case "%TIMER_STOP" -> controller.timerStop();
                    case "%GAME_START" -> controller.startGame();
                    case "%GAME_OVER" -> {
                        controller.gameOver();
                        run = false;
                    }
                    default -> controller.displayErrorMsg(
                            String.format("[ERROR]: Received unrecognized message from server:%n%s", input));
                }
            }
            socket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public void sendPlayerInfo(String playerName){
        sendMessage(String.format("%s %s", "%PLAYER", playerName));
    }

    public void sendChatMsg(String message){
        sendMessage(String.format("%s %s", "%CHAT", message));
    }

    public void sendReady(){sendMessage("%READY");}

    public void sendAnswer(String answer){ sendMessage(String.format("%s %s", "%ANSWER", answer)); }

    public void sendVote(int voteIndex){ sendMessage(String.format("%s %d", "%VOTE", voteIndex)); }

    public void updateLeaderboard(){
        sendMessage("%GET_LEADERBOARD");
    }

    public void sendDisconnect(){
        sendMessage("%DISCONNECT");
    }

    public List<PlayerEntry> getLeaderboard(){
        return leaderboardList;
    }

    private void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
