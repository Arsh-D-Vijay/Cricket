package com.tekion.cricket.services;

import com.tekion.cricket.models.Game;
import com.tekion.cricket.repository.GamesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.tekion.cricket.util.Constants.GAME_ENDED;

@Service
public class GameService {

    @Autowired
    private GamesRepo gamesRepo;
    @Autowired
    private BallOutcomeService ballsOutcomeService;
    @Autowired
    private TeamService teamService;

    @Autowired
    private InningService inningService;

    public ResponseEntity<Object> Autoplay(String gameId){
        Game game;
        if(gamesRepo.findById(gameId).isPresent()){
            game = gamesRepo.findById(gameId).get();
        }else{
            return ResponseEntity.badRequest().body("GAME NOT FOUND");
        }
        int balls = gamesRepo.findById(gameId).get().getMaxBalls() + 1;
        playInnings(gameId, balls);
        playInnings(gameId, balls);
        return ResponseEntity.ok("GAME ENDED !!! ");
    }

    public ResponseEntity<Object> getGameByID(String id) {
        if(gamesRepo.findById(id).isPresent()){
            Game game = gamesRepo.findById(id).get();
            return ResponseEntity.ok(game);
        }else{
            return ResponseEntity.badRequest().body("GAME NOT FOUND");
        }
    }

    public void seriesGames(Game game, int noOfMatches) {
        Game game1 = game;
        while (noOfMatches > 0) {
            initializeNewGame(game1);
            int balls = game1.getMaxBalls() + 1;
            playInnings(game1.getGameID(), balls);
            playInnings(game1.getGameID(), balls);
            noOfMatches--;
            game1 = new Game(game.getTeamAID(), game.getTeamBID(), game.getMaxBalls(), game.getTotalPlayers());
        }
    }

    public ResponseEntity<Object> initializeNewGame(Game game) {
        try {
            teamService.getTeamByID(game.getTeamAID()).addMatchesPlayed();
            teamService.getTeamByID(game.getTeamBID()).addMatchesPlayed();
            gamesRepo.save(game);
            return ResponseEntity.ok("Game Initialized Successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("ERROR WHILE INITIALIZING !! "+e);
        }
    }

    public ResponseEntity<Object> playInnings(String gameID, int ballsToPlay) {
        Game game;
        if(gamesRepo.findById(gameID).isPresent()){
            game = gamesRepo.findById(gameID).get();
        }else{
            return ResponseEntity.badRequest().body("GAME NOT FOUND");
        }
        if (game.isSecondInningPlayed()) {
            resultOrContinue(game);
        } else if (game.isFirstInningPlayed()) {
            if (game.getSecondInningID().isEmpty()) {
                inningService.initializeInning(gameID, game.getTeamBID(), game.getMaxBalls(), game.getTotalPlayers(),
                        inningService.getInningByID(game.getFirstInningID()).getRuns());
            }
            int gameContinue = inningService.play(game.getSecondInningID(), ballsToPlay);
            if (gameContinue == GAME_ENDED) {
                resultOrContinue(game);
            }
        }else{
            if (game.getFirstInningID().isEmpty()){
                inningService.initializeInning(gameID, game.getTeamBID(), game.getMaxBalls(), game.getTotalPlayers());
            }
            int gameContinue = inningService.play(game.getFirstInningID(), ballsToPlay);
            if (gameContinue == GAME_ENDED) {
                resultOrContinue(game);
            }
        }
        teamService.updateTeam(teamService.getTeamByID(game.getTeamAID()));
        teamService.updateTeam(teamService.getTeamByID(game.getTeamBID()));
        gamesRepo.save(game);
        return ResponseEntity.ok(game);
    }

    private void resultOrContinue(Game game) {
        if (game.isFirstInningPlayed()) {
            game.setSecondInningPlayed(true);
            if (inningService.getInningByID(game.getFirstInningID()).getRuns() > inningService.getInningByID(game.getSecondInningID()).getRuns()) {
                game.setWinnerID(game.getTeamAID());
                teamService.getTeamByID(game.getTeamAID()).addMatchesWon();
            } else if (inningService.getInningByID(game.getFirstInningID()).getRuns() < inningService.getInningByID(game.getSecondInningID()).getRuns()) {
                game.setWinnerID(game.getTeamBID());
                teamService.getTeamByID(game.getTeamBID()).addMatchesWon();
            } else {
                game.setWinnerID("Draw");
            }
        } else {
            game.setFirstInningPlayed(true);
        }
        gamesRepo.save(game);
    }

    public ResponseEntity<Object> getAllGames() {
        try{
            return ResponseEntity.ok(gamesRepo.findAll());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("NOT ABLE TO CONNECT TO DB");
        }
    }
}