package com.tekion.cricket.repository;

import com.tekion.cricket.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamsRepo extends JpaRepository<Team, Integer> {

}
