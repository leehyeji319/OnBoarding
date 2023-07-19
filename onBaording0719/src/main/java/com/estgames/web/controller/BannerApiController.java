package com.estgames.web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.estgames.common.annotation.LoginCheck;
import com.estgames.common.annotation.ValidAdmin;
import com.estgames.db.entiity.BannerFileInfo;
import com.estgames.db.entiity.Item;
import com.estgames.db.repsitory.ItemRepository;
import com.estgames.web.dto.item.ItemResponseDto;
import com.estgames.web.service.BannerImgService;
import com.estgames.web.service.ItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/banners")
@Slf4j
public class BannerApiController {

	private final ItemService itemService;
	private final BannerImgService bannerImgService;
	private final ItemRepository itemRepository;

	@LoginCheck
	@ValidAdmin
	@GetMapping("/items/search")
	public ResponseEntity<List<ItemResponseDto>> searchItemsForBanner(
		@RequestParam("keyword") String keyword) {

		List<ItemResponseDto> searchResultList = itemService.findItemWithKeywordAndNotConnectedItem(
			keyword);

		return ResponseEntity.ok(searchResultList);
	}

	@LoginCheck
	@ValidAdmin
	@PostMapping
	public ResponseEntity addBanner(
		@RequestPart("bannerImgFile") MultipartFile file,
		@RequestParam(value = "itemId", required = false) Long itemId) throws Exception {
		log.error("잘 banner api controller 에 들어왓슈...................");
		log.error(file.getOriginalFilename() + " ::::: " + itemId);

		if (itemId != null) {
			BannerFileInfo bannerFileInfo = BannerFileInfo.builder()
				.itemId(itemId)
				.build();
			bannerImgService.saveBannerImg(bannerFileInfo, file);
		} else {
			BannerFileInfo bannerFileInfo = BannerFileInfo.builder()
				.build();
			bannerImgService.saveBannerImg(bannerFileInfo, file);
		}

		return ResponseEntity.ok(HttpStatus.OK);

	}

	@LoginCheck
	@ValidAdmin
	@DeleteMapping("/{bannerId}")
	public String removeBanner(@PathVariable("bannerId") long bannerId) throws Exception {

		bannerImgService.removeBanner(bannerId);

		return "redirect:" + "/admin/main/banners";
	}
}
