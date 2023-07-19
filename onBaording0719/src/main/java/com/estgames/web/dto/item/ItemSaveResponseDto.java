package com.estgames.web.dto.item;

import com.estgames.db.entiity.Item;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemSaveResponseDto {

	private Long itemId;
	private String itemName;
	private int itemPrice;
	private String itemDescription;
	private boolean isMain;
	private String itemImgUrl;
	private Long firstCategoryId;
	private String firstCategoryName;
	private Long secondCategoryId;
	private String secondCategoryName;

	@Builder
	public ItemSaveResponseDto(Long itemId, String itemName, int itemPrice, String itemDescription, boolean isMain,
		String itemImgUrl, Long firstCategoryId, String firstCategoryName, Long secondCategoryId,
		String secondCategoryName) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemPrice = itemPrice;
		this.itemDescription = itemDescription;
		this.isMain = isMain;
		this.itemImgUrl = itemImgUrl;
		this.firstCategoryId = firstCategoryId;
		this.firstCategoryName = firstCategoryName;
		this.secondCategoryId = secondCategoryId;
		this.secondCategoryName = secondCategoryName;
	}

	public ItemSaveResponseDto(Item item) {
		this.itemId = item.getId();
		this.itemName = item.getItemName();
		this.itemPrice = item.getPrice();
		this.itemDescription = item.getDescription();
		this.isMain = item.isMain();
		this.itemImgUrl = item.getItemImgUrl();
		this.firstCategoryId = item.getCategory().getParent().getId();
		this.firstCategoryName = item.getCategory().getParent().getCategoryName();
		this.secondCategoryId = item.getCategory().getId();
		this.secondCategoryName = item.getCategory().getCategoryName();
	}

}
