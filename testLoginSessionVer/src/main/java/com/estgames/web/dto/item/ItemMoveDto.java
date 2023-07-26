package com.estgames.web.dto.item;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemMoveDto {

	private Long targetCategoryId;
	private List<Long> itemIdList;

}
