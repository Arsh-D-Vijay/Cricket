package com.tekion.cricket.services;

import com.tekion.cricket.models.*;
import com.tekion.cricket.repository.GamesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.tekion.cricket.util.Constants.*;

@Service
public class GameService {

    @Autowired
    private GamesRepo gamesRepo;
    @Autowired
    private BallOutcomeService ballsOutcomeService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private PlayerService playerService;

    public ResponseEntity<Object> Autoplay(int gameId) {
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

    public ResponseEntity<Object> getGameByID(int id) {
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
            Team teamB = (Team) teamService.getTeamByID(game.getTeamBID()).getBody();
            teamB.addMatchesPlayed();
            teamService.updateTeam(teamB);
            Team teamA = (Team) teamService.getTeamByID(game.getTeamAID()).getBody();
            teamA.addMatchesPlayed();
            teamService.updateTeam(teamA);
            game.setStrikerID(nextBatsman(game.getTeamAID(), game.getTeamAWickets()));
            gamesRepo.save(game);
            return ResponseEntity.ok("Game Initialized Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR WHILE INITIALIZING !! " + e);
        }
    }
    public ResponseEntity<Object> playInnings(int gameID, int ballsToPlay) {
        Game game;
        if (gamesRepo.findById(gameID).isPresent()) {
            game = gamesRepo.findById(gameID).get();
        } else {
            return ResponseEntity.badRequest().body("GAME NOT FOUND");
        }
        int balls = game.getBallsPlayed() + ballsToPlay;
        if (game.getBallsPlayed() >= game.getMaxBalls() && !game.isFirstInningPlayed()) {
            game.setFirstInningPlayed(true);
            game.setBallsPlayed(0);
        }
        int run = 0;
        while (game.getBallsPlayed() < game.getMaxBalls() && game.getBallsPlayed() < balls) {
            run = playAndUpdateBall(game);
            if (run == GAME_ENDED) {
                break;
            }
            game.setBallsPlayed(game.getBallsPlayed() + 1);
        }
        System.out.println("BallsPlayed: " + game.getBallsPlayed());
        if (run == GAME_ENDED) {
            game = result(game);
        }

        gamesRepo.save(game);
        return ResponseEntity.ok(game);
    }

    private Game result(Game game){
        if (game.isFirstInningPlayed()) {
            game.setSecondInningPlayed(true);
            if (game.getTeamARuns() > game.getTeamBRuns()) {
                game.setWinnerID(game.getTeamAID());
                Team teamA = (Team) teamService.getTeamByID(game.getTeamBID()).getBody();
                teamA.addMatchesWon();
            } else if (game.getTeamBRuns() > game.getTeamARuns()) {
                game.setWinnerID(game.getTeamBID());
                Team teamB = (Team) teamService.getTeamByID(game.getTeamBID()).getBody();
                teamB.addMatchesWon();
            } else {
                game.setWinnerID(DRAW);
            }
        } else {
            game.setFirstInningPlayed(true);
            game.setBallsPlayed(0);
        }
        return game;
    }

    private int nextBatsman(int teamID, int wickets) {
        Team team = (Team) teamService.getTeamByID(teamID).getBody();
        return team.getPlayersIDsList().get(wickets);
    }

    private int playAndUpdateBall(Game game) {
        int run = playerService.playNextBall(game.getStrikerID());
        if (game.isSecondInningPlayed()) {
            return GAME_ENDED;
        } else if (game.isFirstInningPlayed()) {
            if (run != WICKET) {
                game.setTeamBRuns(game.getTeamBRuns() + run);
                ballsOutcomeService.addBallOutcome(
                        new BallOutcome(game.getGameID(), game.getTeamBID(), game.getBallsPlayed(), game.getStrikerID(),
                                run));
                if (game.getTeamARuns() < game.getTeamBRuns()) {
                    return GAME_ENDED;
                }
            } else {
                game.setTeamBWickets(game.getTeamBWickets() + 1);
                ballsOutcomeService.addBallOutcome(
                        new BallOutcome(game.getGameID(), game.getTeamBID(), game.getBallsPlayed(), game.getStrikerID(),
                                -1));
                if (game.getTeamBWickets() == game.getTotalPlayers() - 1) {
                    return GAME_ENDED;
                }
                System.out.println("PLAYER GOT OUT : " + game.getStrikerID());
                game.setStrikerID(nextBatsman(game.getTeamBID(), game.getTeamBWickets()));
                System.out.println("PLAYER GOT IN : " + game.getStrikerID());
            }

        } else {
            if (run != WICKET) {
                game.setTeamARuns(game.getTeamARuns() + run);
                BallOutcome ball = new BallOutcome(game.getGameID(), game.getTeamAID(), game.getBallsPlayed(),
                        game.getStrikerID(), run);
                ballsOutcomeService.addBallOutcome(ball);
            } else {
                game.setTeamAWickets(game.getTeamAWickets() + 1);
                BallOutcome ball = new BallOutcome(game.getGameID(), game.getTeamAID(), game.getBallsPlayed(),
                        game.getStrikerID(), -1);
                ballsOutcomeService.addBallOutcome(ball);
                if (game.getTeamAWickets() == game.getTotalPlayers() - 1) {
                    //                    game.setFirstInningPlayed(true);
                    return GAME_ENDED;
                }
                System.out.println("PLAYER GOT OUT : " + game.getStrikerID());
                game.setStrikerID(nextBatsman(game.getTeamAID(), game.getTeamAWickets()));
                System.out.println("PLAYER GOT IN : " + game.getStrikerID());
            }
        }
        return run;
    }

    public ResponseEntity<Object> rollbackNBalls(int gameID, int nBalls) {
        Game game = (Game) getGameByID(gameID).getBody();

        //        RESET WINNER IF ANY
        if (game.getWinnerID() != null) {
            game.setWinnerID(null);
        }

        //            RESET INNINGS FLAGS IF ANY
        int teamID;
        String team;
        if (game.isFirstInningPlayed() && game.getBallsPlayed() != 0) {
            teamID = game.getTeamBID();
            game.setSecondInningPlayed(false);
            team = "B";
        } else {
            game.setFirstInningPlayed(false);
            teamID = game.getTeamAID();
            team = "A";
        }
        while (nBalls > 0) {
            BallOutcome lastBall = ballsOutcomeService.getLastBall(game.getGameID(), teamID);
            ballsOutcomeService.deleteBall(lastBall);
            if (team == "A") {
                if (lastBall.getBallOutcome() != -1) {
                    //                      Runs RESET
                    game.setTeamARuns(game.getTeamARuns() - lastBall.getBallOutcome());
                    Player striker = (Player) playerService.getPlayerByID(lastBall.getStrikerID()).getBody();
                    striker.addTotalRunsScored(-1 * lastBall.getBallOutcome());
                } else {
                    //                    Wickets RESET
                    game.setTeamAWickets(game.getTeamAWickets() - 1);
                }
                //                    Balls RESET
                game.setBallsPlayed(lastBall.getBallNumber() - 1);
            } else {
                if (lastBall.getBallOutcome() != -1) {
                    //                      Runs RESET
                    game.setTeamARuns(game.getTeamBRuns() - lastBall.getBallOutcome());
                    Player striker = (Player) playerService.getPlayerByID(lastBall.getStrikerID()).getBody();
                    striker.addTotalRunsScored(-1 * lastBall.getBallOutcome());
                } else {
                    //                     Wickets RESET
                    game.setTeamBWickets(game.getTeamAWickets() - 1);
                }
                //                    Balls RESET
                game.setBallsPlayed(lastBall.getBallNumber() - 1);

            }
            nBalls -= 1;
        }
        return ResponseEntity.ok("ROLLBACK SUCCESSFULLY !!");
    }
    public ResponseEntity<Object> getAllGames() {
        try {
            return ResponseEntity.ok(gamesRepo.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("NOT ABLE TO CONNECT TO DB");
        }
    }

}