package com.tekion.cricket.repository;

import com.tekion.cricket.models.Tournament;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepo extends MongoRepository<Tournament,String> {

}
