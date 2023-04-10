package com.tekion.cricket.services;

import com.tekion.cricket.models.BallOutcome;
import com.tekion.cricket.models.Inning;
import com.tekion.cricket.models.Team;
import com.tekion.cricket.repository.InningRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.tekion.cricket.util.Constants.*;

@Service
public class InningService {


    @Autowired
    private InningRepo inningRepo;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private BallOutcomeService ballOutcomeService;

    @Autowired
    private TeamService teamService;


    public Inning getInningByID(String id){
        return inningRepo.findById(id).get();
    }

    public void initializeInning(String gameID, String battingTeamID, int maxBalls, int totalPlayers) {
        Inning inning = new Inning(gameID,battingTeamID,maxBalls, totalPlayers);
        inning.setStrikerID(nextBatsman(inning.getBattingTeamID(), inning.getWickets()));
        inning.setNonStrikerID(nextBatsman(inning.getBattingTeamID(), inning.getWickets()+1));
        inningRepo.save(inning);
    }
    public void initializeInning(String gameID, String battingTeamID, int maxBalls, int totalPlayers, int target) {
        Inning inning = new Inning(gameID,battingTeamID,maxBalls, totalPlayers);
        inning.setTarget(target);
        inning.setStrikerID(nextBatsman(inning.getBattingTeamID(), inning.getWickets()));
        inning.setNonStrikerID(nextBatsman(inning.getBattingTeamID(), inning.getWickets()+1));
        inningRepo.save(inning);
    }

    public int play(String secondInningID, int ballsToPlay) {
        Inning inning = getInningByID(secondInningID);
        int balls = inning.getBallsPlayed() + ballsToPlay;
        int run = 0;
        while(inning.getBallsPlayed() <= inning.getMaxBalls() && inning.getBallsPlayed() <= balls){
            run = update(inning);
            if(run == GAME_ENDED){
                return GAME_ENDED;
            }
            inning.setBallsPlayed(inning.getBallsPlayed()+1);
        }
        teamService.updateTeam(teamService.getTeamByID(inning.getBattingTeamID()));
        inningRepo.save(inning);
        return GAME_CONTINUE;
    }

    private int update(Inning inning) {
        int run = playerService.playNextBall(inning.getStrikerID());
        ballOutcomeService.addBallOutcome(
                new BallOutcome(inning.getGameID(), inning.getBattingTeamID(), inning.getBallsPlayed(),
                        inning.getStrikerID(),
                        run));
        if (run != WICKET) {
            inning.setRuns(inning.getRuns() + run);

            if ((inning.getTarget() != null) && (inning.getRuns() > inning.getTarget())) {
                return GAME_ENDED;
            }
        } else {
            inning.setWickets(inning.getWickets() + 1);
            if (inning.getWickets() == inning.getTotalPlayers() - 1) {
                return GAME_ENDED;
            }
            inning.setStrikerID(nextBatsman(inning.getBattingTeamID(), inning.getWickets()));
        }
        return run;
    }
    private String nextBatsman(String teamID, int wickets) {
        Team team = teamService.getTeamByID(teamID);
        return team.getPlayersIDsList().get(wickets);
    }

}
