package com.tekion.cricket.models;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("BallOutcomes")
public class BallOutcome {
    @Id
    @Indexed(unique = true)
    private String ballID;
    private String gameID;
    private int ballNumber;
    private String teamID;
    private String strikerID;
    private int ballOutcome;
    public BallOutcome() {
    }

    public BallOutcome(String gameID,String teamID, int ballNumber, String strikerID, int ballOutcome) {
        this.teamID = teamID;
        this.gameID = gameID;
        this.ballNumber = ballNumber;
        this.strikerID = strikerID;
        this.ballOutcome = ballOutcome;
    }
}
