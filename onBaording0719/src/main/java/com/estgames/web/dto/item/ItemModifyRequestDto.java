package com.estgames.web.dto.item;

import com.estgames.db.entiity.Category;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemModifyRequestDto {

	private String itemName;
	private String itemPrice;
	private String itemDescription;
	private boolean isMain;
	private String categoryChildName;
	private String itemImgUrl;

	// private Category category;

	@Builder
	public ItemModifyRequestDto(String itemName, String itemPrice, String itemDescription, boolean isMain, String categoryChildName,
		String itemImgUrl) {
		this.itemName = itemName;
		this.itemPrice = itemPrice;
		this.itemDescription = itemDescription;
		this.isMain = isMain;
		this.categoryChildName = categoryChildName;
		this.itemImgUrl = itemImgUrl;
	}

	// @Builder
	// public ItemModifyRequestDto(String itemName, int itemPrice, String itemDescription, boolean isMain, Long categoryId,
	// 	String itemImgUrl, Category category) {
	// 	this.itemName = itemName;
	// 	this.itemPrice = itemPrice;
	// 	this.itemDescription = itemDescription;
	// 	this.isMain = isMain;
	// 	this.categoryId = categoryId;
	// 	this.itemImgUrl = itemImgUrl;
	// 	this.category = category;
	// }

}
