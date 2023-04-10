package com.tekion.cricket.api;


import com.tekion.cricket.models.Game;
import com.tekion.cricket.services.BallOutcomeService;
import com.tekion.cricket.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;
    @Autowired
    private BallOutcomeService ballsOutcomeService;

    //    ALL GAMES
    @GetMapping("/all")
    public ResponseEntity<Object> getAllGames() {
        return gameService.getAllGames();
    }

    // GAME BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getMatchByID(@PathVariable("id") String id) {
        return gameService.getGameByID(id);
    }

    // ADD GAME
    @PostMapping
    public ResponseEntity<Object> addMatch(@RequestBody Game game) {
        return gameService.initializeNewGame(game);
    }

    // PLAY 'N' BALLS
    @PutMapping(path = "/{id}/{balls}")
    public ResponseEntity<Object> playMatch(@PathVariable("id") String id, @PathVariable("balls") int balls) {
        return gameService.playInnings(id, balls);
    }

    // AUTOPLAY
    @PutMapping(path = "/{id}/Autoplay")
    public ResponseEntity<Object> playMatch(@PathVariable("id") String id) {
        return gameService.Autoplay(id);
    }

    // SERIES GAMES
    @PostMapping(path = "/series/{noOfMatches}")
    public ResponseEntity<Object> seriesInitiator(@PathVariable("noOfMatches") int noOfMatches, @RequestBody Game game) {
        gameService.seriesGames(game, noOfMatches);
        return ResponseEntity.ok("Series Initiated");
    }

}


