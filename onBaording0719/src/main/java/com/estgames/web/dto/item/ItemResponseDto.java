package com.estgames.web.dto.item;


import org.modelmapper.ModelMapper;

import com.estgames.db.entiity.Item;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponseDto {

	private Long itemId;
	private String itemName;
	private int itemPrice;
	@JsonProperty("isMain")
	private boolean isMain;
	@JsonProperty("isStar")
	private boolean isStar;
	private String itemDescription;
	private String itemImgUrl;

	private Long firstCategoryId;
	private String firstCategoryName;

	private Long secondCategoryId;
	private String secondCategoryName;

	private ItemImgDto itemImgDto;

	private static ModelMapper modelMapper = new ModelMapper();

	public static ItemResponseDto of(Item item) {
		return modelMapper.map(item, ItemResponseDto.class);
	}

	public ItemResponseDto(Item item) {
		this.itemId = item.getId();
		this.itemName = item.getItemName();
		this.itemPrice = item.getPrice();
		this.itemImgUrl = item.getItemImgUrl();
		this.isMain = item.isMain();
		this.itemDescription = item.getDescription();
		this.firstCategoryId = item.getCategory().getParent().getId();
		this.firstCategoryName = item.getCategory().getParent().getCategoryName();
		this.secondCategoryId = item.getCategory().getId();
		this.secondCategoryName = item.getCategory().getCategoryName();
	}
}
