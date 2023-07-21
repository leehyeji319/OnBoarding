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

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Table(name = "category")
public class Category implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id", nullable = false, updatable = false)
	private long id;

	@Column(name = "category_name", nullable = false, length = 20)
	private String categoryName;

	@OneToMany(mappedBy = "category")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@Builder.Default
	private List<Item> itemList = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_parent_id", referencedColumnName = "category_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Category parent;

	@OneToMany(mappedBy = "parent")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@Builder.Default
	private List<Category> child = new ArrayList<>();

	//=== 연관관계 편의 메서드 ===//
	public void addItemCategory(Item itemCategory) {
		this.itemList.add(itemCategory);
	}

	public void addChildCategory(Category child) {
		this.child.add(child);
		child.setParent(this);
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Category(String categoryName, List<Item> itemCategoryList, Category parent, List<Category> child) {
		this.categoryName = categoryName;
		this.itemList = itemCategoryList;
		this.parent = parent;
		this.child = child;
	}
}
