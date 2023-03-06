package com.example.Cric.models;

import jakarta.persistence.*;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameID;
    private boolean firstInningPlayed;
    private boolean SecondInningPlayed;
    private int teamAID;
    private int teamBID;
    private int WinnerID;
    private int teamARuns;
    private int teamBRuns;
    private int maxBalls;
    private int ballsPlayed;
    private int teamAWickets;
    //    @Column(name = "teamA_Wickets")
    private int teamBWickets;

    @Override
    public String toString() {
        return "Game{" + "gameID=" + gameID + ", firstInningPlayed=" + firstInningPlayed + ", SecondInningPlayed=" +
               SecondInningPlayed + ", teamAID=" + teamAID + ", teamBID=" + teamBID + ", WinnerID=" + WinnerID +
               ", teamARuns=" + teamARuns + ", teamBRuns=" + teamBRuns + ", maxBalls=" + maxBalls + ", ballsPlayed=" +
               ballsPlayed + ", teamAWickets=" + teamAWickets + ", teamBWickets=" + teamBWickets + ", totalPlayers=" +
               totalPlayers + ", strikerID=" + strikerID + '}';
    }

    private int totalPlayers;
    private int strikerID;

    public int getStrikerID() {
        return strikerID;
    }

    public void setStrikerID(int strikerID) {
        this.strikerID = strikerID;
    }

    public Game() {
    }

    public Game(int teamAID, int teamBID, int maxBalls, int totalPlayers) {
        this.firstInningPlayed = false;
        this.SecondInningPlayed = false;
        this.teamAID = teamAID;
        this.teamBID = teamBID;
        //        WinnerID = winnerID;
        this.teamARuns = 0;
        this.teamBRuns = 0;
        this.maxBalls = maxBalls;
        this.ballsPlayed = 0;
        this.teamAWickets = 0;
        this.teamBWickets = 0;
        this.totalPlayers = totalPlayers;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public boolean isFirstInningPlayed() {
        return firstInningPlayed;
    }

    public void setFirstInningPlayed(boolean firstInningPlayed) {
        this.firstInningPlayed = firstInningPlayed;
    }

    public boolean isSecondInningPlayed() {
        return SecondInningPlayed;
    }

    public void setSecondInningPlayed(boolean secondInningPlayed) {
        SecondInningPlayed = secondInningPlayed;
    }

    public int getTeamAID() {
        return teamAID;
    }

    public void setTeamAID(int teamAID) {
        this.teamAID = teamAID;
    }

    public int getTeamBID() {
        return teamBID;
    }

    public void setTeamBID(int teamBID) {
        this.teamBID = teamBID;
    }

    public int getWinnerID() {
        return WinnerID;
    }

    public void setWinnerID(int winnerID) {
        WinnerID = winnerID;
    }

    public int getTeamARuns() {
        return teamARuns;
    }

    public void setTeamARuns(int teamARuns) {
        this.teamARuns = teamARuns;
    }

    public int getTeamBRuns() {
        return teamBRuns;
    }

    public void setTeamBRuns(int teamBRuns) {
        this.teamBRuns = teamBRuns;
    }

    public int getMaxBalls() {
        return maxBalls;
    }

    public void setMaxBalls(int maxBalls) {
        this.maxBalls = maxBalls;
    }

    public int getBallsPlayed() {
        return ballsPlayed;
    }

    public void setBallsPlayed(int ballsPlayed) {
        this.ballsPlayed = ballsPlayed;
    }

    public int getTeamAWickets() {
        return teamAWickets;
    }

    public void setTeamAWickets(int teamAWickets) {
        this.teamAWickets = teamAWickets;
    }

    public int getTeamBWickets() {
        return teamBWickets;
    }

    public void setTeamBWickets(int teamBWickets) {
        this.teamBWickets = teamBWickets;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }


}
