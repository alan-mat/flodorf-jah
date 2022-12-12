package com.floridsdorf.jah.networkTest;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    // Socket for the player's connection
    private Socket socket;

    // Reference to the server
    private GameServer gameServer;

    // Input and output streams for communication with the player
    private BufferedReader in;
    private PrintWriter out;

    // Player's name
    private String playerName;

    public ClientHandler(Socket socket, GameServer gameServer) {
        this.socket = socket;
        this.gameServer = gameServer;
        try {
            // Create input and output streams for the player
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            // Read the player's name from the input stream
            playerName = in.readLine();

            // Send a message to all other players about the new player
            gameServer.broadcastMessage("new-player " + playerName, this);

            String input;
            while ((input = in.readLine()) != null) {
                // If the player is ready to start the game, update the list of ready players on the server
                if (input.equals("ready")) {
                    gameServer.updateReadyPlayers(playerName, true);
                } else if (input.equals("not-ready")) {
                    gameServer.updateReadyPlayers(playerName, false);
                } else if (input.startsWith("answer")) {
                    // If the player has answered the current question, send the answer to the game
                    String[] parts = input.split(" ");
                    String answer = parts[1];
                    gameServer.getGame().receiveAnswer(playerName, answer);
                } else if (input.startsWith("vote")) {
                    // If the player has voted for an answer, send the vote to the game
                    String[] parts = input.split(" ");
                    int answerIndex = Integer.parseInt(parts[1]);
                    gameServer.getGame().receiveVote(playerName, answerIndex);
                } else if (input.equals("disconnect")) {
                    // If the player has disconnected, remove the client handler from the server
                    gameServer.removeClientHandler(this);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for sending a message to the player
    public void sendMessage(String message) {
        out.println(message);
    }
}