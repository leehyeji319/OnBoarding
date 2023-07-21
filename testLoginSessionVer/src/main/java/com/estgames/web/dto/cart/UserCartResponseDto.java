package com.estgames.web.dto.cart;

import com.estgames.db.entiity.CartItem;
import com.estgames.db.entiity.Item;
import com.estgames.db.entiity.User;
import com.estgames.web.dto.item.ItemImgDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCartResponseDto {

	private Long cartItemId;
	private Long userId;
	//itemId가 같이 있어야 상세 조회가 가능하다.
	private Long itemId;
	private String itemImgUrl;
	private String itemName;
	private int itemPrice;
	private int count;
	private ItemImgDto itemImgDto;

	// @Builder
	// public UserCartResponseDto(Long cartItemId, Long userId, Long itemId, String itemImgUrl, String itemName,
	// 	int itemPrice,
	// 	int count) {
	// 	this.cartItemId = cartItemId;
	// 	this.userId = userId;
	// 	this.itemId = itemId;
	// 	this.itemImgUrl = itemImgUrl;
	// 	this.itemName = itemName;
	// 	this.itemPrice = itemPrice;
	// 	this.count = count;
	// }

	public UserCartResponseDto(CartItem cartItem, User user, Item item) {
		this.cartItemId = cartItem.getId();
		this.userId = user.getId();
		this.itemId = item.getId();
		this.itemImgUrl = item.getItemImgUrl();
		this.itemName = item.getItemName();
		this.itemPrice = item.getPrice();
		this.count = cartItem.getCount();
	}

	public UserCartResponseDto(CartItem cartItem) {
		this.cartItemId = cartItem.getId();
		this.count = cartItem.getCount();
	}
}
