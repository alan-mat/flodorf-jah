package com.floridsdorf.jah.networkTest;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Client {
    private String serverAddress;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    private void connectToServer() throws IOException {
        System.out.println("Attempting connection");
        socket = new Socket(serverAddress, 1234);
        System.out.println("Connected to " + socket.getInetAddress().getHostName());
    }

    private void getStreams() throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    private void processConnection() throws IOException {
        String message = "Connection successful";
        out.println(message);
        do {
            message = in.readLine();
            System.out.println(message);
        } while (!message.equals("SERVER>>> TERMINATE"));
    }

    private void closeConnection() {
        System.out.println("Closing connection");
        try {
            out.println("CLIENT>>> TERMINATE");
            socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void sendMessages() throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String message = "";
        do {
            message = input.readLine();
            out.println("CLIENT>>> " + message);
        } while (!message.equals("TERMINATE"));
    }

    public void run() throws IOException {
        while (true) {
            try {
                connectToServer();
                getStreams();
                processConnection();
            } catch (EOFException eofException) {
                System.out.println("Client terminated connection");
            } finally {
                closeConnection();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost");
        client.run();
    }
}