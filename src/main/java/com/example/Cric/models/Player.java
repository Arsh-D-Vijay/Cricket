package com.example.Cric.models;

import jakarta.persistence.*;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playersID")
    private int playersID;
    @Column(name = "playerName")
    private String name;
    //    private int teamID;
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

    public int getPlayersID() {
        return playersID;
    }

    public void setPlayersID(int playersID) {
        this.playersID = playersID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //    public int getTeamID() {
    //        return teamID;
    //    }

    //    public void setTeamID(int teamID) {
    //        this.teamID = teamID;
    //    }

    public int getPlayingStyle() {
        return playingStyle;
    }

    public void setPlayingStyle(int playingStyle) {
        this.playingStyle = playingStyle;
    }

    public int getTotalRunsScored() {
        return totalRunsScored;
    }

    public void addTotalRunsScored(int totalRunsScored) {
        this.totalRunsScored += totalRunsScored;
    }

    public int getTotalWicketsTaken() {
        return totalWicketsTaken;
    }

    public void addTotalWicketsTaken(int totalWicketsTaken) {
        this.totalWicketsTaken += totalWicketsTaken;
    }

    public int playNextBall() {
        double number = Math.random();
        int run  = 0;
        if (this.playingStyle == 1) {
            // Probability Distribution for Bowler
            // 0 --> 15%  , 1 --> 35%, 2--> 25%, 3 --> 5%, 4 --> 5%, 5 --> 5%, 6 -->5%, W --> 10%
            if (number < 0.15) {
                run = 0;
            } else if (number < 0.45) {
                run =  1;
            } else if (number < 0.7) {
                run = 2;
            } else if (number < 0.75) {
                run = 3;
            } else if (number < 0.8) {
                run = 4;
            } else if (number < 0.85) {
                run = 5;
            } else if (number < 0.9) {
                run =  6;
            } else {
                return 7;
            }
        } else {
            // Probability Distribution for Batsman and All-Rounder
            // 0 --> 5%  , 1 --> 20%, 2--> 15%, 3 --> 10%, 4 --> 20%, 5 --> 5%, 6 -->20%, W --> 5%
            if (number < 0.05) {
                run = 0;
            } else if (number < 0.25) {
                run =  1;
            } else if (number < 0.4) {
                run =  2;
            } else if (number < 0.5) {
                run =  3;
            } else if (number < 0.7) {
                run = 4;
            } else if (number < 0.75) {
                run = 5;
            } else if (number < 0.95) {
                run = 6;
            } else {
                return 7;
            }
        }
        this.addTotalRunsScored(run);

        return run;
    }
}
