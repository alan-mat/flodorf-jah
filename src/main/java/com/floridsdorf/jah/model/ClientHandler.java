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
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler(Socket socket, GameServer gameServer) {
        this.socket = socket;
        this.gameServer = gameServer;
        try {
            // Create input and output streams for the player
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        points = 0;
    }

    public void run() {
        try {
            // Read the player's name from the input stream
            playerName = in.readUTF().split(" ")[1];

            // Send a message to all other players about the new player
            gameServer.broadcastMessage("%PLAYER_CONNECTED " + playerName, this);

            while (true) {
                String input = in.readUTF();
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for sending a message to the player
    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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