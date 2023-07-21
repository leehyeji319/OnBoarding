package com.estgames.web.dto.category;


import com.estgames.db.entiity.Category;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategorySaveRequestDto {

	private String categoryName;
	//if targetCategoryId == null : 대분류
	private Long categoryId;

	@Builder
	public CategorySaveRequestDto(String categoryName, long categoryId) {
		this.categoryName = categoryName;
		this.categoryId = categoryId;
	}



	public Category toEntity() {
		return Category.builder()
			.categoryName(this.categoryName)
			.build();
	}

	public Category toEntity(Category parent) {
		return Category.builder()
			.categoryName(this.categoryName)
			.parent(parent)
			.build();
	}
}
