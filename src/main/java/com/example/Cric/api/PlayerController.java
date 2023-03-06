package com.example.Cric.api;

import com.example.Cric.models.Player;
import com.example.Cric.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @GetMapping("/allPlayers")
    public List<Player> getPlayers(){
        return playerService.getAllPlayers();
    }

    @PostMapping
    public void addPlayer(@RequestBody Player player){
        playerService.addPlayer(player);
    }

    @GetMapping(path = "/&{id}")
    public  Player getPlayerByID(@PathVariable("id") int id){
        return playerService.getPlayerByID(id);
    }

    @RequestMapping(value = "/&{id}",method = RequestMethod.DELETE)
    public void deletePlayerByID(@PathVariable("id") int id){
        playerService.deletePlayerByID(id);
    }
}
