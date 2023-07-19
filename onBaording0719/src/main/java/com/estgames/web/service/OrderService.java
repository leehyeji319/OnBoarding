package com.estgames.web.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estgames.web.dto.cart.UserCartResponseDto;
import com.estgames.web.dto.order.OrderRequestDto;
import com.estgames.web.dto.order.OrderResponseDto;
import com.estgames.db.entiity.CartItem;
import com.estgames.db.entiity.OrderItem;
import com.estgames.db.entiity.User;
import com.estgames.db.repsitory.CartItemRepository;
import com.estgames.db.repsitory.OrderItemRepository;
import com.estgames.db.repsitory.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

	private final OrderItemRepository orderItemRepository;
	private final UserRepository userRepository;
	private final CartService cartService;
	private final CartItemRepository cartItemRepository;

	public List<OrderResponseDto> findUserOderList(long userId) {
		List<OrderResponseDto> result = new ArrayList<>();
		List<OrderItem> orderItemsByUserId = orderItemRepository.findOrderItemsByUserId(userId);
		for (OrderItem orderItem : orderItemsByUserId) {
			OrderResponseDto responseDto = new OrderResponseDto();
			responseDto.setOrderItemId(orderItem.getId());
			responseDto.setOrderDate(orderItem.getOrderDate());
			responseDto.setItemId(orderItem.getItem().getId());
			responseDto.setItemName(orderItem.getItem().getItemName());
			responseDto.setItemCount(orderItem.getOrderItemCount());
			responseDto.setOrderSpentCash(orderItem.getOrderSpentCash());
			responseDto.setOrderAfterCash(orderItem.getOrderAfterCash());
			result.add(responseDto);
		}
		return result;
	}

	@Transactional
	public void addUserCart(long userId, long itemId, Integer count) {
		//장바구니랑 같은 것.
		cartService.addCartItem(userId, itemId, count);
	}

	@Transactional
	public void addOrderItemPayForGoods(long userId, OrderRequestDto requestDto) {
		//아이템 구매 결제하기
		//user의 cash 소모하기
		User findUser = validateExistUserById(userId);
		int originalCashBeforePurchase = findUser.getCash();

		//TODO: 잔액 부족 예외처리
		if (requestDto.getAmount() > findUser.getCash()) {
			throw new IllegalArgumentException(
				userId + "의 잔액이 부족합니다! 보유 캐시: " + findUser.getCash()
					+ "요청 금액: " + requestDto.getAmount());
		}

		//상품

		//그리고 현재 카발 캐시삽은 사용자의 장바구니에 있는걸 선택할 수 있는 시스템이 없으므로, 모두 삭제한다.
		List<CartItem> cartItemList = cartItemRepository.findByUserId(userId);

		for (CartItem cartItem : cartItemList) {
			//구매가 일어날 시, cart에 있던 아이템 모두 사라짐
			cartItemRepository.deleteById(cartItem.getId());
			//OrderItem에 추가
			int spentCashEachItem = cartItem.getItem().getPrice() * cartItem.getCount();
			//구매한것들 cash에서 빼주기 -> 사용자가 현재 가지고 있는 돈을 하나씩 살때마다 기록해주기
			findUser.useCash(spentCashEachItem);
			orderItemRepository.save(OrderItem.builder()
				.orderDate(LocalDateTime.now())
				.orderItemCount(cartItem.getCount())
				.orderSpentCash(spentCashEachItem)
				.orderAfterCash(findUser.getCash())
				.item(cartItem.getItem())
				.user(findUser)
				.build());
		}

		if ((originalCashBeforePurchase - requestDto.getAmount()) != findUser.getCash()) {
			throw new IllegalArgumentException("뭔가 계산이 안맞아요!! "
				+ "총 사용 캐시 : " + requestDto.getAmount()
				+ ", 계산 전 사용자 캐시 : " + originalCashBeforePurchase
				+ ", 계산하고 남아야하는 캐시 : " + (originalCashBeforePurchase - requestDto.getAmount())
				+ ", 하지만 user에게 현재 남은 캐시 : " + findUser.getCash());
		}
	}

	public User validateExistUserById(long userId) {
		return userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 id의 user가 존재하지 않습니다. userId : " + userId)
		);
	}
}
