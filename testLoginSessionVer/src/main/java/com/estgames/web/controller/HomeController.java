package com.estgames.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.estgames.common.annotation.CurrentUser;
import com.estgames.db.entiity.User;
import com.estgames.web.dto.BannerImgDto;
import com.estgames.web.dto.cash.ChargeDto;
import com.estgames.web.dto.item.ItemResponseDto;
import com.estgames.web.service.BannerService;
import com.estgames.web.service.ItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

	private final ItemService itemService;
	private final BannerService bannerService;

	@GetMapping("/")
	public String home(
		// @SessionAttribute(name = SessionConstants.LOGIN_USER, required = false)
		@CurrentUser
		User loginUser, Model model) {
		// 세션에 회원 데이터가 없으면 홈으로 이동
		System.out.println("여기 들어옴!!");
		if (loginUser == null) {
			return "home";
		}

		// 세션이 유지되면 로그인 홈으로 이동
		model.addAttribute("user", loginUser);

		int originalSize;
		int tmpSize;
		if (loginUser.getLoginId().equals("admin")) {
			List<ItemResponseDto> mainItemList = itemService.findMainItemList();

			originalSize = mainItemList.size();
			if (originalSize < 12) {
				tmpSize = originalSize / 2;
			} else {
				tmpSize = 8;
			}

			List<ItemResponseDto> mainItemListWithUserForRow = new ArrayList<>(mainItemList.subList(0, tmpSize));
			List<ItemResponseDto> mainItemListWithUserForColumn = new ArrayList<>(mainItemList.subList(tmpSize, mainItemList.size()));
			model.addAttribute("mainItemListWithUserForRow", mainItemListWithUserForRow);
			model.addAttribute("mainItemListWithUserForColumn", mainItemListWithUserForColumn);
			//기존 배너들 가져오기
			List<BannerImgDto> bannerList = bannerService.findBannerList();
			model.addAttribute("bannerList", bannerList);

			return "/admin/adminHome";
		}


		// 만약 일반 유저가 로그인 한 상태라면, 해당 유저의 메인 페이지를 리스트에 담아서 로그인 홈으로 보내준다.
		//나는 리스트 두개를 짤라서 보여줘야하기 때문에, 8개 4개로 짤라서 보내준다. ㅋㅋ


		List<ItemResponseDto> mainItemListWithUser = itemService.findMainItemListWithUser(loginUser.getId());
		originalSize = mainItemListWithUser.size();
		if (originalSize < 12) {
			tmpSize = originalSize / 2;
		} else {
			tmpSize = 8;
		}
		List<ItemResponseDto> mainItemListWithUserForRow = new ArrayList<>(mainItemListWithUser.subList(0, tmpSize));
		List<ItemResponseDto> mainItemListWithUserForColumn = new ArrayList<>(mainItemListWithUser.subList(tmpSize, mainItemListWithUser.size()));
		model.addAttribute("mainItemListWithUserForRow", mainItemListWithUserForRow);
		model.addAttribute("mainItemListWithUserForColumn", mainItemListWithUserForColumn);
		//기존 배너들 가져오기
		List<BannerImgDto> bannerList = bannerService.findBannerList();
		model.addAttribute("bannerList", bannerList);


		return "loginHome";
	}
}