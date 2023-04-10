package com.tekion.cricket.api;

import com.tekion.cricket.models.Team;
import com.tekion.cricket.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllTeams() {
        return teamService.getAllTeams();
    }

    @PostMapping
    public ResponseEntity<Object> addTeam(@RequestBody Team team) {
        return teamService.updateTeam(team);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getTeamByID(@PathVariable("id") String id) {
        return teamService.getByID(id);
    }


}
