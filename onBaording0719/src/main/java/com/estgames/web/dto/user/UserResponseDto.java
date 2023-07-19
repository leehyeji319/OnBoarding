package com.estgames.web.dto.user;

import com.estgames.db.entiity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
	private Long id;
	private String loginId;
	private String name;
	private int cash;

	public static UserResponseDto of(User user) {
		return UserResponseDto.builder()
			.id(user.getId())
			.loginId(user.getLoginId())
			.name(user.getName())
			.cash(user.getCash())
			.build();
	}

	public UserResponseDto(User user) {
		this.id = id;
		this.loginId = loginId;
		this.name = name;
		this.cash = cash;
	}
}