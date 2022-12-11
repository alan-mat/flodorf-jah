package com.floridsdorf.jah.view;

import com.floridsdorf.jah.controller.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PrototypeView {

    private Controller controller;

    public PrototypeView(Controller controller){
        this.controller = controller;
    }

    public void start(){
        Scanner sc = new Scanner(System.in);
        setupPhase(sc);
        System.out.println("Game start!");

        do {
            String prompt = controller.getPrompt();
            System.out.println(prompt);
            getAnswers(sc);
            rateAnswers(sc, prompt);
        }while (!controller.endRound());

        sc.close();
    }

    public void printText(String text){
        System.out.println(text);
    }

    private void setupPhase(Scanner sc){
        System.out.print("How many players? ");
        int playerCount = sc.nextInt();
        sc.nextLine();  //flush one line
        for(int i = 0; i < playerCount; i++){
            System.out.printf("Enter name of player %d: ", i+1);
            String name = sc.nextLine();
            controller.addPlayer(name);
        }
    }

    private void getAnswers(Scanner sc){
        List<String> players = controller.getPlayers();
        for(int i = 0; i < players.size(); i++){
            System.out.printf("%s's answer: ", players.get(i));
            String answer = sc.nextLine();
            controller.addAnswer(players.get(i), answer);
        }
    }

    private void rateAnswers(Scanner sc, String prompt){
        List<String> answers = controller.getAnswers();
        List<String> players = controller.getPlayers();
        Map<String, Integer> votes = new HashMap<>();
        for(String answer : answers){
            votes.put(answer, 0);
        }
        for(String p : players){
            System.out.println(System.lineSeparator()+prompt);
            System.out.printf("%s: pick the funniest answer to the prompt!%n", p);
            for(int i = 0; i < answers.size(); i++){
                System.out.printf("%d. %s%n", i+1, answers.get(i));
            }
            int selected;
            do {
                System.out.print("Selected answer: ");
                selected = sc.nextInt();
            }while (selected > answers.size() || selected < 1);
            sc.nextLine();
            String sa = answers.get(selected-1);
            votes.put(sa, votes.get(sa)+1);
        }
        String out = controller.addPoint(votes);
        System.out.println(out);
    }

}
