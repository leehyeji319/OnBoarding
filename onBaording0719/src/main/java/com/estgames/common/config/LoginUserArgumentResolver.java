package com.estgames.common.config;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.estgames.web.service.LoginService;
import com.estgames.common.annotation.CurrentUser;

import lombok.RequiredArgsConstructor;
/*
	1.supportsParameter() : 현재 parameter를 resolver가 지원할지 true/false로 반환한다. 즉, 해당 메서드가 참이라면 resolveArgument()를 반환한다.
	2.해당 코드에서는 hasParameterAnnotation 메소드를 사용하여 해당 메소드에 CurrentUser라는 어노테이션이 존재하는지 확인한다.
	3.resolveArgument() : 실제 바인딩할 객체를 반환한다.
	4.해당 코드에서는 현재 로그인된 사용자 객체를 반환한다.
*/

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

	private final LoginService loginService;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(CurrentUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		return loginService.getLoginUser();
	}
}
