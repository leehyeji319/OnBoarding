package org.example.domain;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "user")
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private long id;

    @Column(name = "user_email", nullable = false)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_cash")
    private int cash;

    @OneToMany
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Builder.Default
    private List<ItemStar> itemStarList = new ArrayList<>();

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "cart_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Cart cart;

    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Builder.Default
    private List<CashLog> cashLogList = new ArrayList<>();

    //== 연관관계 메서드 ==//

    public void addCashLog(CashLog cashLog) {
        this.cashLogList.add(cashLog);
    }

    public void addLike(ItemStar itemStar) {
        this.itemStarList.add(itemStar);
    }

    public User(String email, String password, int cash, List<ItemStar> itemStarList, List<CashLog> cashLogList) {
        this.email = email;
        this.password = password;
        this.cash = cash;
        this.itemStarList = itemStarList;
        this.cashLogList = cashLogList;
    }

}
