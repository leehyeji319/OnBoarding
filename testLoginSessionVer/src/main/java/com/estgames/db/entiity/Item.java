package com.estgames.db.entiity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@Setter
@ToString
public class Item implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id", nullable = false, updatable = false)
	private long id;

	@Column(name = "item_name", nullable = false, length = 20)
	private String itemName;

	@Column(name = "item_price", nullable = false)
	private int price;

	@Column(name = "item_description", nullable = false, length = 1500)
	private String description;

	@Column(name = "item_img_url", length = 30)
	private String itemImgUrl;

	//메인 아이템인지 여부
	@Column(name = "is_main")
	@Builder.Default
	private boolean isMain = false;

	//메인 아이템 - 우선순위 정하기
	@Column(name = "main_item_priority")
	private Integer mainPriorityNumber;

	//메인 아이템일 시, 종인지 횡인지 구분하기
	@Column(name = "main_item_location", length = 10)
	private String mainItemLocation;

	@Column(name = "is_remove")
	@Builder.Default
	private boolean isRemove = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy = "item")
	@Builder.Default
	private List<ItemFileInfo> itemFileInfoList = new ArrayList<>();

	//비즈니스 로직
	public void setCategoryNull() {
		this.category = null;
	}

}
