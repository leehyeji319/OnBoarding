package com.estgames.web.dto.cash;

import java.util.List;

import lombok.Data;

@Data
public class UserCashLogResponseDto {

	private Long userId;
	private List<UserCashLogDto> userCashLogList;

	public UserCashLogResponseDto(Long userId, List<UserCashLogDto> userCashLogList) {
		this.userId = userId;
		this.userCashLogList = userCashLogList;
	}

	public UserCashLogResponseDto(Long userId) {
		this.userId = userId;
	}
}

