package com.example.Cric.repository;

import com.example.Cric.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamsRepo extends JpaRepository<Team, Integer> {

}
