package com.estgames.web.dto.item;

import org.modelmapper.ModelMapper;

import com.estgames.db.entiity.ItemFileInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemImgDto {

	private Long id;
	private String saveFile;
	private String originalFile;
	private String saveFolderPath;

	private static ModelMapper modelMapper = new ModelMapper();

	public static ItemImgDto of(ItemFileInfo itemFileInfo) {
		return modelMapper.map(itemFileInfo, ItemImgDto.class);
	}
}
