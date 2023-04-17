package com.tekion.cricket.models;


import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playersID")
    private int playersID;
    @Column(name = "playerName")
    private String name;
    @Column(name = "playingStyle")
    private int playingStyle;
    @Column(name = "totalRunsScored")
    private int totalRunsScored;
    @Column(name = "totalWicketsTaken")
    private int totalWicketsTaken;

    public Player() {
    }

    public Player(String name, int playingStyle) {
        //        this.playersID = playerID;
        this.name = name;
        //        this.teamID = teamID;
        this.playingStyle = playingStyle;
        this.totalRunsScored = 0;
        this.totalWicketsTaken = 0;
    }

    public void addTotalRunsScored(int run) {
        this.totalRunsScored+= run;
    }
}