package com.estgames.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.estgames.common.annotation.CurrentUser;
import com.estgames.common.annotation.LoginCheck;
import com.estgames.common.annotation.ValidUser;
import com.estgames.db.entiity.User;
import com.estgames.db.repsitory.UserRepository;
import com.estgames.web.dto.cash.ChargeDto;
import com.estgames.web.service.UserCashService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/cash")
public class CashApiController {

	private final UserCashService userCashService;
	private final UserRepository userRepository;
	private final HttpSession session;

	@LoginCheck
	@ValidUser
	@PostMapping
	public @ResponseBody ResponseEntity userCashCharge(@CurrentUser User loginUser,
		@RequestBody ChargeDto chargeDto, @RequestParam(defaultValue = "/") String redirectURL) {

		log.error("들어온 충전 정보 내역.......... ::: " + chargeDto.toString());

		if (chargeDto.getChargeAmount() == null || chargeDto.getChargeMethodWay() == null) {
			return new ResponseEntity<String>("선택된 내용이 없습니다.", HttpStatus.FORBIDDEN);
		}


		userCashService.addUserCash(loginUser.getId(), chargeDto);
		User updateUser = userRepository.findById(loginUser.getId()).get();
		session.setAttribute(SessionConstants.LOGIN_USER, updateUser);

		return new ResponseEntity<>(true, HttpStatus.OK);
	}

}
