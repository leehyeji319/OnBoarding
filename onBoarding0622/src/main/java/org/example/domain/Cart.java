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
@Table(name = "cart")
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id", nullable = false, updatable = false)
    private long id;

    @OneToMany(mappedBy = "cart")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Builder.Default
    private List<CartItem> cartItemList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public void addCartItem(CartItem cartItem) {
        this.cartItemList.add(cartItem);
    }

}
