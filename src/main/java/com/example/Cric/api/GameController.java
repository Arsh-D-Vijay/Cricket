package com.example.Cric.api;

import com.example.Cric.models.Game;
import com.example.Cric.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping("/allGames")
    public List<Game> getAllMatches(){
        return gameService.getAllGames();
    }

    @GetMapping(value = "/{id}")
    public Game getMatchByID(@PathVariable("id") int id){
        return gameService.getGameByID(id);
    }

    @PostMapping
    public void addMatch(@RequestBody Game game){
        gameService.startNewGame(game);
    }

    @PutMapping(path = "/{id}/{balls}")
    public void playMatch(@PathVariable("id") int id,@PathVariable("balls") int balls){
        System.out.println("sda");
        gameService.playBalls(id,balls);
        System.out.println(gameService.getGameByID(id).toString());
    }

}
