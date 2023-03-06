package com.example.Cric.repository;

import com.example.Cric.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayersRepo extends JpaRepository<Player,Integer> {

}
