package com.tekion.cricket.repository;

import com.tekion.cricket.models.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayersRepo extends MongoRepository<Player,String> {

}
