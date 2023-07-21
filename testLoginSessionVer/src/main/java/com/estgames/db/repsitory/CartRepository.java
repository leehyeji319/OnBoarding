package com.estgames.db.repsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estgames.db.entiity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Optional<Cart> findCartByUserId(long userId);

}
