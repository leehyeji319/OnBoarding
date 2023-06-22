package org.example.domain;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false, updatable = false)
    private long id;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Builder.Default
    private List<ItemCategory> itemCategoryList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_parent_id", referencedColumnName = "category_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Builder.Default
    private List<Category> child = new ArrayList<>();


    //=== 연관관계 편의 메서드 ===//
    public void addItemCategory(ItemCategory itemCategory) {
        this.itemCategoryList.add(itemCategory);
    }

    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Category(String categoryName, List<ItemCategory> itemCategoryList, Category parent, List<Category> child) {
        this.categoryName = categoryName;
        this.itemCategoryList = itemCategoryList;
        this.parent = parent;
        this.child = child;
    }
}
