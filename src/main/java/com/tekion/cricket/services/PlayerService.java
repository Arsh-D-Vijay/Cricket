package com.tekion.cricket.services;

import com.tekion.cricket.models.Player;
import com.tekion.cricket.repository.PlayersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    @Autowired
    private PlayersRepo playersRepo;

    public ResponseEntity<Object> getAllPlayers() {
        try {
            return ResponseEntity.ok(playersRepo.findAll());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("NOT ABLE TO FETCH DATA");
        }
    }

    public ResponseEntity<Object> addOrUpdatePlayer(Player player) {
        try{
            playersRepo.save(player);
            return ResponseEntity.accepted().body("PLAYER ADDED !!");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("ILLEGAL INPUTS");
        }
    }

    public ResponseEntity<Object> getPlayerByID(String id) {
        if(playersRepo.findById(id).isPresent()){
            return ResponseEntity.ok(playersRepo.findById(id).get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    public int playNextBall(String id) {
        double number = Math.random();
        Player player = playersRepo.findById(id).get();
        int run = 0;
        if (player.getPlayingStyle() == 1) {
            // Probability Distribution for Bowler
            // 0 --> 15%  , 1 --> 35%, 2--> 25%, 3 --> 5%, 4 --> 5%, 5 --> 5%, 6 -->5%, W --> 10%
            if (number < 0.15) {
                run = 0;
            } else if (number < 0.45) {
                run = 1;
            } else if (number < 0.7) {
                run = 2;
            } else if (number < 0.75) {
                run = 3;
            } else if (number < 0.8) {
                run = 4;
            } else if (number < 0.85) {
                run = 5;
            } else if (number < 0.9) {
                run = 6;
            } else {
                return -1;
            }
        } else {
            // Probability Distribution for Batsman and All-Rounder
            // 0 --> 5%  , 1 --> 20%, 2--> 23%, 3 --> 10%, 4 --> 20%, 5 --> 5%, 6 -->12%, W --> 5%
            if (number < 0.05) {
                run = 0;
            } else if (number < 0.25) {
                run = 1;
            } else if (number < 0.48) {
                run = 2;
            } else if (number < 0.58) {
                run = 3;
            } else if (number < 0.78) {
                run = 4;
            } else if (number < 0.83) {
                run = 5;
            } else if (number < 0.95) {
                run = 6;
            } else {
                return -1;
            }
        }
        player.addTotalRunsScored(run);
        playersRepo.save(player);
        return run;
    }
}
