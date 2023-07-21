package com.estgames.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.estgames.web.dto.cart.CartOrderDto;
import com.estgames.web.dto.cart.UserCartResponseDto;
import com.estgames.web.service.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartApiController {

	private final CartService cartService;
	private final HttpSession session;
	private final UserRepository userRepository;

	@LoginCheck
	@ValidUser
	@PostMapping("/items/{itemId}")
	public String addCartWithUser(@CurrentUser User loginUser, @PathVariable("itemId") long itemId,
		@RequestParam(defaultValue = "/") String redirectURL,
		@RequestParam(value = "count", required = false) Integer count) {
		long userId = loginUser.getId();
		log.error("카트 추가 컨트롤러까지 잘 들어왔따 :::: userId: " + userId + " 추가할 itemId: " + itemId);

		if (count == null) {
			cartService.addCartItem(userId, itemId, 1);

		} else {
			cartService.addCartItem(userId, itemId, count);
		}

		return "redirect:" + redirectURL;

	}

	// @LoginCheck
	// @ValidUser
	// @DeleteMapping("/items/{itemId}")
	// public String removeCartWithUser(@CurrentUser User loginUser, @PathVariable("itemId") long itemId,
	// 	@RequestParam(defaultValue = "/") String redirectURL) {
	// 	long userId = loginUser.getId();
	// 	log.error("카트 삭제 컨트롤러까지 잘 들어왔따 :::: userId: " + userId + " 삭제할 itemId: " + itemId);
	//
	// 	cartService.removeCartItem(userId, itemId);
	//
	// 	return "redirect:" + redirectURL;
	//
	// }

	@LoginCheck
	@ValidUser
	@GetMapping("/items")
	public String getCartListWithUser(@CurrentUser User loginUser, Model model,
		@RequestParam(defaultValue = "/") String redirectURL) {

		long userId = loginUser.getId();

		List<UserCartResponseDto> cartItemList = cartService.findUserCartItemList(userId);

		log.error("CartApiController에서 호출중 .... cartItemList:: " + cartItemList.toString());
		model.addAttribute("userCartItemList", cartItemList);

		return "redirect:" + "/user/cart";
	}

	@LoginCheck
	@ValidUser
	@PatchMapping("/{cartItemId}")
	public @ResponseBody ResponseEntity modifyCartItemCount(@PathVariable("cartItemId") Long cartItemId,
		int count) {

		if (count <= 0) {
			return new ResponseEntity<String>("최소 한개 이상 담아주세요.", HttpStatus.BAD_REQUEST);
		}

		cartService.modifyCartItemCount(cartItemId, count);
		return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
	}

	@LoginCheck
	@ValidUser
	@DeleteMapping("/{cartItemId}")
	public @ResponseBody ResponseEntity removeCartItem(@PathVariable("cartItemId") Long cartItemId) {

		cartService.removeCartItem(cartItemId);

		return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
	}


	//장바구니 상품 주문
	@LoginCheck
	@ValidUser
	@PostMapping("/orders")
	public @ResponseBody ResponseEntity orderCartItem(@CurrentUser User loginUser,
		@RequestBody CartOrderDto cartOrderDto, HttpServletRequest request) {

		List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

		if(cartOrderDtoList == null || cartOrderDtoList.size() == 0){
			return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);
		}


		cartService.orderCartItem(cartOrderDtoList, loginUser.getId());
		// navbar update를 위해 session값을 강제로 update해줬다..
		User updateUser = userRepository.findById(loginUser.getId()).get();
		session.setAttribute(SessionConstants.LOGIN_USER, updateUser);

		return new ResponseEntity<>(true, HttpStatus.OK);
	}

}
