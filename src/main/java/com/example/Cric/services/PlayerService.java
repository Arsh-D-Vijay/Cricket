package com.example.Cric.services;

import com.example.Cric.models.Player;
import com.example.Cric.repository.PlayersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PlayerService {
    @Autowired
    private PlayersRepo repo;

    public List<Player> getAllPlayers(){
        return repo.findAll();
    }

    public void addPlayer(Player player){
        repo.save(player);
    }

    public Player getPlayerByID(int id){
        return repo.findById(id).get();
    }

    public boolean deletePlayerByID(int id){
        try {
            repo.deleteById(id);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void updatePlayerPerformance(int id){
        Player player = repo.findById(id).get();
        repo.save(player);
    }
    public int playNextBall(int id) {
        double number = Math.random();
        Player player = repo.findById(id).get();
        int run  = 0;
        if (player.getPlayingStyle() == 1) {
            // Probability Distribution for Bowler
            // 0 --> 15%  , 1 --> 35%, 2--> 25%, 3 --> 5%, 4 --> 5%, 5 --> 5%, 6 -->5%, W --> 10%
            if (number < 0.15) {
                run = 0;
            } else if (number < 0.45) {
                run =  1;
            } else if (number < 0.7) {
                run = 2;
            } else if (number < 0.75) {
                run = 3;
            } else if (number < 0.8) {
                run = 4;
            } else if (number < 0.85) {
                run = 5;
            } else if (number < 0.9) {
                run =  6;
            } else {
                return 7;
            }
        } else {
            // Probability Distribution for Batsman and All-Rounder
            // 0 --> 5%  , 1 --> 20%, 2--> 15%, 3 --> 10%, 4 --> 20%, 5 --> 5%, 6 -->20%, W --> 5%
            if (number < 0.05) {
                run = 0;
            } else if (number < 0.25) {
                run =  1;
            } else if (number < 0.4) {
                run =  2;
            } else if (number < 0.5) {
                run =  3;
            } else if (number < 0.7) {
                run = 4;
            } else if (number < 0.75) {
                run = 5;
            } else if (number < 0.95) {
                run = 6;
            } else {
                return 7;
            }
        }
        player.addTotalRunsScored(run);
        repo.save(player);
        return run;
    }

}
