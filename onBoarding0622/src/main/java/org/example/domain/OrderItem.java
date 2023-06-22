package org.example.domain;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.criterion.Order;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "order_item")
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id", nullable = false, updatable = false)
    private long id;

    @Column(name = "order_type", nullable = false)
    private String orderType;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "order_item_count", nullable = false)
    private int orderItemCount;

    @Column(name = "order_spent_cash", nullable = false)
    private int orderSpentCash;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder(toBuilder = true)
    public OrderItem(String orderType, LocalDateTime orderDate, int orderItemCount, int orderSpentCash, Orders orders, Item item) {
        this.orderType = orderType;
        this.orderDate = orderDate;
        this.orderItemCount = orderItemCount;
        this.orderSpentCash = orderSpentCash;
        this.orders = orders;
        this.item = item;
    }
}
