package com.estgames.db.repsitory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.estgames.db.entiity.Star;

public interface StarRepository extends JpaRepository<Star, Long> {

	Optional<Star> findStarByUserIdAndItemId(Long userId, Long itemId);

	List<Star> findStarsByUserId(Long userId);

	@Query("select s from Star s where s.user.id = :userId and s.item.isRemove = false")
	List<Star> findStarsByUserIdAndItemIsNotRemoved(@Param("userId") long userId);

	List<Star> findStarsByItemId(Long itemId);

	Optional<Star> findStarByItemId(long itemId);
}
