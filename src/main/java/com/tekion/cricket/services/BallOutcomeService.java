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

}
