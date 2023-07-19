package com.estgames.web.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.estgames.common.annotation.CurrentUser;
import com.estgames.common.annotation.LoginCheck;
import com.estgames.common.annotation.ValidAdmin;
import com.estgames.common.annotation.ValidUser;
import com.estgames.db.entiity.Category;
import com.estgames.db.entiity.Item;
import com.estgames.db.entiity.User;
import com.estgames.db.repsitory.CategoryRepository;
import com.estgames.db.repsitory.ItemRepository;
import com.estgames.web.dto.category.CategoryParentsResponseDto;
import com.estgames.web.dto.item.ItemModifyRequestDto;
import com.estgames.web.dto.item.ItemResponseDto;
import com.estgames.web.dto.item.ItemSaveRequestDto;
import com.estgames.web.service.CategoryService;
import com.estgames.web.service.ItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemApiController {

	private final ItemService itemService;
	private final CategoryService categoryService;
	private final CategoryRepository categoryRepository;
	private final ItemRepository itemRepository;

	// =================== 유저 ================== //

	@LoginCheck
	@GetMapping("/categories/{categoryId}")
	public String getItemListWithCategoryId(@PathVariable("categoryId") long categoryId, Model model) {
		log.error("카테고리 조회로 성공적으로 컨트롤러까지 왔어요. categoryId: " + categoryId);

		// 카테고리 별로 아이템 호출
		List<ItemResponseDto> itemList = itemService.findItemListWithCategoryId(categoryId);
		model.addAttribute("itemList", itemList);

		// ModelAndView modelAndView = new ModelAndView("admin/itemSetting");
		// modelAndView.addObject("itemList", itemList);

		// return modelAndView;

		return "admin/itemSetting :: #items";
	}

	@LoginCheck
	@ValidUser
	@GetMapping("/categories/{categoryId}/user")
	public String getItemListWithCategoryIdAndUserV3(@CurrentUser User loginUser, Model model,
		@PathVariable("categoryId") long categoryId, @PageableDefault(page = 1) Pageable pageable) {

		Page<ItemResponseDto> userItemListWithCategoryId =
			itemService.findItemListWithCategoryIdWithUserIdV3(loginUser.getId(), categoryId, pageable);

		log.error("들어온 거 한번 보자~~~~ itemApiController.getItemListWithCategoryIdAndUserV3:::::::");

		/*
		 * block limit : page 개수 설정
		 * 현재 사용자가 선택한 페이지 앞뒤로 3페이지씩만 보여준다.
		 * ex. 현재 사용자가 4페이지 라면 2, 3, (4), 5, 6 이렇게
		 * */

		int blockLimit = 5;
		int startPage = (((int)Math.ceil(((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
		int endPage = Math.min((startPage + blockLimit - 1), userItemListWithCategoryId.getTotalPages());

		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		model.addAttribute("userItemList", userItemListWithCategoryId);

		//=== category ===//
		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);

		model.addAttribute("categoryId", categoryId);
		Category category = categoryRepository.findById(categoryId).get();

		if (category.getParent() == null) {
			model.addAttribute("categoryParentName", category.getCategoryName());
			model.addAttribute("categoryChildName", "전체");
		} else {
			model.addAttribute("categoryParentName", category.getParent().getCategoryName());
			model.addAttribute("categoryChildName", category.getCategoryName());
		}

		return "/user/items/item";
	}

	//아이템 개별 조회 - 유저 버전
	@LoginCheck
	@ValidUser
	@GetMapping("/{itemId}")
	public String getItemWithUser(@CurrentUser User loginUser, @PathVariable("itemId") long itemId, Model model) {

		//아이템 개별 dto 담아주기
		ItemResponseDto itemResponseDto = itemService.findItemWithUser(itemId, loginUser.getId());
		model.addAttribute("item", itemResponseDto);

		//item의 카테고리 분류 가져오기 (중분류들만)
		Item findItem = itemRepository.findById(itemId).get();
		// List<ItemResponseDto> otherItemList = itemService.findItemListWithCategoryId(findItem.getCategory().getId());

		List<ItemResponseDto> otherItemList = itemService.findItemListWithCategoryIdWithUserId(
			findItem.getCategory().getId(), loginUser.getId());
		model.addAttribute("otherItemList", otherItemList);

		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);

		return "/user/items/item_detail";
	}

	@LoginCheck
	@ValidUser
	@GetMapping("/search/user")
	public String getItemWithSearchKeywordAndUser(@CurrentUser User loginUser,
		@RequestParam(value = "keyword", required = false) String keyword,
		@PageableDefault(page = 1) Pageable pageable,
		Model model) {

		log.error("유저가 아이템을 검색할 때 :: itemApiController.getItemWithSearchKeywordAndUser:::: keyword=" + keyword);

		Page<ItemResponseDto> userItemList;

		if (keyword == null) {
			return "redirect:" + "/users/item";
		} else {
			userItemList = itemService.searchItemWithKeywordV3(keyword, loginUser.getId(), pageable);
		}

		int blockLimit = 5;
		int startPage = (((int)Math.ceil(((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
		int endPage = Math.min((startPage + blockLimit - 1), userItemList.getTotalPages());

		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		model.addAttribute("userItemList", userItemList);

		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("categoryId", null);
		model.addAttribute("keyword", keyword);
		return "/user/items/item";
	}

	//============== 어드민 =============//

	//아이템 검색
	@LoginCheck
	@ValidAdmin
	@GetMapping("/search")
	public String searchItem(@CurrentUser User loginUser, @RequestParam("keyword") String keyword, Model model) {
		List<ItemResponseDto> responseDtoList = itemService.findItemWithKeywordAndIsNotMain(keyword);
		model.addAttribute("itemSearchResultList", responseDtoList);

		//현재 메인 아이템들을 찾아서 보여준다.
		List<ItemResponseDto> mainItemList = itemService.findMainItemList();
		System.out.println(mainItemList.size());
		model.addAttribute("mainItemListDto", mainItemList);

		return "/admin/mainSetting";
	}

	// 아이템 메인에 추가하기
	@LoginCheck
	@ValidAdmin
	@PutMapping("/{itemId}/main")
	public String addItemToMain(@CurrentUser User loginUser, @PathVariable("itemId") long itemId, Model model) {
		log.info("메인에 추가할 아이템~~ itemId : " + itemId);
		itemService.addItemToMain(itemId);

		//현재 메인 아이템들을 찾아서 보여준다.
		List<ItemResponseDto> mainItemList = itemService.findMainItemList();
		model.addAttribute("mainItemListDto", mainItemList);

		return "/admin/mainSetting";
	}

	//아이템 메인에서 삭제
	@LoginCheck
	@ValidAdmin
	@DeleteMapping("/{itemId}/main")
	public String removeItemToMain(@CurrentUser User loginUser, @PathVariable("itemId") long itemId, Model model) {
		log.info("메인에서 삭제할 아이템~~ itemId : " + itemId);
		itemService.removeItemToMain(itemId);

		//현재 메인 아이템들을 찾아서 보여준다.
		List<ItemResponseDto> mainItemList = itemService.findMainItemList();
		model.addAttribute("mainItemListDto", mainItemList);

		return "admin/mainSetting";
	}

	//아이템 새로 생성 (추가)
	@LoginCheck
	@ValidAdmin
	@PostMapping
	public String addItem(@CurrentUser User loginUser, @ModelAttribute ItemSaveRequestDto requestDto,
		@RequestPart("itemImgFile") MultipartFile itemImgFile, Model model) throws Exception {

		log.error("아이템 생성 컨틀롤러 들어왔땅께 ~~~~! : " + requestDto.toString());
		log.error("아이템 생성 컨틀롤러 들어왔땅께 ~~~~! : " + itemImgFile.getOriginalFilename());
		itemService.addItem(requestDto, itemImgFile);

		//카테고리 리스트 호출 대분류 중분류(셀렉트 박스를 위해)
		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);

		List<ItemResponseDto> itemList = itemService.findItemList();
		model.addAttribute("itemList", itemList);

		return "admin/itemSetting";
	}

	//아이템 삭제
	@LoginCheck
	@ValidAdmin
	@DeleteMapping("/{itemId}")
	public String removeItem(@PathVariable("itemId") long itemId, Model model) throws Exception {
		//아이템 삭제하는 서비스 호출
		long l = itemService.removeItem(itemId);
		log.error("삭제된 itemId : " + itemId);

		//카테고리 리스트 호출 대분류 중분류(셀렉트 박스를 위해)
		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);

		List<ItemResponseDto> itemList = itemService.findItemList();
		model.addAttribute("itemList", itemList);
		return "admin/itemSetting";
	}

	//아이템 수정
	@LoginCheck
	@ValidAdmin
	@PutMapping("/{itemId}")
	public String modifyItem(@PathVariable("itemId") long itemId,
		@ModelAttribute ItemModifyRequestDto itemModifyRequestDto,
		@RequestPart("itemImgFile") MultipartFile itemImgFile, Model model) throws Exception {
		log.error(
			"아이템 수정 컨틀롤러 들어왔땅께 ~~~~! : " + itemModifyRequestDto.toString()
				+ "::: itemImgFile:::::" + itemImgFile.getOriginalFilename());
		itemService.modifyItem(itemId, itemModifyRequestDto, itemImgFile);

		List<ItemResponseDto> itemList = itemService.findItemList();
		model.addAttribute("itemList", itemList);
		return "admin/itemSetting";
	}

	// 아이템 개별 검색 - 어드민 전용
	@LoginCheck
	@ValidAdmin
	@GetMapping("/{itemId}/admin")
	public String findItemWithAdmin(@PathVariable("itemId") long itemId, Model model) {

		//개별 아이템
		ItemResponseDto item = itemService.findItem(itemId);
		model.addAttribute("item", item);

		//카테고리 리스트
		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);

		return "item/itemAdminDetail";

	}

}
