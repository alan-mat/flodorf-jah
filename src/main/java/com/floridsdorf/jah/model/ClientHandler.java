package com.floridsdorf.jah.model;

import java.io.*;
import java.net.*;

/**
 * Server-side
 */
public class ClientHandler implements Runnable {

    private String playerName;
    private int points;
    private Socket socket;
    private GameServer gameServer;
    private BufferedReader in;
    private PrintWriter out;

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
        points = 0;
    }

    public void run() {
        try {
            // Read the player's name from the input stream
            playerName = in.readLine().split(" ")[1];

            // Send a message to all other players about the new player
            gameServer.broadcastMessage("%PLAYER_CONNECTED " + playerName, this);

            String input;
            while ((input = in.readLine()) != null) {
                String command = input.split(" ")[0];    //extract command out of msg

                switch (command) {
                    case "%CHAT" -> gameServer.broadcastMessage(String.format("%s [%s]> %s", "%CHAT", playerName,
                            input.split(" ", 2)[1]), this);
                    case "%READY" -> gameServer.updateReadyPlayers(playerName, true);
                    case "%NOT_READY" -> gameServer.updateReadyPlayers(playerName, false);
                    case "%ANSWER" -> gameServer.getGameHandler().addAnswer(this,
                            input.split(" ", 2)[1]);
                    case "%VOTE" -> gameServer.getGameHandler().addVote(this,
                            Integer.parseInt(input.split(" ", 2)[1]));
                    case "%DISCONNECT" -> {
                        gameServer.removeClientHandler(this);
                        socket.close();
                        return;
                    }
                    default -> sendMessage("%ERROR Invalid server command!");
                }

                /*// If the player is ready to start the game, update the list of ready players on the server
                if (input.equals("ready")) {
                    gameServer.updateReadyPlayers(playerName, true);
                } else if (input.equals("not-ready")) {
                    gameServer.updateReadyPlayers(playerName, false);
                } else if (input.startsWith("answer")) {
                    // If the player has answered the current question, send the answer to the game
                    String[] parts = input.split(" ");
                    String answer = parts[1];
                    //gameServer.getGame().receiveAnswer(playerName, answer);
                } else if (input.startsWith("vote")) {
                    // If the player has voted for an answer, send the vote to the game
                    String[] parts = input.split(" ");
                    int answerIndex = Integer.parseInt(parts[1]);
                    //gameServer.getGame().receiveVote(playerName, answerIndex);
                } else if (input.equals("%DISCONNECT")) {
                    // If the player has disconnected, remove the client handler from the server
                    gameServer.removeClientHandler(this);
                }*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for sending a message to the player
    public void sendMessage(String message) {
        out.println(message);
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        if(points >= 0)
            this.points += points;
    }

}