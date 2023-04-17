package com.tekion.cricket.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tournamentID;

    @ElementCollection
    @CollectionTable(name = "tournamentTeamsList", joinColumns = {@JoinColumn(name = "tournamentID")})
    @Column(name = "teams_id")
    private List<Integer> teamsIDsList;

    @ElementCollection
    @CollectionTable(name = "tournamentTeamsLeftToPlayList", joinColumns = {@JoinColumn(name = "tournamentID")})
    @Column(name = "teams_left_to_play_id")
    private List<Integer> teamLeftToPlay;

    private int winnerID;

    @ElementCollection
    @CollectionTable(name= "tournamentMatchesList", joinColumns = {@JoinColumn(name="tournamentID")})
    @Column(name = "matches_id")
    private List<Integer> matchesID;

    private int maxBalls;
    private int totalPlayers;

    public Tournament(List<Integer> teamsIDsList, int maxBalls, int totalPlayers) {
        this.maxBalls = maxBalls;
        this.totalPlayers = totalPlayers;
        this.teamsIDsList = teamsIDsList;
    }

    public Tournament() {
    }
}
