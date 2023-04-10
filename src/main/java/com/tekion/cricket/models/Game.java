package com.tekion.cricket.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Game")
public class Game {

    @Id
    @Indexed(unique = true)
    private String gameID;
    private String firstInningID;
    private boolean firstInningPlayed;
    private boolean secondInningPlayed;
    private String secondInningID;
    private String teamAID;
    private String teamBID;
    private String winnerID;
    private int maxBalls;
    private int totalPlayers;


    public Game() {
    }

    public Game(String teamAID, String teamBID, int maxBalls, int totalPlayers) {
        this.secondInningPlayed = false;
        this.firstInningPlayed = false;
        this.teamAID = teamAID;
        this.teamBID = teamBID;
        this.maxBalls = maxBalls;
        this.totalPlayers = totalPlayers;
    }

}