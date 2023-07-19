package com.estgames.web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.estgames.db.entiity.BannerFileInfo;
import com.estgames.db.repsitory.BannerFileInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BannerImgService {

	@Value("${bannerImgLocation}")
	private String bannerImgLocation;

	private final BannerFileInfoRepository bannerFileInfoRepository;

	private final FileService fileService;

	public void saveBannerImg(BannerFileInfo bannerFileInfo, MultipartFile bannerImgFile) throws Exception {
		String oriImgName = bannerImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";


		//파일 업로드
		if (!StringUtils.isEmpty(oriImgName)) {
			imgName = fileService.uploadFile(bannerImgLocation, oriImgName,
				bannerImgFile.getBytes());
			imgUrl = "/img/banner/" + imgName;
		}

		//상품 이미지 정보 저장
		bannerFileInfo.updateBannerFileInfo(oriImgName, imgName, imgUrl);
		bannerFileInfoRepository.save(bannerFileInfo);

	}

	public void removeBanner(long bannerId) throws Exception {

		BannerFileInfo findBannerFileInfo = bannerFileInfoRepository.findById(bannerId).get();
		fileService.deleteFile(findBannerFileInfo.getSaveFolderPath());
		bannerFileInfoRepository.deleteById(bannerId);

	}
}
