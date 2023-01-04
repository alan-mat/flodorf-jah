package com.floridsdorf.jah.model;

import com.floridsdorf.jah.controller.Controller;

import java.io.*;
import java.net.Socket;

public class GameClient implements Runnable{

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Controller controller;

    public GameClient(String ip, int port, Controller controller) throws IOException {
        socket = new Socket(ip, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        this.controller = controller;
    }

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
                    case "%CHAT" -> controller.displayChatMsg(rem);
                    case "%PLAYER_CONNECTED" -> controller.displayPlayerConnected(rem);
                    case "%INFO" -> controller.displayServerInfo(rem);
                    case "%NEW_PROMPT" -> controller.newPrompt(rem);
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

    public void sendDisconnect(){
        sendMessage("%DISCONNECT");
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
