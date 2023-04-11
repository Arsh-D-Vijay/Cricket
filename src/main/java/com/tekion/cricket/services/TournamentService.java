package com.tekion.cricket.services;

import com.tekion.cricket.models.Game;
import com.tekion.cricket.models.Tournament;
import com.tekion.cricket.repository.TournamentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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
        tournament.setTeamLeftToPlay(tournament.getTeamsIDsList().stream().collect(Collectors.toList()));
        while (tournament.getTeamLeftToPlay().size() > 1) {

            Game game = new Game(tournament.getTeamLeftToPlay().get(0), tournament.getTeamLeftToPlay().get(1),
                    tournament.getMaxBalls(), tournament.getTotalPlayers());

            tournament.getTeamLeftToPlay().remove(0);
            tournament.getTeamLeftToPlay().remove(0);

            tournament.setTeamLeftToPlay(tournament.getTeamLeftToPlay());
            System.out.println(tournament.getTeamLeftToPlay());

            gameService.initializeNewGame(game);
            gameService.playInnings(game.getGameID(), game.getMaxBalls());
            gameService.playInnings(game.getGameID(), game.getMaxBalls());
            Game gameUpdated = (Game) gameService.getGameByID(game.getGameID()).getBody();

            try {
                tournament.getTeamLeftToPlay().add(gameUpdated.getWinnerID());
            } catch (Exception e) {
                List<String> match = new ArrayList<>();
                match.add(gameUpdated.getWinnerID());
                tournament.setTeamLeftToPlay(match);
            }
            tournament.setTeamLeftToPlay(tournament.getTeamLeftToPlay());
            System.out.println(tournament.getTeamLeftToPlay());

            if (tournament.getMatchesID() == null) {
                String gameID = gameUpdated.getGameID();

                List<String> match = new ArrayList<>();
                match.add(gameID);
                tournament.setMatchesID(match);
            } else {
                tournament.getMatchesID().add(game.getGameID());
                tournament.setMatchesID(tournament.getMatchesID());
            }
            tournamentRepo.save(tournament);
            tournament = tournamentRepo.findById(id).get();
            System.out.println(tournament);
        }
        tournament.setWinnerID(tournament.getTeamLeftToPlay().get(0));
        tournamentRepo.save(tournament);
        return ResponseEntity.ok("TOURNAMENT FINISHED !!");
    }

    public ResponseEntity<Object> tournamentAsyncPlay(String id) {
        Tournament tournament1;
        if (tournamentRepo.findById(id).isPresent()) {
            tournament1 = tournamentRepo.findById(id).get();
        } else {
            return ResponseEntity.notFound().build();
        }
        System.out.println("Tournament LOADED :: " + tournament1);
        tournament1.setTeamLeftToPlay(tournament1.getTeamsIDsList().stream().collect(Collectors.toList()));
        ExecutorService executorService = Executors.newFixedThreadPool(tournament1.getTeamsIDsList().size() / 2);
        int maxBalls = tournament1.getMaxBalls();
        int totalPlayers = tournament1.getTotalPlayers();

        while (tournament1.getTeamLeftToPlay().size() > 1) {
            Tournament tournament = tournament1;
            System.out.println("--------------LOOP-----------");
            CountDownLatch latch = new CountDownLatch(tournament.getTeamLeftToPlay().size() / 2);
            List<String> winners = new ArrayList<>();

            for (int i = 0; i < tournament.getTeamLeftToPlay().size(); i += 2) {
                String team1ID = tournament.getTeamLeftToPlay().get(i);
                String team2ID = tournament.getTeamLeftToPlay().get(i + 1);
                System.out.println("-----MATCH STAGED----" + team1ID +"  "+ team2ID);
                //                final CountDownLatch matchLatch = latch;
                executorService.submit(() -> {
                    String[] game = playMatch(team1ID, team2ID, maxBalls, totalPlayers);
                    System.out.println("-----MATCH PLAYED----" + game[0] +  "  :  " + game[1]);
                    if (tournament.getMatchesID() == null) {
                        System.out.println("if");
                        List<String> match = new ArrayList<>();
                        match.add(game[0]);
                        tournament.setMatchesID(match);
                        System.out.println("Everything is ok in if");
                    } else {
                        System.out.println("else" + tournament.getMatchesID());
                        tournament.getMatchesID().add(game[0]);
                        System.out.println("addedGame");
                        tournament.setMatchesID(tournament.getMatchesID());
                        System.out.println("Everything is ok in else");
                    }
                    String winner = game[1];
                    tournamentRepo.save(tournament);
                    winners.add(winner);
                    System.out.println("Winners : " + winners);
                    latch.countDown();
                    System.out.println("countdown");

                });
            }

            try {
                System.out.println("WAITING FOR LATCHING");
                latch.await();
                System.out.println("------LATCHED----------");
            } catch (InterruptedException e) {
                System.out.println(e);
                e.printStackTrace();
            }
            tournament.setTeamLeftToPlay(winners.stream().collect(Collectors.toList()));
            tournamentRepo.save(tournament);
            tournament1 = tournamentRepo.findById(id).get();
        }

        tournament1.setWinnerID(tournament1.getTeamLeftToPlay().get(0));
        executorService.shutdown();
        tournamentRepo.save(tournament1);
        return ResponseEntity.ok("TOURNAMENT FINISHED !!");
    }


    private String[] playMatch(String teamAID, String teamBID, int maxBalls, int totalPlayers) {
        //        System.out.println("AM I PLAYING");
        Game game = new Game(teamAID, teamBID, maxBalls, totalPlayers);
        gameService.initializeNewGame(game);
        //        System.out.println("GAME CREATED");
        gameService.Autoplay(game.getGameID());
        //        System.out.println("YES I DID");
        game = (Game) gameService.getGameByID(game.getGameID()).getBody();
        return new String[] {game.getGameID(), game.getWinnerID() };
    }
}

