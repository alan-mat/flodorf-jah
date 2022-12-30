package com.floridsdorf.jah.model;

/**
 * Player Instance for every logical player
 */
public class Player {

    private String userName;
    private int points;
    private ClientHandler netHandler;

    public Player(String userName, ClientHandler clientHandler){
        setUserName(userName);
        points = 0;
        this.netHandler = clientHandler;
    }

    public void addPoints(int amount){ points += amount; }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPoints() {
        return points;
    }

    public ClientHandler getClientHandler(){return netHandler;}

}
