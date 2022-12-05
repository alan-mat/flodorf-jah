package com.floridsdorf.jah.networkTest;

import java.net.*;
import java.io.*;
import java.util.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Map<String, Integer> players; // map of player names and scores
    private List<String> answers; // list of answers submitted by players
    private boolean gameStarted; // flag to track if the game has started
    private int currentRound; // current round of the game

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.players = new HashMap<>();
        this.answers = new ArrayList<>();
        this.gameStarted = false;
        this.currentRound = 0;
    }

    public void run() {
        try {
            // Create input and output streams for the client socket
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Send a welcome message to the client
            out.println("Welcome to the game! Type 'HELP' for a list of commands.");

            // Handle client commands
            while (true) {
                String input = in.readLine();
                if (input == null) {
                    break;
                }

                if (input.equalsIgnoreCase("HELP")) {
                    // Handle the HELP command
                } else if (input.equalsIgnoreCase("JOIN")) {
                    // Handle the JOIN command
                    handleJoin(in, out);
                } else if (input.equalsIgnoreCase("READY")) {
                    // Handle the READY command
                    handleReady(in, out);
                } else if (input.equalsIgnoreCase("ANSWER")) {
                    // Handle the ANSWER command
                    handleAnswer(in, out);
                } else if (input.equalsIgnoreCase("LEADERBOARD")) {
                    // Handle the LEADERBOARD command
                    handleLeaderboard(in, out);
                } else {
                    out.println("Unknown command. Type 'HELP' for a list of commands.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e);
            }
        }
    }

    private void handleJoin(BufferedReader in, PrintWriter out) throws IOException {
        // Read the player name from the client
        String name = in.readLine();

        // Add the player to the list of players
        players.put(name, 0);

        // Send a message to the client confirming that they joined the game
        out.println("Successfully joined the game as '"+ name + "'");
    }

    private void handleReady(BufferedReader in, PrintWriter out) throws IOException {
        // Check if the game has already started
        if (gameStarted) {
            out.println("The game has already started. Please wait for the next round to begin.");
            return;
        }

        // Check if there are enough players to start the game
        if (players.size() < 2) {
            out.println("Not enough players to start the game. Please wait for more players to join.");
            return;
        }

        // Set the gameStarted flag to true to indicate that the game has started
        gameStarted = true;

        // Send a message to all players to start the game
        broadcast("The game has started! The first round will begin shortly.");

        // Start the first round of the game
        startRound();
    }

    private void handleAnswer(BufferedReader in, PrintWriter out) throws IOException {
        // Check if the game has started
        if (!gameStarted) {
            out.println("The game has not started yet. Please wait for the game to begin.");
            return;
        }

        // Check if it is currently the time for players to submit answers
        if (currentRound <= 0) {
            out.println("It is not currently time for players to submit answers. Please wait for the next round to begin.");
            return;
        }

        // Read the player's answer from the client
        String answer = in.readLine();

        // Add the answer to the list of answers
        answers.add(answer);

        // Send a message to the client confirming that their answer was received
        out.println("Thank you for your answer. The round will end when all players have submitted their answers.");
    }

    private void handleLeaderboard(BufferedReader in, PrintWriter out) throws IOException {
        // Check if the game has started
        if (!gameStarted) {
            out.println("The game has not started yet. Please wait for the game to begin.");
            return;
        }

        // Send the current leaderboard to the client
        out.println("--- LEADERBOARD ---");
        for (Map.Entry<String, Integer> entry : players.entrySet()) {
            out.println(entry.getKey() + ": " + entry.getValue() + " points");
        }
        out.println("------------------");
    }

    private void startRound() {
        // Increment the round counter
        currentRound++;

        // Send a message to all players to start the round
        broadcast("Round " + currentRound + " has begun! Please submit your answers within 30 seconds.");

        // Start a timer to end the round after 30 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // End the round and calculate the scores
                endRound();
            }
        }, 30 * 1000);
    }

    private void endRound() {
        // Set the round counter to 0 to indicate that it is not currently the time for players to submit answers
        currentRound = 0;

        // Calculate the scores for the round
        Map<String, Integer> roundScores = calculateScores();

        // Send a message to all players with the answers and the scores for the round
        broadcast("The answers for this round were:");
        for (String answer : answers) {
            broadcast(answer);
        }
        broadcast("The scores for this round are:");
        for (Map.Entry<String, Integer> entry : roundScores.entrySet()) {
            broadcast(entry.getKey() + ": " + entry.getValue() + " points");
        }

        // Update the players' scores
        for (Map.Entry<String, Integer> entry : roundScores.entrySet()) {
            String player = entry.getKey();
            int score = entry.getValue();
            players.put(player, players.get(player) + score);
        }

        // Clear the answers for the next round
        answers.clear();

        // Check if the game is over
        if (currentRound == 10) {
            // Send a message to all players that the game is over
            broadcast("The game is over! The final scores are:");
            for (Map.Entry<String, Integer> entry : players.entrySet()) {
                broadcast(entry.getKey() + ": " + entry.getValue() + " points");
            }

            // Set the gameStarted flag to false to indicate that the game is over
            gameStarted = false;
        } else {
            // Start the next round
            startRound();
        }
    }

    private Map<String, Integer> calculateScores() {
// Create a map to store the scores for each player
        Map<String, Integer> scores = new HashMap<>();

        // Calculate the scores for each player
        for (String answer : answers) {
            // Check if the answer is correct
            if (answer.equalsIgnoreCase("correct")) {
                // Add 1 point to the player's score
                scores.put(answer, scores.getOrDefault(answer, 0) + 1);
            } else {
                // Subtract 1 point from the player's score
                scores.put(answer, scores.getOrDefault(answer, 0) - 1);
            }
        }

        return scores;
    }

    private void broadcast(String message) {
        // Send a message to all players
        for (PrintWriter out : outputs) {
            out.println(message);
        }
    }
    private List<PrintWriter> outputs = new ArrayList<>();

}