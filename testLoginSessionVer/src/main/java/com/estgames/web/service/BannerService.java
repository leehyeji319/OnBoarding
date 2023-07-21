package com.estgames.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.estgames.db.entiity.BannerFileInfo;
import com.estgames.db.entiity.Item;
import com.estgames.db.repsitory.BannerFileInfoRepository;
import com.estgames.db.repsitory.ItemRepository;
import com.estgames.web.dto.BannerImgDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BannerService {

	private final FileService fileService;
	private final BannerImgService bannerImgService;
	private final BannerFileInfoRepository bannerFileInfoRepository;
	private final ItemRepository itemRepository;

	public void addBanner(MultipartFile bannerImgFile, long itemId) throws Exception {

		BannerFileInfo bannerFileInfo = BannerFileInfo.builder().build();
		bannerFileInfo.setItemId(itemId);

		bannerImgService.saveBannerImg(bannerFileInfo, bannerImgFile);
	}

	public List<BannerImgDto> findBannerList() {
		List<BannerFileInfo> bannerList = bannerFileInfoRepository.findAll();

		List<BannerImgDto> bannerImgDtoList = new ArrayList<>();

		for (BannerFileInfo bannerFileInfo : bannerList) {
			if (bannerFileInfo.getItemId() == null) {
				BannerImgDto bannerImgDto = BannerImgDto.of(bannerFileInfo);
				bannerImgDtoList.add(bannerImgDto);
				continue;
			}
			Item item = itemRepository.findById(bannerFileInfo.getItemId()).orElseThrow(
				() -> new IllegalArgumentException("해당 배너와 연결된 아이템Id로 검색된 아이템이 존재하지 않습니다.")
			);
			//만약 해당 아이템이 삭제되었으면 배너도 같이 삭제됨
			if (item.isRemove()) {
				continue;
			}
			BannerImgDto bannerImgDto = BannerImgDto.of(bannerFileInfo);
			bannerImgDto.setItemName(item.getItemName());
			bannerImgDto.setItemCategoryName(item.getCategory().getParent().getCategoryName() + " > " + item.getCategory().getCategoryName());
			bannerImgDtoList.add(bannerImgDto);
		}

		return bannerImgDtoList;
	}
}
