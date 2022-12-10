package com.floridsdorf.jah.model;

/**
 * Player Instance for every logical player
 * TODO: Networking support for players
 */
public class Player {

    private String userName;
    private int points;

    public Player(String userName){
        setUserName(userName);
        points = 0;
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

}
