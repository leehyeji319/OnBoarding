package com.estgames.web.dto.order;

import java.time.LocalDateTime;

import com.estgames.db.entiity.OrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOrderItemDto {

	private Long orderItemId;

	private LocalDateTime orderDate;

	private int orderItemCount;

	private int orderSpentCash;

	private int orderAfterCash;

	private Long itemId;

	private String itemName;

	@JsonProperty("isRemove")
	private boolean isRemove;

	public UserOrderItemDto(OrderItem orderItem) {
		this.orderItemId = orderItem.getId();
		this.orderDate = orderItem.getOrderDate();
		this.orderItemCount = orderItem.getOrderItemCount();
		this.orderSpentCash = orderItem.getOrderSpentCash();
		this.orderAfterCash = orderItem.getOrderAfterCash();
		if (orderItem.getItem().isRemove()) {
			this.itemId = null;
		} else {
			this.itemId = orderItem.getItem().getId();
		}
		if (orderItem.getItem().isRemove()) {
			this.isRemove = true;
		} else {
			this.isRemove = false;
		}

		this.itemName = orderItem.getOrderItemName();
	}

}
