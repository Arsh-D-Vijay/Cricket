package com.tekion.cricket.repository;

import com.tekion.cricket.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayersRepo extends JpaRepository<Player,Integer> {

}
