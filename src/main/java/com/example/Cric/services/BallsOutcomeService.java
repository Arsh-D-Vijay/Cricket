package com.example.Cric.services;

import com.example.Cric.models.BallOutcome;
import com.example.Cric.repository.BallsRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BallsOutcomeService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private BallsRepo ballsRepo;
    public BallOutcome getBallOutcome(int gameID,int teamID,int ballNumber){
        Query query;
        query = entityManager.createNativeQuery("SELECT * FROM ball_outcome WHERE gameID = :id AND teamID = :teamID " +
                                                "AND " +
                                                "ball_number = :ball"
                , BallOutcome.class);
        query.setParameter("id",gameID);
        query.setParameter("teamID",teamID);
        query.setParameter("ball",ballNumber);
        BallOutcome ball = (BallOutcome) query.getSingleResult();
        return ball;
    }
    public void addBallOutcome(BallOutcome ballOutcome){
        ballsRepo.save(ballOutcome);
    }
    public void deleteBallOutcome(BallOutcome ballOutcome){
        ballsRepo.delete(ballOutcome);
    }

    public BallOutcome getLastBall(int gameID, int teamID){
        Query query = entityManager.createNativeQuery("SELECT * FROM ball_outcome WHERE gameID = :id AND teamID = " +
                                                      ":teamID " +
                                                      "ORDER BY ball_number DESC LIMIT 1", BallOutcome.class);
        query.setParameter("id",gameID);
        query.setParameter("teamID",teamID);
        BallOutcome ball = (BallOutcome) query.getSingleResult();
        return ball;

    }
}
