package com.tekion.cricket.services;

import com.tekion.cricket.models.Game;
import com.tekion.cricket.models.Tournament;
import com.tekion.cricket.repository.TournamentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepo tournamentRepo;
    @Autowired
    private GameService gameService;

    public ResponseEntity<Object> getAll() {
        try {
            return ResponseEntity.ok(tournamentRepo.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("NOT ABLE TO FETCH DATA");
        }
    }

    public ResponseEntity<Object> getTournamentByID(String id) {
        if (tournamentRepo.findById(id).isPresent()) {
            return ResponseEntity.ok(tournamentRepo.findById(id).get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Object> addTournament(Tournament tournament) {
        try {
            tournamentRepo.save(tournament);
            return ResponseEntity.accepted().body("TOURNAMENT ADDED !!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ILLEGAL INPUTS");
        }
    }

    public ResponseEntity<Object> tournamentPlay(String id) {
        Tournament tournament;
        if (tournamentRepo.findById(id).isPresent()) {
            tournament = tournamentRepo.findById(id).get();
        } else {
            return ResponseEntity.notFound().build();
        }

        List<String> queue = new ArrayList<>(tournament.getTeamsIDsList());
        System.out.println("The Teams Queue: " + queue);
        while (queue.size() > 1) {
            Game game = new Game(queue.get(0), queue.get(1), tournament.getMaxBalls(), tournament.getTotalPlayers());
            queue.remove(0);
            queue.remove(0);
            gameService.initializeNewGame(game);
            gameService.playInnings(game.getGameID(), game.getMaxBalls());
            gameService.playInnings(game.getGameID(), game.getMaxBalls());
            queue.add(game.getWinnerID());
            tournament.getMatchesID().add(game.getGameID());
            tournament.setMatchesID(tournament.getMatchesID());
            tournamentRepo.save(tournament);
        }
        tournament.setWinnerID(queue.get(0));
        tournamentRepo.save(tournament);
        return ResponseEntity.ok("TOURNAMENT FINISHED !!");
    }

    public ResponseEntity<Object> tournamentAsyncPlay(String id) {
        Tournament tournament;
        if (tournamentRepo.findById(id).isPresent()) {
            tournament = tournamentRepo.findById(id).get();
        } else {
            return ResponseEntity.notFound().build();
        }

        while (tournament.getTeamLeftToPlay().size() > 1) {
            playAsyncMatches(tournament);
        }
        tournament.setWinnerID(tournament.getTeamLeftToPlay().get(0));
        tournamentRepo.save(tournament);
        return ResponseEntity.ok("TOURNAMENT FINISHED !!");
    }


    public void playAsyncMatches(Tournament tournament) {
        while (tournament.getTeamLeftToPlay().size() > 1) {
            String teamAID = tournament.getTeamLeftToPlay().remove(0); // remove the first team
            String teamBID = tournament.getTeamLeftToPlay().remove(0); // remove the second team

            CompletableFuture<String> match = CompletableFuture.supplyAsync(() -> playMatch(teamAID, teamBID,
                    tournament.getMaxBalls(),tournament.getTotalPlayers()));
            match.thenAccept(winner -> {
                // add the winner of the match to the back of the list
                tournament.getTeamLeftToPlay().add(winner);
            });
        }

        // Wait for all the matches to finish
        CompletableFuture.allOf(getAllPendingMatches(tournament.getTeamLeftToPlay())).join();
    }
    private CompletableFuture<?>[] getAllPendingMatches(List<String> teamIds) {
        List<CompletableFuture<String>> pendingMatches = new ArrayList<>();
        // Get all the pending matches
        for (String teamId : teamIds) {
            CompletableFuture<String> match = CompletableFuture.supplyAsync(() -> teamId);
            pendingMatches.add(match);
        }

        return pendingMatches.toArray(new CompletableFuture<?>[0]);
    }
    private String playMatch(String teamAID,String teamBID, int maxBalls, int totalPlayers){
        Game game = new Game(teamAID, teamBID,maxBalls, totalPlayers);
        gameService.Autoplay(game.getGameID());
        return game.getWinnerID();
    }
}

