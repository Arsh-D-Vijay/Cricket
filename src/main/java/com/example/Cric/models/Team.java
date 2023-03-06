package com.example.Cric.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Team {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teamID;
    @Column(name = "Name")
    private String teamName;
    //    @ElementCollection
    @ElementCollection
    @Column(name = "player_id")
    @CollectionTable(name = "playersList", joinColumns = {@JoinColumn(name = "teamID")})
    private List<Integer> playersIDsList;
    //    @Transient
    //    private List<Integer> bowlersID;

    private int matchesWon;
    private int matchesPlayed;

    public Team() {
    }

    //    public String ListtoString(List<Integer> myIntList) throws JsonProcessingException {
    //        ObjectMapper mapper = new ObjectMapper();
    //        try {
    //            return mapper.writeValueAsString(myIntList);
    //        } catch (JsonProcessingException e) {
    //            throw new RuntimeException(e);
    //        }
    //    }
    //    public  List<Integer> StringToList(String myString) throws JsonProcessingException {
    //        ObjectMapper mapper = new ObjectMapper();
    //        try {
    //            return mapper.readValue(myString, new TypeReference<List<Integer>>(){});
    //        } catch (JsonProcessingException e) {
    //            throw new RuntimeException(e);
    //        }
    //    }

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

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<Integer> getPlayersIDsList() {
        return playersIDsList;
    }

    public void setPlayersIDsList(List<Integer> playersIDsList) {
        this.playersIDsList = playersIDsList;
    }

    public int getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(){
        this.matchesWon++;
    }
    public void setMatchesWon(int matchesWon) {
        this.matchesWon = matchesWon;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }
    public void setMatchesPlayed(){this.matchesPlayed++;}
}
