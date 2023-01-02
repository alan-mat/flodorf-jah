package com.floridsdorf.jah.model;

import org.json.*;

import java.io.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Server-side
 */
public class GameHandler implements Runnable{

    private GameServer server;
    private List<String> prompts;
    private Map<ClientHandler, String> answers;
    private Map<ClientHandler, Integer> votes;
    private boolean gameOver = false;
    private static final String promptsJSONPath = "com/floridsdorf/jah/prompts_from_cah.json";

    public GameHandler(GameServer server){
        this.server = server;
        answers = new LinkedHashMap<>();
        votes = new LinkedHashMap<>();
        prompts = JSONParser.parsePromptsJSON(promptsJSONPath);
    }

    @Override
    public void run() {
        server.broadcastMessage("%GAME_START", null);
        while(!gameOver){
            answers = new LinkedHashMap<>();    //clear answers
            votes = new LinkedHashMap<>();      //clear votes

            String prompt = getRandomPrompt();
            server.broadcastMessage(String.format("%s %s", "%NEW_PROMPT", prompt), null);
            server.broadcastMessage(String.format("%s You have %d seconds ...", "%INFO", GameServer.ROUND_TIME), null);
            threadSleep(GameServer.ROUND_TIME);
            server.broadcastMessage(String.format("%s Time is up!", "%INFO"), null);
            broadcastAnswers();

            server.broadcastMessage(String.format("%s Vote for the funniest answer now! (30 seconds)", "%INFO"), null);
            threadSleep(GameServer.ROUND_TIME);
            padVotes();
            List<Map.Entry<ClientHandler, Integer>> sortedVotes = getSortedVotesList();
            broadcastVotes(sortedVotes);

            //TODO: add points, check if someone won
            gameOver = true;
        }
        server.broadcastMessage("%GAME_OVER", null);
    }

    /**
     * TODO: change, send only answers
     */
    private void broadcastAnswers(){
        StringBuilder sb = new StringBuilder("%INFO This round's answers:");
        int i = 1;
        for(String answer : answers.values()){
            //%> : new line character
            sb.append("%>").append(String.format("%d: %s", i++, answer));
        }
        server.broadcastMessage(sb.toString(), null);
    }

    private void broadcastVotes(List<Map.Entry<ClientHandler, Integer>> sortedVotes){
        StringBuilder sb = new StringBuilder("%INFO Votes are in:");
        int i = 0;
        for(Map.Entry<ClientHandler, Integer> entry : sortedVotes){
            ClientHandler currentAnswerClient = entry.getKey();
            int currentAnswerVoteAmount = entry.getValue();
            String answer = answers.get(currentAnswerClient);
            sb.append("%>").append(String.format("%d: [%s] %s (%d)", ++i,
                    currentAnswerClient.getPlayerName(), answer, currentAnswerVoteAmount));
        }
        server.broadcastMessage(sb.toString(), null);
    }

    public void addAnswer(ClientHandler client, String answer){
        answers.put(client, answer);
    }

    /**
     * @param voteIndex vote as per answer output, 1 indexed
     */
    public void addVote(ClientHandler client, int voteIndex) {
        ClientHandler votedAnswerClient;
        try {
            votedAnswerClient = new LinkedList<>(answers.keySet()).get(voteIndex - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            client.sendMessage("%ERROR Invalid vote number!");
            return;
        }
        if (!votes.containsKey(votedAnswerClient)) votes.put(votedAnswerClient, 1);
        else votes.put(votedAnswerClient, votes.get(votedAnswerClient) + 1);
    }

    public void padVotes(){
        for(ClientHandler client : answers.keySet()){
            if(!votes.containsKey(client)) votes.put(client, 0);
        }
    }

    public String getRandomPrompt(){
        int rNum = new Random().nextInt(prompts.size());
        return prompts.remove(rNum);
    }

    private List<Map.Entry<ClientHandler, Integer>> getSortedVotesList(){
        List<Map.Entry<ClientHandler, Integer>> sortedVotes = new LinkedList<>(votes.entrySet());
        Collections.sort(sortedVotes, (a, b) -> b.getValue() - a.getValue());
        return sortedVotes;
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public void threadSleep(int seconds){
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            //TODO: some clean handling idk
            throw new RuntimeException(e);
        }
    }

    public List<String> getPrompts(){ return prompts; }

    public void setGameOver(boolean gameOver){ this.gameOver = gameOver; }

}
