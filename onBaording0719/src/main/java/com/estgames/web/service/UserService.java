package com.estgames.web.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estgames.web.dto.user.UserModifyResponseDto;
import com.estgames.web.dto.user.UserResponseDto;
import com.estgames.db.entiity.User;
import com.estgames.db.repsitory.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public UserModifyResponseDto modifyUser(long userId, UserModifyResponseDto requestDto) {
		return new UserModifyResponseDto();
	}

	@Transactional
	public long removeUser(long userId) {
		userRepository.deleteById(userId);

		return userId;
	}

	public List<UserResponseDto> findUserList() {
		List<User> list = userRepository.findAll();

		return list.stream()
			.map(UserResponseDto::new)
			.collect(Collectors.toList());
	}

	public UserResponseDto findUser(long userId) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("사용자의 id가 존재하지 않습니다. userId: " + userId)
		);

		return UserResponseDto.builder()
			.id(userId)
			.loginId(user.getLoginId())
			.name(user.getName())
			.cash(user.getCash())
			.build();
	}
}
