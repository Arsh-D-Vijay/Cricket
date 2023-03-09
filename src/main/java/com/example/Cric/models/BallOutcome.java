package com.example.Cric.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BallOutcome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ballID;

    public int getBallID() {
        return ballID;
    }

    public void setBallID(int ballID) {
        this.ballID = ballID;
    }

    private int gameID;
    private int ballNumber;
    private int teamID;

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    private int strikerID;
    private int ballOutcome;

    public BallOutcome() {
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getBallNumber() {
        return ballNumber;
    }

    public void setBallNumber(int ballNumber) {
        this.ballNumber = ballNumber;
    }

    public int getStrikerID() {
        return strikerID;
    }

    public void setStrikerID(int strikerID) {
        this.strikerID = strikerID;
    }

    public int getBallOutcome() {
        return ballOutcome;
    }

    public void setBallOutcome(int ballOutcome) {
        this.ballOutcome = ballOutcome;
    }

    public BallOutcome(int gameID,int teamID, int ballNumber, int strikerID, int ballOutcome) {
        this.teamID = teamID;
        this.gameID = gameID;
        this.ballNumber = ballNumber;
        this.strikerID = strikerID;
        this.ballOutcome = ballOutcome;
    }
}
