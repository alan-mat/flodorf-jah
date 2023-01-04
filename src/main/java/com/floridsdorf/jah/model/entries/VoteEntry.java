package com.floridsdorf.jah.model.entries;

import java.io.Serializable;

public class VoteEntry  implements Serializable {
    public String playerName;
    public String answer;
    public int voteAmount;

    public VoteEntry(String playerName, String answer, int voteAmount){
        this.playerName = playerName;
        this.answer = answer;
        this.voteAmount = voteAmount;
    }
}
