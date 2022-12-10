package com.floridsdorf.jah.view;

import com.floridsdorf.jah.controller.Controller;

import java.util.List;
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

        System.out.println(controller.getPrompt());
        getAnswers(sc);
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

}
