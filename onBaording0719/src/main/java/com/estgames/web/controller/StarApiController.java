package com.estgames.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.estgames.common.annotation.CurrentUser;
import com.estgames.common.annotation.LoginCheck;
import com.estgames.common.annotation.ValidUser;
import com.estgames.db.entiity.User;
import com.estgames.web.service.StarService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/star")
public class StarApiController {

	private final StarService starService;

	// 사용자 즐겨찾기 추가하기
	@LoginCheck
	@ValidUser
	@PostMapping("/items/{itemId}")
	public String addStarWithUser(@CurrentUser User loginUser, @PathVariable("itemId") long itemId,
		@RequestParam(defaultValue = "/") String redirectURL) {
		long userId = loginUser.getId();
		log.error("즐겨찾기 추가 컨트롤러까지 잘 들어왔따 :::::: userId: " + userId + " 추가할 itemId: " + itemId);
		starService.addStar(userId, itemId);
		return "redirect:" + redirectURL;
	}

	// 사용자 즐겨찾기 취소하기
	@LoginCheck
	@ValidUser
	@DeleteMapping("/items/{itemId}")
	public String removeStarWithUser(@CurrentUser User loginUser, @PathVariable("itemId") long itemId,
		@RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request) {
		long userId = loginUser.getId();
		log.error("즐겨찾기 삭제 컨트롤러까지 잘 들어왔따 :::::: userId: " + userId + " 삭제할 itemId: " + itemId);
		starService.removeStar(userId, itemId);
		return "redirect:" + redirectURL;
	}
}
