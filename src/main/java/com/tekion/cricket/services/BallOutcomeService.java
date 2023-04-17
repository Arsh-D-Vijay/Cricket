package com.tekion.cricket.services;


import com.tekion.cricket.models.BallOutcome;
import com.tekion.cricket.repository.BallOutcomeRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BallOutcomeService {

    @Autowired
    private BallOutcomeRepo ballsRepo;
    @PersistenceContext
    private EntityManager entityManager;

    public void addBallOutcome(BallOutcome ballOutcome) {
        ballsRepo.save(ballOutcome);
    }

    public BallOutcome getLastBall(int gameID, int teamID) {
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM ball_outcome WHERE gameID = :id AND teamID = " + ":teamID " +
                "ORDER BY ball_number DESC LIMIT 1", BallOutcome.class);
        query.setParameter("id", gameID);
        query.setParameter("teamID", teamID);
        BallOutcome ball = (BallOutcome) query.getSingleResult();
        return ball;
    }

    public void deleteBall(BallOutcome ball){
        ballsRepo.delete(ball);
    }

}
