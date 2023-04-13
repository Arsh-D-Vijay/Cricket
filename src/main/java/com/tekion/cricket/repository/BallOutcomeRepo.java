package com.tekion.cricket.repository;


import com.tekion.cricket.models.BallOutcome;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BallOutcomeRepo extends MongoRepository<BallOutcome,String> {
    BallOutcome findByGameIDAndTeamIDAndBallNumber(String gameID, String battingTeamID, int ballNumber);

}
