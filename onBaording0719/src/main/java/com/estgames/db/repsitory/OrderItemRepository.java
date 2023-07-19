package com.estgames.db.repsitory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.estgames.db.entiity.OrderItem;
import com.estgames.db.entiity.UserCash;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	@Query("select ot from OrderItem ot where ot.user.id = :userId")
	List<OrderItem> findOrderItemsByUserId(@Param("userId") long userId);

	Page<OrderItem> findAllByUserId(long userId, Pageable pageable);
}
