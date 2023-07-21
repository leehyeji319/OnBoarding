package com.estgames.web.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderRequestDto {

	//총금액
	//회원의 장바구니에 있던것은 들어온 userId로 리스트를 찾아낸 후에 Cart -> OrderItem에 넣은 후 Cart에 있던것은 삭제한다.
	private int amount;
}
