package com.floridsdorf.jah.networkTest;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {
    // Maximum number of players in the game
    private static final int MAX_PLAYERS = 10;

    // Minimum number of players required to start the game
    private static final int MIN_PLAYERS = 2;

    // Time in seconds for each round
    private static final int ROUND_TIME = 30;

    // Game questions
    private List<String> allQuestionsGame = new ArrayList<>();

    // Map of connected players and their scores
    private Map<String, Integer> playerScores = new HashMap<>();

    // List of player names that are ready to start the game
    private List<String> readyPlayers = new ArrayList<>();

    // Server socket for accepting new connections
    private ServerSocket serverSocket;

    private Game game;

    // List of client handlers for connected players
    private List<ClientHandler> clientHandlers = new ArrayList<>();

    public GameServer(int port) throws IOException {
        // Create a new server socket and bind it to the specified port
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Could not listen on port: " + port);
            System.exit(-1);
        }

        // Load the game questions from a file or database
        loadQuestions(); // LOADS HERE BECAUSE I WANT IT TO LOAD LATER FROM JSON NOW ITS USELESS
    }

    // Method for loading game questions from a file or database

        // Method for loading the game questions from a JSON file (LATER)
        public List<String> loadQuestions() throws IOException {
            List<String> questions = new ArrayList<>();
            //      File file = new File("questions.json");

            questions.add("Question 1?");
            questions.add("Question 2?");
            questions.add("Question 3?");
            questions.add("Question 4?");
            questions.add("Question 5?");
            questions.add("Question 6?");
            questions.add("Question 7?");
            questions.add("Question 8?");
            questions.add("Question 9?");
            questions.add("Question 10?");

            return questions;
        }



    // Method for starting the game
    private void startGame() {
        // Create a new game instance and start the game
        Game game = new Game(allQuestionsGame, clientHandlers, ROUND_TIME);
        game.start();
    }

    public void run() {
        try {
            while (true) {
                // Accept a new connection from a player
                Socket socket = serverSocket.accept();
                System.out.println("Player connected: " + socket.getInetAddress());

                // Create a new client handler for the player
                ClientHandler clientHandler = new ClientHandler(socket, this);
                clientHandlers.add(clientHandler);

                // Start a new thread for the client handler
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            //would error if the server is closed or the port is already in use
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
        if (readyPlayers.size() >= MIN_PLAYERS) {
            //if the everyone is ready, start the game
            if (readyPlayers.size() == clientHandlers.size()) {
                startGame();
            }
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

    public Game getGame() {
        return game;
    }

    public static void main(String[] args) {
        try {
            // Create a new server instance and start it
            GameServer gameServer = new GameServer(8000);
            gameServer.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
