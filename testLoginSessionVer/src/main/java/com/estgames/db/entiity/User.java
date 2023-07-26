package com.estgames.db.entiity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user")
@Builder(toBuilder = true)
@ToString
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false, updatable = false)
	private long id;

	@Column(name = "user_loginId", nullable = false, updatable = false, length = 50)
	private String loginId;

	@Column(name = "user_password", nullable = false, length = 50)
	private String password;

	@Column(name = "user_name", nullable = false, length = 20)
	private String name;

	@Column(name = "user_cash")
	@Builder.Default
	private int cash = 0;

	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@Builder.Default
	private List<Star> starList = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@Builder.Default
	private List<UserCash> userCashList = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@Builder.Default
	private List<OrderItem> orderItemList = new ArrayList<>();

	//== 연관관계 메서드 ==//

	public void addCashLog(UserCash userCash) {
		this.userCashList.add(userCash);
	}

	public void addLike(Star star) {
		this.starList.add(star);
	}

	//== 비즈니스 로직 ==//
	public void chargeCash(int amount) {
		this.cash += amount;
	}

	public void useCash(int amount) {
		this.cash -= amount;
	}

	public void setId(long id) {
		this.id = id;
	}
}
