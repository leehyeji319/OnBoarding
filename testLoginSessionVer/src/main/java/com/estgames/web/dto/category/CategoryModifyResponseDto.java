package com.estgames.web.dto.category;

import lombok.Builder;
import lombok.Data;

@Data
public class CategoryModifyResponseDto {

	private long parentCategoryId;
	private String parentCategoryName;
	private long categoryId;
	private String categoryName;

	@Builder
	public CategoryModifyResponseDto(long parentCategoryId, String parentCategoryName, long categoryId,
		String categoryName) {
		this.parentCategoryId = parentCategoryId;
		this.parentCategoryName = parentCategoryName;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}
}
