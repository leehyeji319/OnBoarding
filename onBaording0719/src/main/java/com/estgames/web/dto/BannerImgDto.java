package com.estgames.web.dto;

import org.modelmapper.ModelMapper;

import com.estgames.db.entiity.BannerFileInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BannerImgDto {

	private Long id;
	private String itemId;
	private String saveFolderPath;
	private String originalFile;
	private String saveFile;
	private String itemName;
	private String itemCategoryName;

	private static ModelMapper modelMapper = new ModelMapper();

	public static BannerImgDto of(BannerFileInfo bannerFileInfo) {
		return modelMapper.map(bannerFileInfo, BannerImgDto.class);
	}
}
