package com.tekion.cricket.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class BallOutcome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ballID;
    private int gameID;
    private int ballNumber;
    private int teamID;
    private int strikerID;
    private int ballOutcome;

    public BallOutcome() {
    }

    public BallOutcome(int gameID, int teamID, int ballNumber, int strikerID, int ballOutcome) {
        this.teamID = teamID;
        this.gameID = gameID;
        this.ballNumber = ballNumber;
        this.strikerID = strikerID;
        this.ballOutcome = ballOutcome;
    }
}
