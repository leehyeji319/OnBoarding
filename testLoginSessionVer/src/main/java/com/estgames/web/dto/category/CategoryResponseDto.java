package com.estgames.web.dto.category;


import com.estgames.db.entiity.Category;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryResponseDto {
	private long parentCategoryId;
	private String parentCategoryName;
	private long categoryId;
	private String categoryName;
	// private List<>

	@Builder
	public CategoryResponseDto(long parentCategoryId, String parentCategoryName, long categoryId, String categoryName) {
		this.parentCategoryId = parentCategoryId;
		this.parentCategoryName = parentCategoryName;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}

	public CategoryResponseDto(Category category) {
		this.parentCategoryId = category.getParent().getId();
		this.parentCategoryName = category.getParent().getCategoryName();
		this.categoryId = category.getId();
		this.categoryName = category.getCategoryName();
	}
}
