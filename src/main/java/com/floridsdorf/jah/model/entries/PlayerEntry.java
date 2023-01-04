package com.floridsdorf.jah.model.entries;

import java.io.Serializable;

public class PlayerEntry implements Serializable {
    public String playerName;
    public int points;

    public PlayerEntry(String playerName, int points){
        this.playerName = playerName;
        this.points = points;
    }
}
