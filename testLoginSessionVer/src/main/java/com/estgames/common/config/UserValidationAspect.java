package com.estgames.common.config;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.estgames.db.entiity.User;
import com.estgames.db.repsitory.UserRepository;
import com.estgames.common.exception.ExceptionFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class UserValidationAspect {
	private final UserRepository userRepository;

	@Autowired
	public UserValidationAspect(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	//로그인 된 상태인지 확인
	@Before("@annotation(com.estgames.common.annotation.ValidLogin) && args(request, ..)")
	public void validationIsLogin(HttpServletRequest request) {
		if (request.getSession(false) == null) {
			throw ExceptionFactory.invalidAccess();
		}
	}

	//일반 유저인지 확인
	@Before("@annotation(com.estgames.common.annotation.ValidUser) && args(loginUser, ..)")
	public void validationUser(User loginUser) {
		//해당 유저가 일반 유저인지 확인
		log.info("일반 유저인지 검증애노테이션 실행중..");
		if (loginUser.getLoginId().equals("admin")) {
			throw ExceptionFactory.authenticateUserFail();
		}
	}

	//관리자 유저인지 확인
	@Before("@annotation(com.estgames.common.annotation.ValidAdmin) && args(loginUser, ..)")
	public void validationAdmin(User loginUser) {
		//해당 유저가 관리자 유저인지 확인
		log.info("관리자 유저인지 검증애노테이션 실행중..");
		if (!loginUser.getLoginId().equals("admin")) {
			throw ExceptionFactory.authenticateAdminFail();
		}
	}
}
