package com.tekion.cricket.api;

import com.tekion.cricket.models.Player;
import com.tekion.cricket.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/all")
    public ResponseEntity<Object> getPlayers() {
        return playerService.getAllPlayers();
    }

    @PostMapping
    public ResponseEntity<Object> addPlayer(@RequestBody Player player) {
        return playerService.addOrUpdatePlayer(player);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getPlayerByID(@PathVariable("id") String id) {
        return playerService.getPlayerByID(id);
    }

}