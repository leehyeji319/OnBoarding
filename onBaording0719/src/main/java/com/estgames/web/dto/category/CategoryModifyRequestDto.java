package com.estgames.web.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryModifyRequestDto {

	private Long parentCategoryId;
	private String parentCategoryName;
	private Long categoryId;
	private String categoryName;

	private Long targetCategoryId;


}
