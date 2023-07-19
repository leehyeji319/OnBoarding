package com.estgames.web.dto.item;


import com.estgames.db.entiity.Category;
import com.estgames.db.entiity.Item;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// @RequiredArgsConstructor
public class ItemSaveRequestDto {

	private String itemName;
	private String itemPrice;
	private String itemDescription;
	private boolean isMain = false;
	private Long categoryId;
	private String itemImgUrl;

	private Category category;

	@Builder
	public ItemSaveRequestDto(String itemName, String itemPrice, String itemDescription, Long categoryId,
		String itemImgUrl, Category category) {
		this.itemName = itemName;
		this.itemPrice = itemPrice;
		this.itemDescription = itemDescription;
		this.isMain = false;
		this.categoryId = categoryId;
		this.itemImgUrl = itemImgUrl;
		this.category = category;
	}

	public Item toEntity(Category category) {
		return Item.builder()
			.itemName(this.itemName)
			.price(Integer.parseInt(this.itemPrice.replaceAll(",", "")))
			.description(this.itemDescription)
			.isMain(this.isMain)
			.category(category)
			.itemImgUrl(this.itemImgUrl)
			.build();
	}

}
