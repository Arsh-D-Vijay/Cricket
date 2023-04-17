package com.tekion.cricket.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameID;
    private boolean firstInningPlayed;
    private boolean SecondInningPlayed;
    private int teamAID;
    private int teamBID;
    private Integer WinnerID;
    private int teamARuns;
    private int teamBRuns;
    private int maxBalls;
    private int ballsPlayed;
    private int teamAWickets;
    private int teamBWickets;
    private int totalPlayers;
    private int strikerID;

    public Game() {
    }

    public Game(int teamAID, int teamBID, int maxBalls, int totalPlayers) {
        this.firstInningPlayed = false;
        this.SecondInningPlayed = false;
        this.teamAID = teamAID;
        this.teamBID = teamBID;
        this.teamARuns = 0;
        this.teamBRuns = 0;
        this.maxBalls = maxBalls;
        this.ballsPlayed = 0;
        this.teamAWickets = 0;
        this.teamBWickets = 0;
        this.totalPlayers = totalPlayers;
    }
    @Override
    public String toString() {
        return "Game{" + "gameID=" + gameID + ", firstInningPlayed=" + firstInningPlayed + ", SecondInningPlayed=" +
               SecondInningPlayed + ", teamAID=" + teamAID + ", teamBID=" + teamBID + ", WinnerID=" + WinnerID +
               ", teamARuns=" + teamARuns + ", teamBRuns=" + teamBRuns + ", maxBalls=" + maxBalls + ", ballsPlayed=" +
               ballsPlayed + ", teamAWickets=" + teamAWickets + ", teamBWickets=" + teamBWickets + ", totalPlayers=" +
               totalPlayers + ", strikerID=" + strikerID + '}';
    }
}