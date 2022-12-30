package com.floridsdorf.jah.model;

import java.util.*;
import java.util.concurrent.*;

public class Game {
    // List of game questions
    private List<String> questions;

    // List of client handlers for the connected players
    private List<ClientHandler> clientHandlers;

    // Time in seconds for each round
    private int roundTime;

    // Current round number
    private int round;

    // Map of answers received from the players
    private Map<String, String> answers = new HashMap<>();
    // Map of votes received from the players
    private Map<String, Integer> votes = new HashMap<>();
    // Map of player scores
    private Map<String, Integer> scores = new HashMap<>();

    public Game(List<String> questions, List<ClientHandler> clientHandlers, int roundTime) {
        this.questions = questions;
        this.clientHandlers = clientHandlers;
        this.roundTime = roundTime;
    }

    // Method for starting the game
    public void start() {
        // Start the game in a new thread
        new Thread(() -> {
            try {
                // Loop through all the game questions
                for (round = 0; round < questions.size(); round++) {
                    // Send the current question to all players
                    String question = questions.get(round);
                    broadcastMessage("question " + question);

                    // Wait for the specified time for players to answer the question
                    TimeUnit.SECONDS.sleep(roundTime);

                    // Send the received answers to all players
                    broadcastAnswers();

                    // Wait for the specified time for players to vote for the funniest answer
                    TimeUnit.SECONDS.sleep(roundTime);

                    // Calculate the player scores based on the received votes
                    calculateScores();
                }
                // Send the final scores to all players
                broadcastScores();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Method for broadcasting a message to all players
    private void broadcastMessage(String message) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMessage(message);
        }
    }

    // Method for sending the received answers to all players
    private void broadcastAnswers() {
        StringBuilder message = new StringBuilder();
        message.append("answers");
        for (Map.Entry<String, String> entry : answers.entrySet()) {
            message.append(" ");
            message.append(entry.getValue());
        }
        broadcastMessage(message.toString());
    }

    // Method for calculating the player scores based on the received votes
    private void calculateScores() {
        // Sort the answers by the number of votes in descending order
        List<Map.Entry<String, Integer>> sortedAnswers = new ArrayList<>(votes.entrySet());
        Collections.sort(sortedAnswers, (a, b) -> b.getValue() - a.getValue());

        // Give 5 points to the player with the funniest answer, 3 points to the second funniest, and 1 point to the third funniest
        int i = 0;
        for (Map.Entry<String, Integer> entry : sortedAnswers) {
            int points = 0;
            if (i == 0) points = 5;
            if (i == 1) points = 3;
            if (i == 2) points = 1;

            String playerName = entry.getKey();
            if (!scores.containsKey(playerName)) scores.put(playerName, 0);
            scores.put(playerName, scores.get(playerName) + points);

            i++;
        }

        // Clear the answers and votes for the next round
        answers.clear();
        votes.clear();
    }

    // Method for sending the final scores to all players
    private void broadcastScores() {
        StringBuilder message = new StringBuilder();
        message.append("scores");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            message.append(" ");
            message.append(entry.getKey());
            message.append(" ");
            message.append(entry.getValue());
        }
        broadcastMessage(message.toString());
    }

    // Method for receiving an answer from a player
    public void receiveAnswer(String playerName, String answer) {
        answers.put(playerName, answer);
    }

    // Method for receiving a vote from a player
    public void receiveVote(String playerName, int answerIndex) {
        // Get the player that wrote the answer at the specified index
        String votedPlayer = new ArrayList<>(answers.keySet()).get(answerIndex);

        if (!votes.containsKey(votedPlayer)) votes.put(votedPlayer, 0);
        votes.put(votedPlayer, votes.get(votedPlayer) + 1);
    }
}


