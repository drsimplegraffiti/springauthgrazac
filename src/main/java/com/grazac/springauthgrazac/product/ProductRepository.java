package com.grazac.springauthgrazac.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByNameContainingIgnoreCase(String name);
}