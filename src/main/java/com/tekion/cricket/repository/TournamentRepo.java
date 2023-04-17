package com.tekion.cricket.repository;

import com.tekion.cricket.models.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepo extends JpaRepository<Tournament,Integer> {

}