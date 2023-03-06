package com.example.Cric.services;

import com.example.Cric.models.Game;
import com.example.Cric.models.Team;
import com.example.Cric.repository.GamesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {




    @Autowired
    private GamesRepo gamesRepo;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private TeamService teamService;

    //    GET Game Table

    public List<Game> getAllGames(){
        return gamesRepo.findAll();
    }
    //    GET Game By ID

    public Game getGameByID(int id){
        return gamesRepo.findById(id).get();
    }
    //    Start a new Game
    public void startNewGame(Game game){
        game.setStrikerID(nextBatsman(game));
        teamService.getTeamsByID(game.getTeamAID()).setMatchesPlayed();
        teamService.getTeamsByID(game.getTeamBID()).setMatchesPlayed();
        gamesRepo.save(game);
    }

    //    PLAY a Ball and Update Table

    public void playBalls(int gameid, int ballsToPlay){
        Game game = getGameByID(gameid);
        int balls = game.getBallsPlayed()+ballsToPlay;
        if (game.getBallsPlayed() >= game.getMaxBalls() && !game.isFirstInningPlayed()){
            game.setFirstInningPlayed(true);
            game.setBallsPlayed(0);
        }


        int run = 0;
        System.out.println("Here"+game.getBallsPlayed());
        while(game.getBallsPlayed()<game.getMaxBalls() && game.getBallsPlayed()<balls){
            run = playBall(game);
            System.out.println(run);
            if(run == -1){
                break;
            }
            game.setBallsPlayed(game.getBallsPlayed()+1);
        }

        System.out.println("BallsPlayed: "+game.getBallsPlayed());

        if (run == -1){
            if(game.isFirstInningPlayed()){
                game.setSecondInningPlayed(true);
                if(game.getTeamARuns()>game.getTeamBRuns()){
                    game.setWinnerID(game.getTeamAID());
                    teamService.getTeamsByID(game.getTeamAID()).setMatchesWon();
                } else if (game.getTeamBRuns()>game.getTeamARuns()) {
                    game.setWinnerID(game.getTeamBID());
                    teamService.getTeamsByID(game.getTeamBID()).setMatchesWon();
                }else {
                    game.setWinnerID(-1);
                }
            } else {
                game.setFirstInningPlayed(true);
                game.setBallsPlayed(0);
            }
        }
        teamService.updateTeam(teamService.getTeamsByID(game.getTeamAID()));
        teamService.updateTeam(teamService.getTeamsByID(game.getTeamBID()));
        gamesRepo.save(game);
    }
    private int nextBatsman(Game game) {
        if (game.isFirstInningPlayed()){
            Team team = teamService.getTeamsByID(game.getTeamBID());
            return team.getPlayersIDsList().get(game.getTeamBWickets());
        }
        else{
            Team team = teamService.getTeamsByID(game.getTeamAID());
            return team.getPlayersIDsList().get(game.getTeamAWickets());
        }
    }
    private int playBall(Game game){
        int run = playerService.playNextBall(game.getStrikerID());
        //        game.setBallsPlayed(game.getBallsPlayed()+1);
        if(game.isSecondInningPlayed()){
            //            game.setBallsPlayed(game.getMaxBalls());
            return -1;
        } else if (game.isFirstInningPlayed()) {
            if(run != 7){
                game.setTeamBRuns(game.getTeamBRuns() + run);
                if(game.getTeamARuns() < game.getTeamBRuns()){
                    //                    game.setSecondInningPlayed(true);
                    //                    game.setBallsPlayed(game.getMaxBalls());
                    return -1;
                }
            }
            else{
                game.setTeamBWickets(game.getTeamBWickets()+1);
                if (game.getTeamBWickets() == game.getTotalPlayers()-1){
                    //                    game.setSecondInningPlayed(true);
                    return -1;
                }
                System.out.println("PLAYER GOT OUT : "+game.getStrikerID());
                game.setStrikerID(nextBatsman(game));
                System.out.println("PLAYER GOT IN : "+game.getStrikerID());
            }

        }else {
            if(run != 7){
                game.setTeamARuns(game.getTeamARuns() + run);
            }
            else{
                game.setTeamAWickets(game.getTeamAWickets()+1);
                if (game.getTeamAWickets() == game.getTotalPlayers()-1){
                    //                    game.setFirstInningPlayed(true);
                    return -1;
                }
                System.out.println("PLAYER GOT OUT : "+game.getStrikerID());
                game.setStrikerID(nextBatsman(game));
                System.out.println("PLAYER GOT IN : "+game.getStrikerID());
            }
        }
        return run;
    }

}
