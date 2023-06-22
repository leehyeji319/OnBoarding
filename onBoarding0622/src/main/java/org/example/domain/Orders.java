package org.example.domain;

import antlr.CommonAST;
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
@Table(name = "orders")
@Builder(toBuilder = true)
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false, updatable = false)
    private long id;

    @OneToMany(mappedBy = "orders")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Builder.Default
    private List<OrderItem> orderItemList = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        this.orderItemList.add(orderItem);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "order_cash_amount", nullable = false)
    private int orderCashAmount;

    @Column(name = "order_cash_before", nullable = false)
    private int orderCashBefore;

    @Column(name = "order_cash_after", nullable = false)
    private int orderCashAfter;

    public Orders(User user, int orderCashAmount, int orderCashBefore, int orderCashAfter) {
        this.user = user;
        this.orderCashAmount = orderCashAmount;
        this.orderCashBefore = orderCashBefore;
        this.orderCashAfter = orderCashAfter;
    }
}
