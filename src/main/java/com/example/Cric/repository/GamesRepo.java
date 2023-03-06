package com.example.Cric.repository;

import com.example.Cric.models.Game;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamesRepo extends JpaRepository<Game, Integer> {

}
