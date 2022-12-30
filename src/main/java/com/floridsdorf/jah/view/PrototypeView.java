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

        setup(sc);
        while (true){
            String input = sc.nextLine();
            controller.getGameClient().sendMessage(input);
            if(input.equals("disconnect"))
                break;
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
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        controller.getGameClient().sendMessage(name);
    }

    private void hostGame(Scanner sc){
        System.out.println("Hosting game ...");
        try {
            controller.hostGame();
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
            controller.joinGame(ip, Controller.SERVER_PORT);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println("Couldn't connect to host!");
            setup(sc);
        }
    }

    public void printText(String text){
        System.out.println(text);
    }

}
