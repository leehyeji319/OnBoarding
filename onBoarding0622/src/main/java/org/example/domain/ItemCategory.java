package org.example.domain;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "item_category", uniqueConstraints = @UniqueConstraint(columnNames = {"item_id", "category_id"}))
public class ItemCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_category_id", nullable = false, updatable = false)
    private long id;

//    @Column(name = "item_id")
//    private long itemId;

    @Column(name = "target_category_id", nullable = false)
    private long targetCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder(toBuilder = true)
    public ItemCategory(long targetCategoryId, Category category, Item item) {
        this.targetCategoryId = targetCategoryId;
        this.category = category;
        this.item = item;
    }
}
