package com.estgames.web.dto.star;

import java.util.List;

import com.estgames.web.dto.item.ItemResponseDto;

import lombok.Data;

@Data
public class StarListWithUserResponseDto {
	private Long userId;
	private List<ItemResponseDto> userStarItemList;

	public StarListWithUserResponseDto(Long userId, List<ItemResponseDto> userStarItemList) {
		this.userId = userId;
		this.userStarItemList = userStarItemList;
	}

	public StarListWithUserResponseDto(Long userId) {
		this.userId = userId;
	}
}
