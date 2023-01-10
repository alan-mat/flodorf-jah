package com.floridsdorf.jah.view;

import com.floridsdorf.jah.controller.MainController;

import java.io.IOException;
import java.util.Scanner;

public class PrototypeView {

    private MainController mainController;

    public PrototypeView(MainController mainController){
        this.mainController = mainController;
    }

    public void start(){
        Scanner sc = new Scanner(System.in);

        setup(sc);
        while (true){
            String input = sc.nextLine();
            if(input.equals("exit") || input.equals("quit") || input.equals("disconnect")) {
                mainController.getGameClient().sendDisconnect();
                break;
            }
            if(input.startsWith("!")){
                if ("!ready".equalsIgnoreCase(input)) {
                    mainController.getGameClient().sendReady();
                    System.out.println("You are ready");
                    continue;
                }
                String command = input.split(" ")[0];
                String rem;
                try{
                    rem = input.split(" ", 2)[1];
                }catch (ArrayIndexOutOfBoundsException e){
                    rem = "";
                }
                switch (command.toLowerCase()){
                    case "!answer" -> mainController.getGameClient().sendAnswer(rem);
                    case "!vote" -> {
                        try{
                            mainController.getGameClient().sendVote(Integer.parseInt(rem));
                        }catch (NumberFormatException e){
                            System.err.println("Vote must be an Integer!");
                        }
                    }
                    default -> System.err.println("Invalid command!");
                }
                continue;
            }
            mainController.getGameClient().sendChatMsg(input);
        }

        sc.close();
    }

    public void setup(Scanner sc) {
        String input;
        do {
            System.out.println("Do you want to host or join a game? [host, join]");
            input = sc.nextLine();
        } while (!input.equals("host") && !input.equals("join"));
        if(input.equals("host"))
            hostGame(sc);
        else
            joinGame(sc);
        String name;
        do{
            System.out.print("Enter your name: ");
            name = sc.nextLine();
        }while(name.isBlank());
        mainController.getGameClient().sendPlayerInfo(name);
    }

    private void hostGame(Scanner sc){
        System.out.println("Hosting game ...");
        try {
            mainController.hostGame();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println("Couldn't host game!");
            setup(sc);
        }
    }

    private void joinGame(Scanner sc){
        System.out.print("Enter host ip: ");
        String ip = sc.nextLine();
        System.out.println("Joining game ...");
        try {
            mainController.joinGame(ip, MainController.SERVER_PORT);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println("Couldn't connect to host!");
            setup(sc);
        }
    }

    public void printText(String text){
        System.out.println(text);
    }

    public void printText(String text, boolean isError){
        if(isError) System.err.println(text);
        else System.out.println(text);
    }

}
