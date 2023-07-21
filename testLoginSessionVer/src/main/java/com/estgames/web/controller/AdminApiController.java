package com.estgames.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.estgames.web.service.AdminService;
import com.estgames.web.service.LoginService;
import com.estgames.common.annotation.CurrentUser;
import com.estgames.common.annotation.LoginCheck;
import com.estgames.common.annotation.ValidAdmin;
import com.estgames.db.entiity.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminApiController {

	private final AdminService adminService;
	private final LoginService loginService;

	//메인 아이템 추가 삭제
	@LoginCheck
	@ValidAdmin
	@PutMapping("/main/items/{itemId}")
	public String modifyMainItem(@CurrentUser User loginUser, @PathVariable("itemId") long itemId) {
		System.out.println("검증완료. 커스텀 어노테이션 제대로 생성됨!!!!");
		return "admin/itemSetting";
	}


	//메인 배너

	//카테고리

	//아이템

}
