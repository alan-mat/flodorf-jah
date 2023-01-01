package com.floridsdorf.jah.model;

import com.floridsdorf.jah.controller.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient implements Runnable{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Controller controller;

    public GameClient(String ip, int port, Controller controller) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.controller = controller;
    }

    @Override
    public void run() {
        String input;
        try {
            //wait for messages from server
            while ((input = in.readLine()) != null) {
                String command = input.split(" ")[0];   //extract command from msg
                String rem;
                try{
                    rem = input.split(" ", 2)[1];
                }catch (ArrayIndexOutOfBoundsException e){
                    rem = "";
                }
                switch (command) {
                    case "%CHAT" -> controller.displayChatMsg(rem);
                    case "%PLAYER_CONNECTED" -> controller.displayPlayerConnected(rem);
                    case "%INFO" -> controller.displayServerInfo(rem);
                    case "%NEW_PROMPT" -> controller.newPrompt(rem);
                    case "%GAME_START" -> controller.startGame();
                    case "%GAME_OVER" -> controller.gameOver();
                    default -> System.err.printf("[ERROR]: Received unrecognized message from server:%n%s", input);
                }
            }
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

    public void sendDisconnect(){
        sendMessage("%DISCONNECT");
    }

    private void sendMessage(String message) {
        out.println(message);
    }

}
