package com.estgames.web.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.estgames.db.entiity.ItemFileInfo;
import com.estgames.db.repsitory.ItemFileInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ItemImgService {

	@Value("${itemImgLocation}")
	private String itemImgLocation;

	private final ItemFileInfoRepository itemFileInfoRepository;

	private final FileService fileService;

	public void saveItemImg(ItemFileInfo itemFileInfo, MultipartFile itemImgFile) throws Exception {
		String oriImgName = itemImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";


		//파일 업로드
		if (!StringUtils.isEmpty(oriImgName)) {
			imgName = fileService.uploadFile(itemImgLocation, oriImgName,
				itemImgFile.getBytes());
			imgUrl = "/img/item/" + imgName;
		}

		//상품 이미지 정보 저장
		itemFileInfo.updateItemFileInfo(oriImgName, imgName, imgUrl);
		itemFileInfoRepository.save(itemFileInfo);

	}

	public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
		if (!itemImgFile.isEmpty()) {
			ItemFileInfo savedItemImg = itemFileInfoRepository.findById(itemImgId)
				.orElseThrow(EntityNotFoundException::new);

			//기존 이미지 파일 삭제
			if (!StringUtils.isEmpty(savedItemImg.getSaveFile())) {
				fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getSaveFile());
			}

			String oriImgName = itemImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			String imgUrl = "/img/item" + imgName;
			savedItemImg.updateItemFileInfo(oriImgName, imgName, imgUrl);
		}
	}
}
