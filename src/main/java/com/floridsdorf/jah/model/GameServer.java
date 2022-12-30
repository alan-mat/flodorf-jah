package com.floridsdorf.jah.model;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer implements Runnable{
    // Maximum number of players in the game
    private static final int MAX_PLAYERS = 10;

    // Minimum number of players required to start the game
    private static final int MIN_PLAYERS = 2;

    // Time in seconds for each round
    public static final int ROUND_TIME = 30;

    // List of player names that are ready to start the game
    private List<String> readyPlayers = new ArrayList<>();

    // Server socket for accepting new connections
    private ServerSocket serverSocket;

    // List of client handlers for connected players
    private List<ClientHandler> clientHandlers = new ArrayList<>();

    private GameHandler gameHandler;

    public GameServer(int port) throws IOException {
        // Create a new server socket and bind it to the specified port
        try {
            serverSocket = new ServerSocket(port);
            System.out.printf("Running on port %d.%n", port);
        } catch (IOException e) {
            System.out.println("Could not listen on port: " + port);
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        System.out.println("Awaiting clients ...");
        try {
            while (true) {
                // Accept a new connection from a player
                Socket socket = serverSocket.accept();

                // Create a new client handler for the player
                ClientHandler clientHandler = new ClientHandler(socket, this);
                clientHandlers.add(clientHandler);

                // Start a new thread for the client handler
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for updating the list of ready players
    public void updateReadyPlayers(String playerName, boolean isReady) {
        if (isReady) {
            readyPlayers.add(playerName);
        } else {
            readyPlayers.remove(playerName);
        }

        // If the number of ready players is greater than or equal to the minimum required players, start the game
        if (readyPlayers.size() >= MIN_PLAYERS && readyPlayers.size() == clientHandlers.size()) {
            gameHandler = new GameHandler();
        }
    }


    // Method for removing a client handler from the list
    public void removeClientHandler(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);

        // If there is only one player remaining, end the game
        if (clientHandlers.size() == 1) {
            clientHandlers.get(0).sendMessage("game-over");
        }
    }

    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != sender) {
                clientHandler.sendMessage(message);
            }
        }
    }

    public GameHandler getGameHandler(){return gameHandler;}

    public static void main(String[] args) {
        try {
            // Create a new server instance and start it
            GameServer gameServer = new GameServer(7777);
            gameServer.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
