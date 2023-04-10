package com.tekion.cricket.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Inning")
@NoArgsConstructor
public class Inning {
    @Id
    @Indexed(unique = true)
    private String inningID;
    private String gameID;
    private String battingTeamID;
    private int ballsPlayed;
    private int runs;
    private int wickets;
    private String strikerID;
    private String nonStrikerID;
    private int maxBalls;
    private int totalPlayers;
    private Integer target;

    public Inning(String gameID, String battingTeamID, int maxBalls, int totalPlayers){
        this.gameID = gameID;
        this.battingTeamID = battingTeamID;
        this.wickets = 0;
        this.runs = 0;
        this.ballsPlayed = 0;
        this.maxBalls = maxBalls;
        this.totalPlayers = totalPlayers;

    }

}
