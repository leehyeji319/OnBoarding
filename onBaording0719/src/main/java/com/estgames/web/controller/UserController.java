package com.estgames.web.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.estgames.common.annotation.CurrentUser;
import com.estgames.common.annotation.LoginCheck;
import com.estgames.common.annotation.ValidUser;
import com.estgames.db.entiity.User;
import com.estgames.web.dto.cart.UserCartResponseDto;
import com.estgames.web.dto.cash.UserCashLogDto;
import com.estgames.web.dto.category.CategoryParentsResponseDto;
import com.estgames.web.dto.item.ItemResponseDto;
import com.estgames.web.dto.order.UserOrderItemDto;
import com.estgames.web.service.CartService;
import com.estgames.web.service.CategoryService;
import com.estgames.web.service.ItemService;
import com.estgames.web.service.OrderItemService;
import com.estgames.web.service.StarService;
import com.estgames.web.service.UserCashService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final CategoryService categoryService;
	private final StarService starService;
	private final CartService cartService;
	private final UserCashService userCashService;
	private final OrderItemService orderItemService;
	private final ItemService itemService;

	@LoginCheck
	@ValidUser
	@GetMapping("/home")
	public String userHome(@CurrentUser User loginUser, Model model) {

		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("user", loginUser);

		return "loginHome";
	}


	//TODO: 네비게이션 바 - 아이템 보러가기 눌렀을 때 나오는 아이템 화면 - 페이징 처리까지
	@LoginCheck
	@ValidUser
	@GetMapping("/item")
	public String userItem(@CurrentUser User loginUser,  @PageableDefault(page = 1) Pageable pageable,
		Model model) {
		//일단 아이템 전체를 보내준다.

		Page<ItemResponseDto> userItemList = itemService.findItemListWithUserIdV3(loginUser.getId(), pageable);

		log.error("들어온 거 한번 보자~~~~ userController.userItem:::::::" + userItemList.toString());
		/*
		 * block limit : page 개수 설정
		 * 현재 사용자가 선택한 페이지 앞뒤로 3페이지씩만 보여준다.
		 * ex. 현재 사용자가 4페이지 라면 2, 3, (4), 5, 6 이렇게
		 * */

		int blockLimit = 5;
		int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
		int endPage = Math.min((startPage + blockLimit - 1), userItemList.getTotalPages());

		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		model.addAttribute("userItemList", userItemList);

		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("categoryId", null);


		return "/user/items/item";
	}


	// === 즐겨찾기 === //
	@LoginCheck
	@ValidUser
	@GetMapping("/star")
	public String userStar(@CurrentUser User loginUser, Model model) {
		model.addAttribute("user", loginUser);

		//즐겨찾기 리스트 가져오기
		List<ItemResponseDto> starItemList = starService.findStarListWithUserId(loginUser.getId());

		model.addAttribute("starItemList", starItemList);
		return "user/star";

	}

	// === 장바구니 === //
	@LoginCheck
	@ValidUser
	@GetMapping("/cart")
	public String userCart(@CurrentUser User loginUser, Model model) {
		model.addAttribute("user", loginUser);

		//장바구니 리스트 가져오기
		List<UserCartResponseDto> cartItemList = cartService.findUserCartItemList(loginUser.getId());

		log.error("UserController에서 호출중....userId:: " + loginUser.getId() + " cartItemList::" + cartItemList.toString());

		model.addAttribute("cartItemList", cartItemList);

		return "user/cart";
	}

	// === 이용내역 - 충전 === //
	@LoginCheck
	@ValidUser
	@GetMapping("/history/charge")
	public String userChargeHistory(@CurrentUser User loginUser, @PageableDefault(page = 1) Pageable pageable,
		Model model) {
		model.addAttribute("user", loginUser);

		//유저의 충전 내역 리스트 보내주기
		// List<UserCashLogDto> userCashLogList = userCashService.findUserCashLogListV2(loginUser.getId());

		Page<UserCashLogDto> userCashLogList = userCashService.findUserCashLogListV3(loginUser.getId(), pageable);

		log.error("들어온 거 한번 보자~~~~ userController.userChargeHistory:::::::" + userCashLogList.toString());
		/*
		* block limit : page 개수 설정
		* 현재 사용자가 선택한 페이지 앞뒤로 3페이지씩만 보여준다.
		* ex. 현재 사용자가 4페이지 라면 2, 3, (4), 5, 6 이렇게
		* */

		int blockLimit = 3;
		int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
		int endPage = Math.min((startPage + blockLimit - 1), userCashLogList.getTotalPages());

		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);


		model.addAttribute("userCashLogList", userCashLogList);

		return "user/history_charge";
	}

	// === 이용내역 - 구매 === //
	@LoginCheck
	@ValidUser
	@GetMapping("/history/purchase")
	public String userPurchaseHistory(@CurrentUser User loginUser, @PageableDefault(page = 1) Pageable pageable,
		Model model) {
		model.addAttribute("user", loginUser);

		//유저의 구매 내역 리스트 보내주기
		Page<UserOrderItemDto> userOrderItemList = orderItemService.findUserOrderItemListV3(loginUser.getId(),
			pageable);
		log.error("들어온 거 한번 보자~~~~ userController.userPurchaseHistory:::::::" + userOrderItemList.toString());
		int blockLimit = 3;
		int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
		int endPage = Math.min((startPage + blockLimit - 1), userOrderItemList.getTotalPages());

		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);


		model.addAttribute("userOrderItemList", userOrderItemList);

		return "user/history_purchase";
	}

	// === 캐시충전 === //
	@LoginCheck
	@ValidUser
	@GetMapping("/charge")
	public String userCharge(@CurrentUser User loginUser, Model model) {
		model.addAttribute("user", loginUser);

		return "charge-modal";
	}
}
