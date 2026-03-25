package com.grazac.springauthgrazac.player;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService service;

    @PostMapping
    public PlayerResponse create(@RequestBody PlayerRequest request) {
        return service.createPlayer(request);
    }

    @GetMapping("/{id}")
    public PlayerResponse get(@PathVariable Long id) {
        return service.getPlayer(id);
    }

    @GetMapping
    public List<PlayerResponse> getAll() {
        return service.getAllPlayers();
    }

    @PutMapping("/{id}")
    public PlayerResponse update(@PathVariable Long id,
                                 @RequestBody PlayerRequest request) {
        return service.updatePlayer(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletePlayer(id);
    }
}
