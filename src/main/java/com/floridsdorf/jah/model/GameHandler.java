package com.floridsdorf.jah.model;

import org.json.*;

import java.io.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GameHandler {

    private List<String> prompts;
    private List<Player> players;
    private Map<String, String> currentRoundAnswers;

    private static final String promptsJSONPath = "com/floridsdorf/jah/prompts_from_cah.json";
    private static final int pointsRequired = 3;

    public GameHandler(){
        prompts = new ArrayList<>();
        players = new ArrayList<>();
        currentRoundAnswers = new HashMap<>();
        prompts = JSONParser.parsePromptsJSON(promptsJSONPath);
    }

    public List<Player> endRound(){
        currentRoundAnswers = new HashMap<>();
        List<Player> winningPlayers = new ArrayList<>();
        for(Player p : players){
            if(p.getPoints() >= pointsRequired)
                winningPlayers.add(p);
        }
        if(winningPlayers.isEmpty())
            return null;
        return winningPlayers;
    }

    public String getRandomPrompt(){
        int rNum = new Random().nextInt(prompts.size());
        return prompts.remove(rNum);
    }

    public void addPlayer(String name){
        addPlayer(new Player(name));
    }

    public void addPlayer(Player p){
        players.add(p);
    }

    public void addAnswer(String userName, String answer){
        currentRoundAnswers.put(userName, answer);
    }

    public Player addPoint(Map<String, Integer> votes){
        //TODO: refactor
        //TODO: support multiple winning players
        votes = sortMapByValue(votes);
        String prompt = "";
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            prompt = entry.getKey();
        }
        for(Map.Entry<String, String> entry : currentRoundAnswers.entrySet()){
            if(entry.getValue().equals(prompt)){
                for(Player p : players){
                    if(p.getUserName().equals(entry.getKey())) {
                        p.addPoints(1);
                        return p;
                    }
                }
            }
        }
        return null;    //should never happen
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

    public List<Player> getPlayers(){ return players; }

    public List<String> getAnswers(){ return new ArrayList<>(currentRoundAnswers.values()); }

}
