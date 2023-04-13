package com.tekion.cricket.services;


import com.tekion.cricket.models.BallOutcome;
import com.tekion.cricket.repository.BallOutcomeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BallOutcomeService {

    @Autowired
    private BallOutcomeRepo ballsRepo;

    public void addBallOutcome(BallOutcome ballOutcome) {
        ballsRepo.save(ballOutcome);
    }

    public BallOutcome getBall(String gameID, String battingTeamID, int ballNumber){
        return ballsRepo.findByGameIDAndBattingTeamIDAndBallNumber(gameID,battingTeamID,ballNumber);
    }

    public void deleteBall(BallOutcome ball){
        ballsRepo.delete(ball);
    }

}
