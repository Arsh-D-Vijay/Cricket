package com.tekion.cricket.services;

import com.tekion.cricket.models.Team;
import com.tekion.cricket.repository.TeamsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    @Autowired
    private TeamsRepo teamsRepo;

    public ResponseEntity<Object> getAllTeams(){
        try {
            return ResponseEntity.ok(teamsRepo.findAll());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("NOT ABLE TO FETCH DATA");
        }
    }

    public ResponseEntity<Object> getByID(int id){
        if(teamsRepo.findById(id).isPresent()){
            return ResponseEntity.ok(teamsRepo.findById(id).get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    public ResponseEntity<Object> getTeamByID(int id){
        Team team;
        if (teamsRepo.findById(id).isPresent()) {
            team = teamsRepo.findById(id).get();
        } else {
            return ResponseEntity.badRequest().body("TEAM NOT FOUND");
        }
        return ResponseEntity.ok(team);
    }

    public ResponseEntity<Object> updateTeam(Team team){
        try{
            teamsRepo.save(team);
            return ResponseEntity.accepted().body("TEAM ADDED !!");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("ILLEGAL INPUTS");
        }
    }


}
