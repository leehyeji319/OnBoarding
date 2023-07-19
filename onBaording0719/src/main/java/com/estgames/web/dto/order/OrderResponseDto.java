package com.estgames.web.dto.order;

import java.time.LocalDateTime;

import com.estgames.db.entiity.Item;
import com.estgames.db.entiity.OrderItem;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderResponseDto {
	private Long orderItemId;
	private LocalDateTime orderDate;
	private Long itemId;
	private String itemName;
	private int itemCount;
	private int orderSpentCash;
	private int orderAfterCash;

	@Builder
	public OrderResponseDto(Long orderItemId, LocalDateTime orderDate, Long itemId, String itemName, int itemCount,
		int orderSpentCash, int orderAfterCash) {
		this.orderItemId = orderItemId;
		this.orderDate = orderDate;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemCount = itemCount;
		this.orderSpentCash = orderSpentCash;
		this.orderAfterCash = orderAfterCash;
	}

	public OrderResponseDto(Item item, OrderItem orderItem) {
		this.orderItemId = orderItem.getId();
		this.orderDate = orderItem.getOrderDate();
		this.itemId = item.getId();
		this.itemName = item.getItemName();
		this.itemCount = orderItem.getOrderItemCount();
		this.orderSpentCash = orderItem.getOrderSpentCash();
		this.orderAfterCash = orderItem.getOrderAfterCash();
	}

}
