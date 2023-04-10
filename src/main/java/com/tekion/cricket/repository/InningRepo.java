package com.tekion.cricket.repository;

import com.tekion.cricket.models.Inning;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InningRepo extends MongoRepository<Inning,String> {

}
