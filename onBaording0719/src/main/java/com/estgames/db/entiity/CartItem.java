package com.estgames.db.entiity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "cart_item")
@Getter
@Builder(toBuilder = true)
public class CartItem {

	@Id
	@GeneratedValue
	@Column(name = "cart_item_id", nullable = false, updatable = false)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "cart_id")
	private Cart cart;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Item item;

	@Column(name = "item_count", nullable = false)
	private int count;

	// ==== 비즈니스 로직 ==== //
	//장바구니에 아이템 갯수 추가하는거
	public void addCount(int count) {
		this.count += count;
	}
}
