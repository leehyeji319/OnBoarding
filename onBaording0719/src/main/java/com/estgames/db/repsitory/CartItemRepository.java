package com.estgames.db.repsitory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.estgames.db.entiity.CartItem;
import com.estgames.db.entiity.Item;
import com.estgames.db.entiity.User;

// @NoRepositoryBean
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	Optional<CartItem> findByCartIdAndItemId(long cartId, long itemId);

	@Query("select ci from CartItem ci where ci.cart.user.id = :userId and ci.item.id = :itemId and ci.item.isRemove = false")
	Optional<CartItem> findByUserIdAndItemId(@Param("userId") long userId, @Param("itemId") long itemId);

	@Query("select ci from CartItem ci where ci.cart.user.id = :userId and ci.item.isRemove = false")
	List<CartItem> findByUserId(@Param("userId") long userId);

	@Query("select ci.cart.user.id from CartItem ci where ci.id = :cartItemId")
	User findUserByCartItemId(@Param("cartItemId") long cartItemId);

	@Query("select ci.item from CartItem ci where ci.id = :cartItemId")
	Item findItemByCartItemId(@Param("cartItemId") long cartItemId);
}
