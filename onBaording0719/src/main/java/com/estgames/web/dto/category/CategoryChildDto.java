package com.estgames.web.dto.category;

import com.estgames.db.entiity.Category;

import lombok.Data;

@Data
public class CategoryChildDto {

	private long categoryParentId;
	private long categoryChildId;
	private String categoryChildName;

	public CategoryChildDto(Category category) {
		this.categoryParentId = category.getParent().getId();
		this.categoryChildId = category.getId();
		this.categoryChildName = category.getCategoryName();
	}

	public CategoryChildDto(long categoryParentId, long categoryChildId, String categoryChildName) {
		this.categoryParentId = categoryParentId;
		this.categoryChildId = categoryChildId;
		this.categoryChildName = categoryChildName;
	}
}
