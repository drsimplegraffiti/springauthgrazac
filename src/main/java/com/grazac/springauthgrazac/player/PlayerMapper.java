package com.grazac.springauthgrazac.player;

import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public Player toEntity(PlayerRequest request) {
        if (request == null) return null;

        return Player.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
    }


    // destination                       source will be the parmas ----> cue
    public PlayerResponse toResponse(Player player) {
        if (player == null) return null;

        return PlayerResponse.builder()
                .id(player.getId())
                .name(player.getName())
                .email(player.getEmail())
                .build();
    }
}