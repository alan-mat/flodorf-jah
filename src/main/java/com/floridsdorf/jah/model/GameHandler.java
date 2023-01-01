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
    private boolean gameOver = false;
    private static final String promptsJSONPath = "com/floridsdorf/jah/prompts_from_cah.json";

    public GameHandler(GameServer server){
        this.server = server;
        answers = new HashMap<>();
        prompts = JSONParser.parsePromptsJSON(promptsJSONPath);
    }

    @Override
    public void run() {
        server.broadcastMessage("%GAME_START", null);
        while(!gameOver){
            answers = new HashMap<>();  //clear answers
            String prompt = getRandomPrompt();
            server.broadcastMessage(String.format("%s %s", "%NEW_PROMPT", prompt), null);
            server.broadcastMessage(String.format("%s You have %d seconds ...", "%INFO", GameServer.ROUND_TIME), null);
            try {
                Thread.sleep(GameServer.ROUND_TIME * 1000);
            } catch (InterruptedException e) {
                //TODO: some clean handling idk
                throw new RuntimeException(e);
            }
            server.broadcastMessage(String.format("%s Time is up!", "%INFO"), null);
            broadcastAnswers();
            //TODO: rate answers
            //TODO: add points, check if someone won
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

    public void addAnswer(ClientHandler client, String answer){
        answers.put(client, answer);
    }

    public String getRandomPrompt(){
        int rNum = new Random().nextInt(prompts.size());
        return prompts.remove(rNum);
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

    public List<String> getPrompts(){ return prompts; }

    public void setGameOver(boolean gameOver){ this.gameOver = gameOver; }

}
