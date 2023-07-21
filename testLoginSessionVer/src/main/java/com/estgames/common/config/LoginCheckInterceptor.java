package com.estgames.common.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.estgames.web.service.LoginService;
import com.estgames.common.annotation.LoginCheck;
import com.estgames.common.exception.ExceptionFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

	private final LoginService loginService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {

		//실행될 컨트롤러의 메소드(핸들러)
		HandlerMethod handlerMethod = (HandlerMethod) handler;

		//해당 핸들러에 이전에 만든 LoginCheck 어노테이션이 존재하는지 확인
		LoginCheck loginCheck = handlerMethod.getMethodAnnotation(LoginCheck.class);

		//1. loginCheck 가 null이라면, 로그인 없이 접근 간으하기 때문에 true를 리턴하여 다음 작없 실행
		if (loginCheck == null) {
			return true;
		}

		//2. loginCheck가 null이 아니라면, session에서 로그인 정보를 꺼내서 null 여부를 판단.
		//null 이라면 로그인 이후 이용이 가능하다는 Exception 날린다.
		if (loginService.getLoginUser() == null) {
			throw ExceptionFactory.invalidAccess();
		}

		return true;

	}
}
