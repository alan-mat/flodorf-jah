package com.floridsdorf.jah.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient implements Runnable{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public GameClient(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        String input;
        try {
            while ((input = in.readLine()) != null) {
                System.out.println(input);
            }
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public static void main(String[] args) {
        try {
            new GameClient("127.0.0.1", 7777);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
