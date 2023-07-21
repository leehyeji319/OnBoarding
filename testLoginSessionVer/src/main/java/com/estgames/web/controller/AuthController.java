package com.estgames.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.estgames.common.annotation.CurrentUser;
import com.estgames.web.dto.auth.LoginForm;
import com.estgames.web.service.LoginService;
import com.estgames.db.entiity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
// @RequestMapping("/api/auth")
@Slf4j
public class AuthController {

	private final LoginService loginService;

	@GetMapping("/login")
	public String loginForm(@ModelAttribute LoginForm loginForm) {

		return "login/loginForm";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute @Validated LoginForm loginForm,
		BindingResult bindingResult, @RequestParam(defaultValue = "/") String redirectURL,
		HttpServletRequest request) {
		System.out.println("로그인버튼 누름! 유저아이디 : " + loginForm.getLoginId() + " 비번 : " +loginForm.getPassword());

		if (bindingResult.hasErrors()) {
			return "login/loginForm";
		}
		User loginUser = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

		if (loginUser == null) {
			bindingResult.reject("login fail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "login/loginForm";
		}

		log.error(loginUser.toString());

		System.out.println("로그인 성공!!");
		//로그인 성공
		HttpSession session = request.getSession();// 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성하여 반환
		session.setAttribute(SessionConstants.LOGIN_USER, loginUser);// 세션에 로그인 회원 정보 보관

		return "redirect:" + redirectURL;
	}

	@PostMapping("/logout")
	public String logout(HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); //세션 정보 삭제
		}

		return "redirect:/";
	}
}
