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

    public void saveAndUpdateInning(Inning inning){
        inningRepo.save(inning);
    }

    public Inning initializeInning(String gameID, String battingTeamID, int maxBalls, int totalPlayers) {
        Inning inning = new Inning(gameID,battingTeamID,maxBalls, totalPlayers);
        inning.setStrikerID(nextBatsman(inning.getBattingTeamID(), inning.getWickets()));
        inning.setNonStrikerID(nextBatsman(inning.getBattingTeamID(), inning.getWickets()+1));
        inningRepo.save(inning);
        return inning;
    }
    public Inning initializeInning(String gameID, String battingTeamID, int maxBalls, int totalPlayers, int target) {
        Inning inning = new Inning(gameID,battingTeamID,maxBalls, totalPlayers);
        inning.setTarget(target);
        inning.setStrikerID(nextBatsman(inning.getBattingTeamID(), inning.getWickets()));
        inning.setNonStrikerID(nextBatsman(inning.getBattingTeamID(), inning.getWickets()+1));
        inningRepo.save(inning);
        return inning;
    }

    public int play(String inningID, int ballsToPlay) {
        Inning inning = getInningByID(inningID);
        int balls = inning.getBallsPlayed() + ballsToPlay;
        int run = 0;
        while(inning.getBallsPlayed() < inning.getMaxBalls() && inning.getBallsPlayed() <= balls){
            inning.setBallsPlayed(inning.getBallsPlayed()+1);
            run = updateBall(inning);
            if(run == GAME_ENDED){
                inningRepo.save(inning);
                return GAME_ENDED;
            }
//            System.out.println("REACHED HERE");
        }
        teamService.updateTeam(teamService.getTeamByID(inning.getBattingTeamID()));
        inningRepo.save(inning);
        if(inning.getBallsPlayed() == inning.getMaxBalls()){
            return GAME_ENDED;
        }
        return GAME_CONTINUE;
    }

    private int updateBall(Inning inning) {
        int run = playerService.playNextBall(inning.getStrikerID());
//        System.out.println("RUNS : "+ run);
        ballOutcomeService.addBallOutcome(
                new BallOutcome(inning.getGameID(), inning.getBattingTeamID(), inning.getBallsPlayed(),
                        inning.getStrikerID(),
                        run));
//        System.out.println("BALL SAVED");
        if (run != WICKET) {
            inning.setRuns(inning.getRuns() + run);
            if ((inning.getTarget() != null) && (inning.getRuns() > inning.getTarget())) {
                return GAME_ENDED;
            }
            if(run%2 != 0){
                swapSides(inning);
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

    private void swapSides(Inning inning) {
        String temp = inning.getStrikerID();
        inning.setStrikerID(inning.getNonStrikerID());
        inning.setNonStrikerID(temp);
        inningRepo.save(inning);
    }

    private String nextBatsman(String teamID, int wickets) {
        Team team = teamService.getTeamByID(teamID);
        return team.getPlayersIDsList().get(wickets);
    }

}
