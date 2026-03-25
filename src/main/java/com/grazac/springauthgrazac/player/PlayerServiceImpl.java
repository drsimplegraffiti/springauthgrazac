package com.grazac.springauthgrazac.player;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository repository;
    private final PlayerMapper mapper;

    @Override
    public PlayerResponse createPlayer(PlayerRequest request) {
        Optional<Player> customerExist = repository.findPlayerByEmail(request.getEmail());
        if(customerExist.isPresent()) throw new RuntimeException("customer already exist");
        // convert dto(request) to entity (db instance)

        // when
        Player customer = mapper.toEntity(request);
        Player saved = repository.save(customer);
        // convert entity to dto
        return mapper.toResponse(saved);
    }

    @Override
    public PlayerResponse getPlayer(Long id) {
        Player customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return mapper.toResponse(customer);
    }

    @Override
    public List<PlayerResponse> getAllPlayers() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse) // ✅ method reference updated
                .toList();
    }

    @Override
    public PlayerResponse updatePlayer(Long id, PlayerRequest request) {
        Player customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());

        return mapper.toResponse(repository.save(customer));
    }

    @Override
    public void deletePlayer(Long id) {
        repository.deleteById(id);
    }
}