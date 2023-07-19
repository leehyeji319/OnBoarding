package com.estgames.web.service;

import static com.estgames.web.controller.SessionConstants.*;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.estgames.db.entiity.User;
import com.estgames.db.repsitory.UserRepository;
import com.estgames.db.repsitory.UserRepositoryImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

	private final UserRepositoryImpl userRepositoryImpl;
	private final UserRepository userRepository;
	private final HttpSession session;


	public User login(String loginId, String password) {

		// return userRepositoryImpl.findByLoginId(loginId).filter(u -> u.getPassword().equals(password))
		// 	.orElse(null);
		//if null then login fail
		// return userRepository.findByLoginId(loginId).filter(u -> u.getPassword().equals(password))
		// 	.orElse(null);

		log.error("로그인 진행 중.....");
		Optional<User> loginUser = userRepository.findByLoginIdAndPassword(loginId, password);
		return loginUser.orElse(null);

	}

	public User getLoginUser() {
		return (User)session.getAttribute(LOGIN_USER);

	}

}
