package com.estgames.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.estgames.common.annotation.CurrentUser;
import com.estgames.common.annotation.LoginCheck;
import com.estgames.common.annotation.ValidUser;
import com.estgames.db.entiity.User;
import com.estgames.web.service.CartService;
import com.estgames.web.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderApiController {

	private final OrderService orderService;
	private final CartService cartService;

	//구매하기 버튼을 누르면 장바구니를 불러오면 된다..
	@LoginCheck
	@ValidUser
	@PostMapping("/items/{itemId}")
	public String addCartWithUser(@CurrentUser User loginUser, @PathVariable("itemId") long itemId,
		@RequestParam(defaultValue = "/") String redirectURL, Model model,
		@RequestParam(value = "count", required = false) Integer count) {
		long userId = loginUser.getId();
		log.error("카드 추가 컨트롤러까지 잘 들어왔따 :::: userId: " + userId + " 추가할 itemId: " + itemId);

		if (count == null) {

			cartService.addCartItem(userId, itemId, 1);
		} else {
			cartService.addCartItem(userId, itemId, count);
		}


		//유저정보 넣어주기
		model.addAttribute("user", loginUser);


		return "redirect:" + "/users/cart";
	}

}

