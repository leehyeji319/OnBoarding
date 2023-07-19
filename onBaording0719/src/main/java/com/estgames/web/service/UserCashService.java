package com.estgames.web.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estgames.web.dto.cash.ChargeDto;
import com.estgames.web.dto.cash.UserCashLogDto;
import com.estgames.web.dto.cash.UserCashLogResponseDto;
import com.estgames.db.entiity.User;
import com.estgames.db.entiity.UserCash;
import com.estgames.db.repsitory.UserCashRepository;
import com.estgames.db.repsitory.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserCashService {

	private final UserCashRepository userCashRepository;
	private final UserRepository userRepository;

	public int findUserCashStatus(long userId) {
		User findUser = validateExistUserById(userId);
		return findUser.getCash();
	}

	//api 버전
	public UserCashLogResponseDto findUserCashLogList(long userId) {
		validateExistUserById(userId);
		UserCashLogResponseDto responseDto = new UserCashLogResponseDto(userId);
		List<UserCash> userCashList = userCashRepository.findUserCashByUserId(userId);
		List<UserCashLogDto> collect = userCashList.stream()
			.map(UserCashLogDto::new)
			.collect(Collectors.toList());

		responseDto.setUserCashLogList(collect);

		return responseDto;
	}


	//SSR 버전
	public List<UserCashLogDto> findUserCashLogListV2(long userId) {
		validateExistUserById(userId);

		List<UserCash> userCashList = userCashRepository.findUserCashByUserId(userId);
		List<UserCashLogDto> collect = userCashList.stream()
			.map(UserCashLogDto::new)
			.collect(Collectors.toList());
		return collect;
	}

	//SSR + paging 처리 버전
	public Page<UserCashLogDto> findUserCashLogListV3(long userId, Pageable pageable) {
		int page = pageable.getPageNumber() - 1; //page위치에 있는 값은 0부터 시작
		int pageLimit = 10; //한 페이지에 보여줄 글 개수

		// 한 페이지당 10개씩 글을 보여주고 정렬 기준은 ID 기준으로 내림차순
		Page<UserCash> userCashLogListPages = userCashRepository.findAllByUserId(
			userId, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "chargeDate")));

		Page<UserCashLogDto> result = userCashLogListPages.map(UserCashLogDto::new);

		return result;
	}


	//캐시 충전
	@Transactional
	public void addUserCash(long userId, ChargeDto chargeDto) {
		User findUser = validateExistUserById(userId);
		int amount = Integer.parseInt(chargeDto.getChargeAmount().replaceAll("[^0-9]", ""));

		findUser.chargeCash(amount);

		UserCash savedUserCash = userCashRepository.save(UserCash.builder()
			.chargeDate(LocalDateTime.now())
			.chargeType(chargeDto.getChargeMethodWay())
			.chargeAmount(amount)
			.chargeBeforeCash(findUser.getCash() - amount)
			.chargeAfterCash(findUser.getCash())
			.user(findUser)
			.build());

	}


	public User validateExistUserById(long userId) {
		return userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 id의 user가 존재하지 않습니다. userId : " + userId)
		);
	}

	public List<UserCashLogResponseDto> findAllUserCashLogList() {

		List<UserCash> all = userCashRepository.findAll();
		// all.stream()
		// 	.map()
		return null;
	}
}
