package com.tekion.cricket.models;

//import jakarta.persistence.*;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Players")
public class Player {

    @Id
    @Indexed(unique = true)
    private String playersID;
    private String name;
    private int playingStyle;
    private int totalRunsScored;
    private int totalWicketsTaken;

    public Player() {
    }

    public Player(String name, int playingStyle) {
        this.name = name;
        this.playingStyle = playingStyle;
        this.totalRunsScored = 0;
        this.totalWicketsTaken = 0;
    }

    public void addTotalRunsScored(int run) {
        this.totalRunsScored+= run;
    }
}
