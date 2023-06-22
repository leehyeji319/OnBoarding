package org.example.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "item_star", uniqueConstraints = @UniqueConstraint(columnNames = {"item_id", "user_id"}))
public class ItemStar {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_star_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder(toBuilder = true)
    public ItemStar(Item item, User user) {
        this.item = item;
        this.user = user;
    }
}
