package com.estgames.web.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estgames.db.entiity.OrderItem;
import com.estgames.db.repsitory.OrderItemRepository;
import com.estgames.web.dto.cart.CartOrderDto;
import com.estgames.web.dto.cart.UserCartResponseDto;
import com.estgames.db.entiity.Cart;
import com.estgames.db.entiity.CartItem;
import com.estgames.db.entiity.Item;
import com.estgames.db.entiity.User;
import com.estgames.db.repsitory.CartItemRepository;
import com.estgames.db.repsitory.CartRepository;
import com.estgames.db.repsitory.ItemRepository;
import com.estgames.db.repsitory.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;
	private final OrderItemRepository orderItemRepository;
	private final static int MAX_ITEM_COUNT = 20;

	@Transactional
	public List<UserCartResponseDto> findUserCartItemList(long userId) {

		//만약 유저에게 카트가 없다면, 카트를 생성해줌.
		Optional<Cart> findCart = cartRepository.findCartByUserId(userId);
		User findUser = validateExistUserById(userId);

		if (findCart.isEmpty()) {
			cartRepository.save(Cart.builder()
				.user(findUser)
				.build());
		}

		List<CartItem> userCartItemList = cartItemRepository.findByUserId(userId);

		//만약 유저아이디로 찾아온 cartItemList가 null 이라면, 아직 장바구니에 아무것도 담지 않은것. 그러므로 return null해준다.
		if (userCartItemList == null) {
			return null;
		}

		List<UserCartResponseDto> result = new ArrayList<>();

		for (CartItem findCartItem : userCartItemList) {
			// User findUserByCartItem = cartItemRepository.findUserByCartItemId(findCartItem.getId());
			Item findItem = cartItemRepository.findItemByCartItemId(findCartItem.getId());
			UserCartResponseDto responseDto = new UserCartResponseDto(findCartItem, findUser, findItem);
			result.add(responseDto);
		}
		return result;
	}

	//유저가 생성될 때, 카트도 자동 생성되는걸로 해야한다. -> 회원가입을 하면 유저는 기본 cart를 한개만 가지고 있다.

	@Transactional
	public void addCartItem(long userId, long itemId, int count) {

		log.error("장바구니에 들어가는 중............. userId: " + userId + " itemId: " + itemId);

		// if (count == null) {
		// 	count = 1;
		// }

		//TODO: 장바구니에 이미 상품 종류가 20개라면 더이상 담을 수 없다. -> 아니라고 피드백
		// List<CartItem> findCartItemList = cartItemRepository.findByUserId(userId);
		// if (findCartItemList.size() >= MAX_ITEM_COUNT) {
		// 	throw new IllegalArgumentException("이미 장바구니에 20개의 상품이 존재합니다. 장바구니를 비워주세요.");
		// }

		//이미 장바구니에 있는 물품인지 확인
		Optional<CartItem> findCartItem = cartItemRepository.findByUserIdAndItemId(userId, itemId);

		if (findCartItem.isPresent()) {
			throw new IllegalArgumentException("이미 장바구니에 추가된 상품입니다.");
		}

		User findUser = validateExistUserById(userId);
		Item findItem = validateExistItemById(itemId);
		// Cart findCart = validateExistCartByUserId(userId);
		Optional<Cart> findCart = cartRepository.findCartByUserId(userId);
		if (findCart.isEmpty()) {
			Cart savedCart = cartRepository.save(Cart.builder()
				.user(findUser)
				.build());

			cartItemRepository.save(CartItem.builder()
				.cart(savedCart)
				.item(findItem)
				.count(count)
				.build());
		} else {

			cartItemRepository.save(CartItem.builder()
				.cart(findCart.get())
				.item(findItem)
				.count(count)
				.build());
		}

		// if (findCartItem.isPresent()) {
		// 	// 이미 장바구니에 있는 물건이라면, count 개수를 ++ 해준다.
		// 	findCartItem.get().addCount(count);
		// } else {
		// 	//만약 장바구니에 없는 물건이라면 CartItem 생성
		//
		// 	//검증
		// 	User findUser = validateExistUserById(userId);
		// 	Item findItem = validateExistItemById(itemId);
		// 	Cart findCart = validateExistCartByUserId(userId);
		//
		// 	cartItemRepository.save(CartItem.builder()
		// 		.cart(findCart)
		// 		.item(findItem)
		// 		.count(count)
		// 		.build());
		// }

	}

	@Transactional
	public long modifyCartItem(long userId, long itemId, Integer count) {
		validateExistUserById(userId);
		validateExistItemById(itemId);
		CartItem findCartItem = validateExistCartItemByUserIdAndItemId(userId, itemId);
		CartItem savedCartItem = cartItemRepository.save(findCartItem.toBuilder()
			.id(findCartItem.getId())
			.count(count)
			.build());

		return savedCartItem.getId();
	}

	//장바구니 내에서는 itemId를 알수가 없지 않나..?
	@Transactional
	public void removeCartItem(Long cartItemId) {
		cartItemRepository.deleteById(cartItemId);

	}

	//수량을 어디에 받아올건데..
	public User validateExistUserById(long userId) {
		return userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 id의 user가 존재하지 않습니다. userId : " + userId)
		);
	}

	public Item validateExistItemById(long itemId) {
		return itemRepository.findById(itemId).orElseThrow(
			() -> new IllegalArgumentException("해당 id의 item이 존재하지 않습니다. itemId : " + itemId)
		);
	}

	public Cart validateExistCartByUserId(long userId) {
		return cartRepository.findCartByUserId(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 userId로 찾은 Cart가 존재하지 않습니다. usreId : " + userId)
		);
	}

	public CartItem validateExistCartItemByUserIdAndItemId(long userId, long itemId) {
		return cartItemRepository.findByUserIdAndItemId(userId, itemId).orElseThrow(
			() -> new IllegalArgumentException(
				"해당 userId와 itemId로 일치하는 CartItem이 존재하지 않습니다. userId : " + userId + ", itemId : " + itemId)
		);
	}

	@Transactional
	public void modifyCartItemCount(long cartItemId, int count) {
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
			() -> new IllegalArgumentException("해당 id의 cartItem이 존재하지 않습니다. cartItem : " + cartItemId)
		);

		cartItemRepository.save(
			cartItem.toBuilder()
				.count(count)
				.build()
		);

	}

	@Transactional
	public void orderCartItem(List<CartOrderDto> cartOrderDtoList, long userId) {

		User user = userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 id로 조회한 user가 존재하지 않습니다. userId : " + userId)
		);

		int totalPrice = 0;
		for (CartOrderDto cartOrderDto : cartOrderDtoList) {
			CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(
				() -> new IllegalArgumentException("해당 id로 조회한 cart item이 존재하지 않습니다.")
			);
			totalPrice += cartItem.getItem().getPrice() * cartItem.getCount();
		}

		if (totalPrice > user.getCash()) {
			throw new IllegalArgumentException("주문 금액이 유저의 보유 금액보다 큽니다.");
		}

		for (CartOrderDto cartOrderDto : cartOrderDtoList) {
			CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(
				() -> new IllegalArgumentException("해당 id로 조회한 cart item이 존재하지 않습니다.")
			);

			Item item = cartItem.getItem();
			// User user = cartItem.getCart().getUser();

			int eachCartItemPrice = cartItem.getCount() * cartItem.getItem().getPrice();


			orderItemRepository.save(
				OrderItem.builder()
					.orderDate(LocalDateTime.now())
					.orderItemCount(cartItem.getCount())
					.orderSpentCash(eachCartItemPrice)
					.orderAfterCash(user.getCash() - eachCartItemPrice)
					.user(user)
					.item(item)
					.build());

			user.useCash(eachCartItemPrice);

			//구매가 완료되었으면, 장바구니에서 구매한 물품을 삭제한다.
			cartItemRepository.deleteById(cartItem.getId());

		}


	}
}
