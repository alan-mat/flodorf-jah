package com.floridsdorf.jah.networkTest;

import java.net.*;
import java.io.*;

public class CardServer {
    public static void main(String[] args) throws IOException {
        int port = 8080; // default port
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        // Create a new server socket
        ServerSocket serverSocket = new ServerSocket(port);
        //you could add a try also here
        System.out.println("Server listening on port " + port);

        // Accept incoming connections
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket.getRemoteSocketAddress());

            // Create a new thread to handle the client
            Thread clientThread = new Thread(new ClientHandler(clientSocket));
            clientThread.start();
        }
    }
}