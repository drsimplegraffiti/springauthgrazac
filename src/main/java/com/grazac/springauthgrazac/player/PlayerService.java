package com.grazac.springauthgrazac.player;


import java.util.List;

public interface PlayerService {

    PlayerResponse createPlayer(PlayerRequest request);
    PlayerResponse getPlayer(Long id);
    List<PlayerResponse> getAllPlayers();
    PlayerResponse updatePlayer(Long id, PlayerRequest request);
    void deletePlayer(Long id);
}
