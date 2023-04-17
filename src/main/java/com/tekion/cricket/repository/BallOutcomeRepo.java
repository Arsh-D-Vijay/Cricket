package com.tekion.cricket.repository;


import com.tekion.cricket.models.BallOutcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BallOutcomeRepo extends JpaRepository<BallOutcome,Integer> {
}