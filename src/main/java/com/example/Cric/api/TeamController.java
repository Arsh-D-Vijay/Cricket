package com.example.Cric.api;

import com.example.Cric.models.Team;
import com.example.Cric.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @GetMapping("/allTeams")
    public List<Team> getAllTeams(){
        return teamService.getAllTeams();
    }

    @PostMapping
    public void addTeam(@RequestBody Team team){
        teamService.updateTeam(team);
    }

    @GetMapping(path = "/&{id}")
    public  Team getTeamByID(@PathVariable("id") int id){
        return teamService.getTeamsByID(id);
    }

    @RequestMapping(value = "/&{id}",method = RequestMethod.DELETE)
    public void deleteTeamByID(@PathVariable("id") int id){
        teamService.deleteTeamByID(id);
    }

    //    @PutMapping(value = "/{teamid}&playerid={id}")
    //    public void addPlayerToTeam(@PathVariable("teamid") int teamid,@PathVariable("id") int id){
    //        teamService.addPlayerToTeam(teamid,id);
    //    }

}
