package com.floridsdorf.jah.networkTest;
//copy everything from my last Server class here
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 1234;
    private static final int MAX_CLIENTS = 10;
    private static final int MAX_QUESTIONS = 10;
    //1 minutes in seconds
    private static final int WAITING_TIME = 60;
    private static final int MAX_POINTS = 10;
    private static final int SECOND_MAX_POINTS = 5;
    private static final int THIRD_MAX_POINTS = 3;
    private static final int FOURTH_MAX_POINTS = 1;

    //the jokes will be read through json later
    private static final String[] QUESTIONS = {"Tell me something funny", "Tell me something sad", "Tell me something that you like", "Tell me something that you don't like"};

    private static final ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENTS);

    private static class Handler implements Runnable {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public boolean checkifwon() {
            for (int i = 0; i < MAX_CLIENTS; i++) {
                if (points[i] == MAX_POINTS) {
                    return true;
                }
            }
            return false;
        }

        private int[] points = new int[MAX_CLIENTS];

        //ask the question to all clients
        public void askquestion(int questionnumber) {
            for (int i = 0; i < MAX_CLIENTS; i++) {
                out.println(QUESTIONS[questionnumber]);
            }
        }

        //get best answer for clients and give points
        public void getbestanswer() {
            int[] bestanswer = new int[MAX_CLIENTS];
            for (int i = 0; i < MAX_CLIENTS; i++) {
                bestanswer[i] = 0;
            }
            for (int i = 0; i < MAX_CLIENTS; i++) {
                for (int j = 0; j < MAX_CLIENTS; j++) {
                    if (answers[i] == answers[j]) {
                        bestanswer[i]++;
                    }
                }
            }
            for (int i = 0; i < MAX_CLIENTS; i++) {
                if (bestanswer[i] == 1) {
                    points[i] += MAX_POINTS;
                } else if (bestanswer[i] == 2) {
                    points[i] += SECOND_MAX_POINTS;
                } else if (bestanswer[i] == 3) {
                    points[i] += THIRD_MAX_POINTS;
                } else if (bestanswer[i] == 4) {
                    points[i] += FOURTH_MAX_POINTS;
                }
            }
        }
        //now the rest from my last Server class
        private String[] answers = new String[MAX_CLIENTS];

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("SUBMITNAME");
                name = in.readLine();
                out.println("NAMEACCEPTED");
                for (int i = 0; i < MAX_QUESTIONS; i++) {
                    askquestion(i);
                    for (int j = 0; j < MAX_CLIENTS; j++) {
                        answers[j] = in.readLine();
                    }
                    getbestanswer();
                    if (checkifwon()) {
                        out.println("WINNER");
                        break;
                    }
                }
                out.println("GAMEOVER");
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("The server is running...");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        } finally {
            listener.close();
        }
    }
}

