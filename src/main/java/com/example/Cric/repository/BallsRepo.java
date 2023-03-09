package com.example.Cric.repository;

import com.example.Cric.models.BallOutcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BallsRepo extends JpaRepository<BallOutcome,Integer> {

}
