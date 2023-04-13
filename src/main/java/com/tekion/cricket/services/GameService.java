package com.tekion.cricket.services;

import com.tekion.cricket.models.*;
import com.tekion.cricket.repository.GamesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.tekion.cricket.util.Constants.GAME_ENDED;
import static com.tekion.cricket.util.Constants.WICKET;

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
    @Autowired
    private PlayerService playerService;

    public ResponseEntity<Object> Autoplay(String gameId) {
        Game game;
        if (gamesRepo.findById(gameId).isPresent()) {
            game = gamesRepo.findById(gameId).get();
        } else {
            return ResponseEntity.badRequest().body("GAME NOT FOUND");
        }
        int balls = gamesRepo.findById(gameId).get().getMaxBalls() + 1;
        playInnings(gameId, balls);
        playInnings(gameId, balls);
        return ResponseEntity.ok("GAME ENDED !!! ");
    }

    public ResponseEntity<Object> getGameByID(String id) {
        if (gamesRepo.findById(id).isPresent()) {
            Game game = gamesRepo.findById(id).get();
            return ResponseEntity.ok(game);
        } else {
            return ResponseEntity.badRequest().body("GAME NOT FOUND");
        }
    }

    public ResponseEntity<Object> seriesGames(Game game, int noOfMatches) {
        Game game1 = game;
        while (noOfMatches > 0) {
            initializeNewGame(game1);
            int balls = game1.getMaxBalls() + 1;
            playInnings(game1.getGameID(), balls);
            playInnings(game1.getGameID(), balls);
            noOfMatches--;
            game1 = new Game(game.getTeamAID(), game.getTeamBID(), game.getMaxBalls(), game.getTotalPlayers());
        }
        return ResponseEntity.ok("SERIES MATCH PLAYED");
    }

    public ResponseEntity<Object> initializeNewGame(Game game) {
        try {
            Team teamB = teamService.getTeamByID(game.getTeamBID());
            teamB.addMatchesPlayed();
            teamService.updateTeam(teamB);
            Team teamA = teamService.getTeamByID(game.getTeamAID());
            teamA.addMatchesPlayed();
            teamService.updateTeam(teamA);
            gamesRepo.save(game);
            return ResponseEntity.ok("Game Initialized Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR WHILE INITIALIZING !! " + e);
        }
    }

    public ResponseEntity<Object> playInnings(String gameID, int ballsToPlay) {
        Game game;
        if (gamesRepo.findById(gameID).isPresent()) {
            game = gamesRepo.findById(gameID).get();
        } else {
            return ResponseEntity.badRequest().body("GAME NOT FOUND");
        }
        if (game.isSecondInningPlayed()) {
            resultOrContinue(game);
        } else if (game.isFirstInningPlayed()) {
            if (game.getSecondInningID() == null) {
                Inning inning = inningService.initializeInning(gameID, game.getTeamBID(), game.getMaxBalls(),
                        game.getTotalPlayers(), inningService.getInningByID(game.getFirstInningID()).getRuns());
                game.setSecondInningID(inning.getInningID());
            }
            int gameContinue = inningService.play(game.getSecondInningID(), ballsToPlay);
            if (gameContinue == GAME_ENDED) {
                resultOrContinue(game);
            }
        } else {
            if (game.getFirstInningID() == null) {
                Inning inning = inningService.initializeInning(gameID, game.getTeamAID(), game.getMaxBalls(),
                        game.getTotalPlayers());
                game.setFirstInningID(inning.getInningID());

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
            if (inningService.getInningByID(game.getFirstInningID()).getRuns() >
                inningService.getInningByID(game.getSecondInningID()).getRuns()) {
                game.setWinnerID(game.getTeamAID());
                Team team = teamService.getTeamByID(game.getTeamAID());
                team.addMatchesWon();
                teamService.updateTeam(team);
            } else if (inningService.getInningByID(game.getFirstInningID()).getRuns() <
                       inningService.getInningByID(game.getSecondInningID()).getRuns()) {
                game.setWinnerID(game.getTeamBID());
                Team team = teamService.getTeamByID(game.getTeamBID());
                team.addMatchesWon();
                teamService.updateTeam(team);
            } else {
                game.setWinnerID("Draw");
            }
        } else {
            game.setFirstInningPlayed(true);
        }
        gamesRepo.save(game);
    }

    public ResponseEntity<Object> getAllGames() {
        try {
            return ResponseEntity.ok(gamesRepo.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("NOT ABLE TO CONNECT TO DB");
        }
    }

    public ResponseEntity<Object> rollbackNBalls(String gameID, int nBalls) {

        Game game;
        if (gamesRepo.findById(gameID).isPresent()) {
            game = gamesRepo.findById(gameID).get();
        } else {
            return ResponseEntity.badRequest().body("GAME NOT FOUND");
        }
        String inningID = getLastBallInning(game);
        Inning inning = inningService.getInningByID(inningID);

        if (game.getWinnerID() != null) {
            Team team = teamService.getTeamByID(game.getWinnerID());
            team.setMatchesWon(team.getMatchesWon() - 1);
            game.setWinnerID(null);
            game.setSecondInningPlayed(false);
        }
        if(game.getSecondInningID() != null){
            inning.setTarget(null);
        }
        if(inningID.equals(game.getFirstInningID())){
            game.setSecondInningPlayed(false);
            game.setFirstInningPlayed(false);
        }
        for (int i = 0; i < nBalls; i++) {
            BallOutcome lastBall = ballsOutcomeService.getBall(game.getGameID(), inning.getBattingTeamID(),
                    inning.getBallsPlayed());
            if(lastBall == null){
                return ResponseEntity.internalServerError().body("ONLY "+i+" balls were removed as there is no more " +
                                                                 "balls played in the inning");
            }
            if (lastBall.getBallOutcome() != WICKET) {
                Player striker = (Player) playerService.getPlayerByID(lastBall.getStrikerID()).getBody();
                striker.setTotalRunsScored(striker.getTotalRunsScored() - lastBall.getBallOutcome());
                playerService.addOrUpdatePlayer(striker);
                inning.setRuns(inning.getRuns()- lastBall.getBallOutcome());
            } else {
                inning.setStrikerID(lastBall.getStrikerID());
                inning.setWickets(inning.getWickets()-1);
            }
            inning.setBallsPlayed(inning.getBallsPlayed() - 1);
            ballsOutcomeService.deleteBall(lastBall);
        }
        return ResponseEntity.ok(nBalls + " Balls rolled back successfully");
    }

    private String getLastBallInning(Game game) {
        if (game.getSecondInningID() != null) {
            int ballsPlayed = inningService.getInningByID(game.getSecondInningID()).getBallsPlayed();
            if (ballsPlayed == 0) {
                return game.getFirstInningID();
            }
            return game.getSecondInningID();
        }
        return game.getFirstInningID();
    }
}