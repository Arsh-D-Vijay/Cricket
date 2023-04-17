package com.tekion.cricket.api;

import com.tekion.cricket.models.Tournament;
import com.tekion.cricket.services.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tournament")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return tournamentService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getByID(@PathVariable("id") int id) {
        return tournamentService.getTournamentByID(id);
    }

    @PostMapping
    public ResponseEntity<Object> startEliminationTournament(@RequestBody Tournament tournament) {
        return tournamentService.addTournament(tournament);

    }

    @PutMapping("/{id}/play")
    public ResponseEntity<Object> playTournament(@PathVariable("id") int id) {
        return tournamentService.tournamentPlay(id);

    }

    @PutMapping("/{id}/asyncPlay")
    public ResponseEntity<Object> asyncGame(@PathVariable("id") int id) {
        return tournamentService.tournamentAsyncPlay(id);
    }

}
