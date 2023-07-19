package com.estgames.db.entiity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
@Table(name = "cash_log")
public class UserCash extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cash_log_id", nullable = false, updatable = false)
	private long id;

	@Column(name = "cash_charge_date", nullable = false)
	private LocalDateTime chargeDate;

	@Column(name = "cash_charge_type", nullable = false)
	private String chargeType;

	@Column(name = "cash_charge_amount", nullable = false)
	private int chargeAmount;

	@Column(name = "cash_charge_before")
	private int chargeBeforeCash;

	@Column(name = "cash_charge_after", nullable = false)
	private int chargeAfterCash;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

}
