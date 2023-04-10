package com.tekion.cricket.repository;

import com.tekion.cricket.models.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamsRepo extends MongoRepository<Team, String> {

}
