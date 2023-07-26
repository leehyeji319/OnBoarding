package com.estgames.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.estgames.common.annotation.CurrentUser;
import com.estgames.common.annotation.LoginCheck;
import com.estgames.common.annotation.ValidAdmin;
import com.estgames.db.entiity.User;
import com.estgames.web.dto.category.CategoryModifyRequestDto;
import com.estgames.web.dto.category.CategoryParentsResponseDto;
import com.estgames.web.dto.category.CategorySaveRequestDto;
import com.estgames.web.service.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryApiController {

	private final CategoryService categoryService;

	// @LoginCheck
	@GetMapping
	public List<CategoryParentsResponseDto> getCategoryList() {
		return categoryService.findCategoryList();
	}

	@LoginCheck
	@ValidAdmin
	@PostMapping
	public String addCategory(@CurrentUser User loginUser, @ModelAttribute CategorySaveRequestDto requestDto,
		Model model) {

		log.error("카테고리 생성 컨트롤러 잘 들어왔어요!!! ::::: " + requestDto.toString());

		categoryService.addCategory(requestDto);
		//카테고리 리스트 호출 대분류 중분류(셀렉트 박스를 위해)
		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);

		//생성할 카테고리 이름을 담을 객체
		CategorySaveRequestDto categorySaveRequestDto = new CategorySaveRequestDto();
		model.addAttribute("categorySaveRequestDto", categorySaveRequestDto);

		CategoryModifyRequestDto categoryModifyRequestDto = new CategoryModifyRequestDto();
		model.addAttribute("categoryModifyRequestDto", categoryModifyRequestDto);

		return "admin/categorySetting";
	}

	@LoginCheck
	@ValidAdmin
	@PutMapping
	public String modifyCategory(@CurrentUser User loginUser, @ModelAttribute CategoryModifyRequestDto requestDto,
		Model model) {

		log.error("카테고리 수정 컨트롤러 잘 들어왔어요!!! ::::: " + requestDto.toString());

		categoryService.modifyCategoryName(requestDto);

		//카테고리 리스트 호출 대분류 중분류(셀렉트 박스를 위해)
		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);

		//생성할 카테고리 이름을 담을 객체
		CategorySaveRequestDto categorySaveRequestDto = new CategorySaveRequestDto();
		model.addAttribute("categorySaveRequestDto", categorySaveRequestDto);

		CategoryModifyRequestDto categoryModifyRequestDto = new CategoryModifyRequestDto();
		model.addAttribute("categoryModifyRequestDto", categoryModifyRequestDto);


		return "admin/categorySetting";
	}

	@LoginCheck
	@ValidAdmin
	@PutMapping("/move")
	public String moveChildCategoryAnotherParentCategory(@CurrentUser User loginUser,
		@ModelAttribute CategoryModifyRequestDto requestDto, Model model) {

		log.error(requestDto.toString());

		categoryService.moveChildCategoryAnotherParentCategory(requestDto);

		//카테고리 리스트 호출 대분류 중분류(셀렉트 박스를 위해)
		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);

		//생성할 카테고리 이름을 담을 객체
		CategorySaveRequestDto categorySaveRequestDto = new CategorySaveRequestDto();
		model.addAttribute("categorySaveRequestDto", categorySaveRequestDto);

		CategoryModifyRequestDto categoryModifyRequestDto = new CategoryModifyRequestDto();
		model.addAttribute("categoryModifyRequestDto", categoryModifyRequestDto);

		return "admin/categorySetting";
	}

	@LoginCheck
	@ValidAdmin
	@DeleteMapping
	public @ResponseBody ResponseEntity removeCategory(@CurrentUser User loginUser, @RequestBody CategoryModifyRequestDto requestDto,
		Model model) {

		log.error("카테고리 삭제 " + requestDto.getCategoryId());
		categoryService.removeCategory(requestDto.getCategoryId());

		//카테고리 리스트 호출 대분류 중분류(셀렉트 박스를 위해)
		List<CategoryParentsResponseDto> categoryList = categoryService.findCategoryList();
		model.addAttribute("categoryList", categoryList);

		//생성할 카테고리 이름을 담을 객체
		CategorySaveRequestDto categorySaveRequestDto = new CategorySaveRequestDto();
		model.addAttribute("categorySaveRequestDto", categorySaveRequestDto);

		CategoryModifyRequestDto categoryModifyRequestDto = new CategoryModifyRequestDto();
		model.addAttribute("categoryModifyRequestDto", categoryModifyRequestDto);

		// return "admin/categorySetting";
		return ResponseEntity.ok(categoryList);
	}
}
