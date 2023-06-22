package org.example.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Builder(toBuilder = true)
    public Item(String itemName, int price, String description, String itemImgUrl, boolean isMain) {
        this.itemName = itemName;
        this.price = price;
        this.description = description;
        this.itemImgUrl = itemImgUrl;
        this.isMain = isMain;
    }
}
