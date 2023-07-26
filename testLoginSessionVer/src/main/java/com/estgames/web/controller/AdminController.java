package com.estgames.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.estgames.common.annotation.CurrentUser;
import com.estgames.common.annotation.LoginCheck;
import com.estgames.common.annotation.ValidAdmin;
import com.estgames.db.entiity.User;
import com.estgames.web.dto.BannerImgDto;
import com.estgames.web.dto.category.CategoryModifyRequestDto;
import com.estgames.web.dto.category.CategoryParentsResponseDto;
import com.estgames.web.dto.category.CategorySaveRequestDto;
import com.estgames.web.dto.item.ItemModifyRequestDto;
import com.estgames.web.dto.item.ItemResponseDto;
import com.estgames.web.dto.item.ItemSaveRequestDto;
import com.estgames.web.service.AdminService;
import com.estgames.web.service.BannerService;
import com.estgames.web.service.CategoryService;
import com.estgames.web.service.ItemService;
import com.estgames.web.service.LoginService;
import com.estgames.web.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;
	private final LoginService loginService;
	private final ItemService itemService;
	private final CategoryService categoryService;
	private final UserService userService;
	private final BannerService bannerService;

	@LoginCheck
	@ValidAdmin
	@GetMapping("/home")
	public String adminHome(@CurrentUser User loginUser, Model model) {

		//기존 배너들 가져오기
		List<BannerImgDto> bannerList = bannerService.findBannerList();
		model.addAttribute("bannerList", bannerList);

		return "admin/adminHome";
	}

	//메인 아이템 추가 페이지
	@LoginCheck
	@ValidAdmin
	@GetMapping("/main/items")
	public String mainItem(@CurrentUser User loginUser, Model model) {

		//배너 아이템 띄워주기 -> 아직 구현 안됨

		//현재 메인 아이템들을 찾아서 보여준다.
		List<ItemResponseDto> mainItemList = itemService.findMainItemList();
		System.out.println(mainItemList.size());
		model.addAttribute("mainItemListDto", mainItemList);


		return "admin/mainSetting";
	}

	//======= 배너 ========//

	@LoginCheck
	@ValidAdmin
	@GetMapping("/main/banners")
	public String mainBanner(@CurrentUser User loginUser, Model model) {

		//기존 배너들 가져오기
		List<BannerImgDto> bannerList = bannerService.findBannerList();
		model.addAttribute("bannerList", bannerList);

		return "admin/bannerSetting";
	}

	//======= 아이템 =======//

	//아이템 셋팅 화면
	@LoginCheck
	@ValidAdmin
	@GetMapping("/items")
	public String editItems(@CurrentUser User loginUser, Model model) {

		//카테고리 리스트
		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);

		List<ItemResponseDto> itemList = itemService.findItemList();
		model.addAttribute("itemList", itemList);
		// ItemResponseDto modifyTargetDto = new ItemResponseDto();
		// model.addAttribute("modifyTargetDto", modifyTargetDto);
		return "admin/itemSetting";
	}

	//아이템 생성 폼
	@LoginCheck
	@ValidAdmin
	@GetMapping("/items/add")
	public String addItemForm(@CurrentUser User loginUser, Model model) {

		//생성할 아이템 dto
		ItemSaveRequestDto itemSaveRequestDto = new ItemSaveRequestDto();

		model.addAttribute("itemSaveRequestDto", itemSaveRequestDto);

		//카테고리 리스트 호출 대분류 중분류(셀렉트 박스를 위해)
		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);
		return "item/itemAddForm";
	}

	//아이템 수정 폼
	@LoginCheck
	@ValidAdmin
	@PostMapping("/items/{itemId}/modify")
	public String modifyItemForm(@CurrentUser User loginUser, @PathVariable("itemId") long itemId,
		Model model) {

		log.error("수정할 아이템 아디가 잘 들어왔어요 itemId : " + itemId);
		//수정할 아이템의 기존 객체 내용
		ItemResponseDto itemResponseDto = itemService.findItem(itemId);
		log.error(itemResponseDto.toString());
		model.addAttribute("itemResponseDto", itemResponseDto);

		//카테고리 리스트 호출 대분류 중분류(셀렉트 박스를 위해)
		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);

		//새로운 폼 DTO 전달 -> 이걸 put mapping으로 다음 controller에 보내야함
		ItemModifyRequestDto itemModifyRequestDto = new ItemModifyRequestDto();
		model.addAttribute("itemModifyRequestDto", itemModifyRequestDto);

		return "item/itemEditForm";
	}

	//아이템 삭제 관리 페이지
	@LoginCheck
	@ValidAdmin
	@GetMapping("/items/remove")
	public String removedItemManage(@CurrentUser User loginUser, Model model) {

		List<ItemResponseDto> itemList = itemService.findItemListWithIsRemoveTrue();
		model.addAttribute("itemList", itemList);

		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);

		return "item/itemRemoveList";
	}


	// ====== 카테고리 ===== //
	@LoginCheck
	@ValidAdmin
	@GetMapping("/categories")
	public String editCategories(@CurrentUser User loginUser, Model model) {

		//카테고리 리스트 호출 대분류 중분류(셀렉트 박스를 위해)
		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);

		//생성할 카테고리 dto 객체
		CategorySaveRequestDto categorySaveRequestDto = new CategorySaveRequestDto();
		model.addAttribute("categorySaveRequestDto", categorySaveRequestDto);

		//수정할 카테고리 dto 객체
		CategoryModifyRequestDto categoryModifyRequestDto = new CategoryModifyRequestDto();
		model.addAttribute("categoryModifyRequestDto", categoryModifyRequestDto);


		return "admin/categorySetting";
	}



}
