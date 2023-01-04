package com.floridsdorf.jah.model;

import com.floridsdorf.jah.model.entries.PlayerEntry;
import com.floridsdorf.jah.model.entries.VoteEntry;
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

            addPoints(sortedVotes);
            server.sortClientsByPoints();
            broadcastLeaderboard();
            if(checkGameOver()){
                gameOver = true;
                broadcastWinners();
            }
        }
        server.broadcastMessage("%GAME_OVER", null);
    }

    private void broadcastAnswers(){
        List<String> answerList = new LinkedList<>(answers.values());
        server.broadcastMessage("%ANSWER_LIST", null);
        server.broadcastObject(answerList, null);
    }

    private void broadcastVotes(List<Map.Entry<ClientHandler, Integer>> sortedVotes){
        List<VoteEntry> voteEntryList = new LinkedList<>();
        for(Map.Entry<ClientHandler, Integer> entry : sortedVotes){
            ClientHandler currentAnswerClient = entry.getKey();
            int currentAnswerVoteAmount = entry.getValue();
            String answer = answers.get(currentAnswerClient);
            VoteEntry vote = new VoteEntry(currentAnswerClient.getPlayerName(), answer, currentAnswerVoteAmount);
            voteEntryList.add(vote);
        }
        server.broadcastMessage("%VOTE_LIST", null);
        server.broadcastObject(voteEntryList, null);
    }

    private void addPoints(List<Map.Entry<ClientHandler, Integer>> sortedVotes){
        int playersToReward = 3;
        int pointsToAward   = 5;
        int localVoteMax    = -1;
        for(Map.Entry<ClientHandler, Integer> entry : sortedVotes) {
            if(entry.getValue() <= 0) return;
            if(localVoteMax == -1){ //top voted answer
                localVoteMax = entry.getValue();
            }
            if(entry.getValue() < localVoteMax){
                localVoteMax = entry.getValue();
                pointsToAward -= 2;
            }
            entry.getKey().addPoints(pointsToAward);
            playersToReward--;
            if(playersToReward <= 0) return;
        }
    }

    private void broadcastLeaderboard(){
        server.broadcastMessage("%SEND_LEADERBOARD", null);
        server.broadcastObject(server.getLeaderboard(), null);
    }

    private boolean checkGameOver(){
        return server.getClientHandlers().get(0).getPoints() >= GameServer.POINTS_TO_WIN;
    }

    private void broadcastWinners(){
        int topPlayerPoints = server.getClientHandlers().get(0).getPoints();
        List<String> winners = new LinkedList<>();
        for(ClientHandler client : server.getClientHandlers()){
            if(client.getPoints() < topPlayerPoints) break;
            winners.add(client.getPlayerName());
        }
        server.broadcastMessage("%WINNER_LIST", null);
        server.broadcastObject(winners, null);
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

    public void threadSleep(int seconds){
        try {
            server.broadcastMessage(String.format("%s %d", "%TIMER_START", GameServer.ROUND_TIME), null);
            Thread.sleep(seconds * 1000L);
            server.broadcastMessage("%TIMER_STOP", null);
        } catch (InterruptedException e) {
            //TODO: some clean handling idk
            throw new RuntimeException(e);
        }
    }

    public List<String> getPrompts(){ return prompts; }

    public void setGameOver(boolean gameOver){ this.gameOver = gameOver; }

}
