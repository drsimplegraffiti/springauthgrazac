package com.grazac.springauthgrazac.player;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findPlayerByEmail(String email);
    Boolean existsByEmail(String email);
}