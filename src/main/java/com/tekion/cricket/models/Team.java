package com.tekion.cricket.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Team {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teamID;
    @Column(name = "Name")
    private String teamName;
    @ElementCollection
    @Column(name = "player_id")
    @CollectionTable(name = "playersList", joinColumns = {@JoinColumn(name = "teamID")})
    private List<Integer> playersIDsList;
    private int matchesWon;
    private int matchesPlayed;

    public Team() {
    }


    @Override
    public String toString() {
        return "" + playersIDsList;
    }

    public Team(String teamName, List<Integer> playersIDsList, int matchesWon, int matchesPlayed) {
        this.teamName = teamName;
        this.playersIDsList = playersIDsList;
        System.out.println("List: " + this.playersIDsList);
        this.matchesWon = matchesWon;
        this.matchesPlayed = matchesPlayed;
    }

    public void addMatchesPlayed() {
        this.matchesPlayed ++;
    }

    public void addMatchesWon() {
        this.matchesWon ++;
    }
}
