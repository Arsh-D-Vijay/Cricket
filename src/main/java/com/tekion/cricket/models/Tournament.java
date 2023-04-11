package com.tekion.cricket.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("Tournaments")
@Data
public class Tournament {

    @Id
    @Indexed(unique = true)
    private String tournamentID;
    private List<String> teamsIDsList;
    private List<String> teamLeftToPlay;
    private String winnerID;
    private List<String> matchesID;
    private int maxBalls;
    private int totalPlayers;

    public Tournament() {
    }

    public Tournament(List<String> teamsIDsList, int maxBalls, int totalPlayers) {
        this.teamsIDsList = teamsIDsList;
        this.maxBalls = maxBalls;
        this.totalPlayers = totalPlayers;
    }

    @Override
    public String toString() {
        return "Tournament{" + "tournamentID='" + tournamentID + '\'' + ", teamsIDsList=" + teamsIDsList +
               ", teamLeftToPlay=" + teamLeftToPlay + ", winnerID='" + winnerID + '\'' + ", matchesID=" + matchesID +
               ", maxBalls=" + maxBalls + ", totalPlayers=" + totalPlayers + '}';
    }
}
