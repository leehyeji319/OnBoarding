package com.estgames.web.dto.category;

import java.util.List;

import com.estgames.db.entiity.Category;

import lombok.Data;

@Data
public class CategoryParentsResponseDto {

	private long categoryParentId;
	private String categoryParentName;
	private List<CategoryChildDto> categoryChildDtoList;

	public CategoryParentsResponseDto(Category category) {
		this.categoryParentId = category.getId();
		this.categoryParentName = category.getCategoryName();
	}
	public CategoryParentsResponseDto(long categoryParentId, String categoryParentName) {
		this.categoryParentId = categoryParentId;
		this.categoryParentName = categoryParentName;
	}
}
