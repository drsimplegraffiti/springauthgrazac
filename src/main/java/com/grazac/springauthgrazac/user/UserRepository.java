package com.grazac.springauthgrazac.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Optional<User> findUserByEmail(String email);

    Optional<User> findByEmail(String username);

    Optional<User> findUserByUsername(String username);
}
