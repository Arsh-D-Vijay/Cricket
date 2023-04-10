package com.tekion.cricket.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Teams")
@Data
public class Team {

    @Id
    @Indexed(unique = true)
    private String teamID;
    private String teamName;
    private List<String> playersIDsList;
    private int matchesWon;
    private int matchesPlayed;

    public Team() {
    }

    @Override
    public String toString() {
        return "" + playersIDsList;
    }

    public Team(String teamName, List<String> playersIDsList, int matchesWon, int matchesPlayed) {
        this.teamName = teamName;
        this.playersIDsList = playersIDsList;
        System.out.println("List: " + this.playersIDsList);
        this.matchesWon = matchesWon;
        this.matchesPlayed = matchesPlayed;
    }

    public void addMatchesWon() {
        this.matchesWon++;
    }

    public void addMatchesPlayed() {
        this.matchesPlayed++;
    }
}
