package com.estgames.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estgames.web.dto.item.ItemResponseDto;
import com.estgames.web.dto.star.StarResponseDto;
import com.estgames.db.entiity.Item;
import com.estgames.db.entiity.Star;
import com.estgames.db.entiity.User;
import com.estgames.db.repsitory.ItemRepository;
import com.estgames.db.repsitory.StarRepository;
import com.estgames.db.repsitory.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StarService {

	private final StarRepository starRepository;
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;

	@Transactional
	public StarResponseDto addStar(long userId, long itemId) {
		User findUser = validateExistUserById(userId);
		Item findItem = validateExistItemById(itemId);
		Star saveStar = starRepository.save(Star.builder()
			.item(findItem)
			.user(findUser)
			.build());

		return new StarResponseDto(saveStar);
	}

	@Transactional
	public long removeStar(long userId, long itemId) {
		validateExistUserById(userId);
		validateExistItemById(itemId);

		Star findStar = validateExistStarByUserIdAndItemId(userId, itemId);
		starRepository.deleteById(findStar.getId());

		return findStar.getId();
	}

	public List<ItemResponseDto> findStarListWithUserId(long userId) {
		validateExistUserById(userId);
		List<ItemResponseDto> result = new ArrayList<>();

		//user의 좋아요 목록 들고오기.
		List<Star> starsByUserId = starRepository.findStarsByUserIdAndItemIsNotRemoved(userId);

		List<Item> itemList = toItems(starsByUserId);

		for (Item item : itemList) {
			ItemResponseDto responseDto = ItemResponseDto.builder()
				.itemId(item.getId())
				.itemName(item.getItemName())
				.itemPrice(item.getPrice())
				.isStar(isExistStarByUserIdAndItemId(userId, item.getId()))
				.firstCategoryId(item.getCategory().getParent().getId())
				.firstCategoryName(item.getCategory().getParent().getCategoryName())
				.secondCategoryId(item.getCategory().getId())
				.secondCategoryName(item.getCategory().getCategoryName())
				.build();

			result.add(responseDto);
		}


		return result;

	}

	private List<Long> toItemIds(List<Star> result) {
		return result.stream()
			.map(i -> i.getItem().getId())
			.collect(Collectors.toList());
	}

	private List<Item> toItems(List<Star> result) {
		return result.stream()
			.map(Star::getItem)
			.collect(Collectors.toList());
	}

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

	public Star validateExistStarByUserIdAndItemId(long userId, long itemId) {
		return starRepository.findStarByUserIdAndItemId(userId, itemId).orElseThrow(
			() -> new IllegalArgumentException(
				"해당 userId와 itemId에 일치하는 좋아요 정보가 존재하지 않습니다. userId : " + userId + ", itemId : " + itemId)
		);
	}

	public boolean isExistStarByUserIdAndItemId(Long userId, Long itemId) {
		Optional<Star> star = starRepository.findStarByUserIdAndItemId(userId, itemId);
		return star.isPresent();
	}

}
