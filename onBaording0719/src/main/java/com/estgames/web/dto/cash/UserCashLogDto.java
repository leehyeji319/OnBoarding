package com.estgames.web.dto.cash;

import java.time.LocalDateTime;

import com.estgames.db.entiity.User;
import com.estgames.db.entiity.UserCash;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCashLogDto {
	private Long cashId;

	private LocalDateTime chargeDate;

	private String chargeType;

	private int chargeAmount;
	private int chargeBeforeCash;
	private int chargeAfterCash;

	@Builder
	public UserCashLogDto(Long cashId, LocalDateTime chargeDate, String chargeType, int chargeAmount,
		int chargeBeforeCash,
		int chargeAfterCash) {
		this.cashId = cashId;
		this.chargeDate = chargeDate;
		this.chargeType = chargeType;
		this.chargeAmount = chargeAmount;
		this.chargeBeforeCash = chargeBeforeCash;
		this.chargeAfterCash = chargeAfterCash;
	}

	public UserCashLogDto(UserCash userCash) {
		this.cashId = userCash.getId();
		this.chargeDate = userCash.getChargeDate();
		this.chargeType = userCash.getChargeType();
		this.chargeAmount = userCash.getChargeAmount();
		this.chargeBeforeCash = userCash.getChargeBeforeCash();
		this.chargeAfterCash = userCash.getChargeAfterCash();
	}

	public UserCash toEntity(User user) {
		return UserCash.builder()
			.chargeDate(LocalDateTime.now())
			.chargeType(this.chargeType)
			.chargeAmount(this.chargeAmount)
			.chargeAfterCash(this.chargeAfterCash)
			.chargeBeforeCash(this.chargeBeforeCash)
			.user(user)
			.build();
	}

}
