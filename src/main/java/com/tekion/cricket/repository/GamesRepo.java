package com.tekion.cricket.repository;


import com.tekion.cricket.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamesRepo extends JpaRepository<Game, Integer> {

}
