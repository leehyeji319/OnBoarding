package com.estgames.web.dto.star;


import com.estgames.db.entiity.Star;

import lombok.Builder;
import lombok.Data;

@Data
public class StarResponseDto {
	private Long starId;
	private Long userId;
	private Long itemId;

	@Builder
	public StarResponseDto(Long starId, Long userId, Long itemId) {
		this.starId = starId;
		this.userId = userId;
		this.itemId = itemId;
	}

	public StarResponseDto(Star star) {
		this.starId = star.getId();
		this.userId = star.getUser().getId();
		this.itemId = star.getItem().getId();
	}
}
