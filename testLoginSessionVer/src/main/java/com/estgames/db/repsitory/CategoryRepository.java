package com.estgames.db.repsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estgames.db.entiity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findCategoryByCategoryName(String categoryName);
}
