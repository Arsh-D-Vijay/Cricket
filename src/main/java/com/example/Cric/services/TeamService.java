package com.example.Cric.services;

import com.example.Cric.models.Team;
import com.example.Cric.repository.TeamsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamsRepo teamsRepo;

    public List<Team> getAllTeams() {
        return teamsRepo.findAll();
    }

    public Team getTeamsByID(int id) {
        return teamsRepo.findById(id).get();
    }

    public void updateTeam(Team team) {
        teamsRepo.save(team);
    }

    public void deleteTeamByID(int id) {
        teamsRepo.deleteById(id);
    }


    public void addPlayerToTeam(int teamID, int playerID) {
        Team team = teamsRepo.findById(teamID).get();
        team.getPlayersIDsList().add(playerID);
        team.setPlayersIDsList(team.getPlayersIDsList());
    }
}
