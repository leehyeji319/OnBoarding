package com.estgames.db.entiity;

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
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private long id;

	@Column(name = "item_name")
	private String itemName;

	@Column(name = "item_price")
	private int price;

	@Column(name = "item_description")
	private String description;

	@Column(name = "item_img_url")
	private String itemImgUrl;

	//메인 아이템인지 여부
	@Column(name = "is_main")
	private boolean isMain;

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

}
