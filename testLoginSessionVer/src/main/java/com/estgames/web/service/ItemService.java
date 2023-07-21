package com.estgames.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.estgames.db.entiity.BannerFileInfo;
import com.estgames.db.entiity.CartItem;
import com.estgames.db.entiity.ItemFileInfo;
import com.estgames.db.repsitory.BannerFileInfoRepository;
import com.estgames.db.repsitory.CartItemRepository;
import com.estgames.db.repsitory.CartRepository;
import com.estgames.db.repsitory.ItemFileInfoRepository;
import com.estgames.web.dto.item.ItemImgDto;
import com.estgames.web.dto.item.ItemModifyRequestDto;
import com.estgames.web.dto.item.ItemResponseDto;
import com.estgames.web.dto.item.ItemSaveRequestDto;
import com.estgames.web.dto.item.ItemSaveResponseDto;
import com.estgames.db.entiity.Category;
import com.estgames.db.entiity.Item;
import com.estgames.db.entiity.Star;
import com.estgames.db.entiity.User;
import com.estgames.db.repsitory.CategoryRepository;
import com.estgames.db.repsitory.ItemRepository;
import com.estgames.db.repsitory.StarRepository;
import com.estgames.db.repsitory.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ItemService {

	private final ItemRepository itemRepository;
	private final StarRepository starRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final ItemImgService itemImgService;
	private final ItemFileInfoRepository itemFileInfoRepository;
	private final FileService fileService;
	private final BannerFileInfoRepository bannerFileInfoRepository;
	private final CartItemRepository cartItemRepository;
	private static final String REMOVE_ITEM = "_Remove_";

	// 메인 페이지에 뜰 아이템들 가져오기
	public List<ItemResponseDto> findMainItemList() {

		List<ItemResponseDto> result = new ArrayList<>();

		List<Item> mainItemList = itemRepository.findItemsByMainIsTrueAAndRemoveIsFalse();
		log.error("mainItemList Size는!!!? :" + mainItemList.size());
		for (Item item : mainItemList) {
			Optional<ItemFileInfo> itemImg = itemFileInfoRepository.findByItemId(item.getId());
			ItemImgDto itemImgDto = null;
			if (itemImg.isPresent()) {
				itemImgDto = ItemImgDto.of(itemImg.get());
			}
			ItemResponseDto responseDto = ItemResponseDto.builder()
				.itemId(item.getId())
				.itemName(item.getItemName())
				.itemPrice(item.getPrice())
				.isMain(true)
				// .isStar(isExistStarByUserIdAndItemId(userId, item.getId()))
				.firstCategoryId(item.getCategory().getParent().getId())
				.firstCategoryName(item.getCategory().getParent().getCategoryName())
				.secondCategoryId(item.getCategory().getId())
				.secondCategoryName(item.getCategory().getCategoryName())
				.itemImgDto(itemImgDto)
				.build();

			result.add(responseDto);
		}

		return result;

	}

	public List<ItemResponseDto> findMainItemListWithUser(long userId) {
		validateExistUserById(userId);

		List<ItemResponseDto> result = new ArrayList<>();

		List<Item> mainItemList = itemRepository.findItemsByMainIsTrueAAndRemoveIsFalse();
		for (Item item : mainItemList) {
			Optional<ItemFileInfo> itemImg = itemFileInfoRepository.findByItemId(item.getId());
			ItemImgDto itemImgDto = null;
			if (itemImg.isPresent()) {
				itemImgDto = ItemImgDto.of(itemImg.get());
			}

			ItemResponseDto responseDto = ItemResponseDto.builder()
				.itemId(item.getId())
				.itemName(item.getItemName())
				.itemPrice(item.getPrice())
				.isMain(item.isMain())
				.isStar(isExistStarByUserIdAndItemId(userId, item.getId()))
				.firstCategoryId(item.getCategory().getParent().getId())
				.firstCategoryName(item.getCategory().getParent().getCategoryName())
				.secondCategoryId(item.getCategory().getId())
				.secondCategoryName(item.getCategory().getCategoryName())
				.itemImgDto(itemImgDto)
				.build();

			result.add(responseDto);
		}

		return result;

	}

	public List<ItemResponseDto> findItemList() {
		// List<Item> list = itemRepository.findAll();
		List<Item> list_ = itemRepository.findAllByRemoveIsFalse();

		List<ItemResponseDto> collect = list_.stream()
			.map(ItemResponseDto::new)
			.collect(Collectors.toList());

		for (ItemResponseDto itemResponseDto : collect) {
			Optional<ItemFileInfo> itemImg = itemFileInfoRepository.findByItemId(itemResponseDto.getItemId());
			ItemImgDto itemImgDto = null;
			if (itemImg.isPresent()) {
				itemImgDto = ItemImgDto.of(itemImg.get());
			}
			itemResponseDto.setItemImgDto(itemImgDto);
		}
		return collect;
	}

	public List<ItemResponseDto> findItemList(long userId) {
		List<Item> list = itemRepository.findAll();

		//star 표시 해줘야함
		List<ItemResponseDto> result = list.stream()
			.map(ItemResponseDto::new)
			.collect(Collectors.toList());

		result.forEach(i -> i.setStar(
			isExistStarByUserIdAndItemId(userId, i.getItemId())
		));

		//category 표시 해줘야함
		//stream foreach 비효율적인가? 고민된다.
		// TODO: 여기 리팩토링 필요. 포문을 한번에 돌면서 넣어주는게 백배 낫다 ..........
		result.forEach(i -> i.setFirstCategoryId((Long)findCategoryInfo(i.getItemId()).get(0)));
		result.forEach(i -> i.setFirstCategoryName((String)findCategoryInfo(i.getItemId()).get(1)));
		result.forEach(i -> i.setSecondCategoryId((Long)findCategoryInfo(i.getItemId()).get(2)));
		result.forEach(i -> i.setSecondCategoryName((String)findCategoryInfo(i.getItemId()).get(3)));

		return result;
	}

	//카테고리별 아이템 조회 (admin에서, user에선 별까지 보여야함)
	public List<ItemResponseDto> findItemListWithCategoryId(long categoryId) {
		List<ItemResponseDto> result = new ArrayList<>();

		Category findCategory = categoryRepository.findById(categoryId).orElseThrow(
			() -> new IllegalArgumentException("해당 카테고리 id의 카테고리가 존재하지 않습니다. categoryId : " + categoryId)
		);

		//if 카테고리가 부모이면, 해당 자식 카테고리들까지 다 들고와야함
		if (findCategory.getParent() == null) {
			List<Item> itemListInParent = itemRepository.findAllByCategoryChildWithParentId(categoryId);
			for (Item item : itemListInParent) {
				Optional<ItemFileInfo> itemImg = itemFileInfoRepository.findByItemId(item.getId());
				ItemImgDto itemImgDto = null;
				if (itemImg.isPresent()) {
					itemImgDto = ItemImgDto.of(itemImg.get());
				}
				ItemResponseDto responseDto = ItemResponseDto.builder()
					.itemId(item.getId())
					.itemName(item.getItemName())
					.itemPrice(item.getPrice())
					.firstCategoryId(item.getCategory().getParent().getId())
					.firstCategoryName(item.getCategory().getParent().getCategoryName())
					.secondCategoryId(item.getCategory().getId())
					.secondCategoryName(item.getCategory().getCategoryName())
					.itemImgDto(itemImgDto)
					.build();

				result.add(responseDto);
			}

			return result;
		}

		//else 카테고리가 자식이면, 자식 카테고리만 들고와야함

		List<Item> itemListInChild = itemRepository.findAllByCategoryId(categoryId);
		for (Item item : itemListInChild) {
			Optional<ItemFileInfo> itemImg = itemFileInfoRepository.findByItemId(item.getId());
			ItemImgDto itemImgDto = null;
			if (itemImg.isPresent()) {
				itemImgDto = ItemImgDto.of(itemImg.get());
			}
			ItemResponseDto responseDto = ItemResponseDto.builder()
				.itemId(item.getId())
				.itemName(item.getItemName())
				.itemPrice(item.getPrice())
				.firstCategoryId(item.getCategory().getParent().getId())
				.firstCategoryName(item.getCategory().getParent().getCategoryName())
				.secondCategoryId(item.getCategory().getId())
				.secondCategoryName(item.getCategory().getCategoryName())
				.itemImgDto(itemImgDto)
				.build();

			result.add(responseDto);
		}

		List<Item> itemList = itemRepository.findAllByCategoryId(categoryId);

		return result;
	}

	public List<?> findCategoryInfo(long itemId) {

		List<Object> categoryInfos = new ArrayList<>();

		Item findItem = validateExistItemById(itemId);

		categoryInfos.add(findItem.getCategory().getParent().getId());
		categoryInfos.add(findItem.getCategory().getParent().getCategoryName());
		categoryInfos.add(findItem.getCategory().getId());
		categoryInfos.add(findItem.getCategory().getCategoryName());

		return categoryInfos;
	}

	public boolean isExistStarByUserIdAndItemId(Long userId, Long itemId) {
		Optional<Star> star = starRepository.findStarByUserIdAndItemId(userId, itemId);
		return star.isPresent();
	}

	public List<Long> toItemIds(List<ItemResponseDto> result) {
		return result.stream()
			.map(ItemResponseDto::getItemId)
			.collect(Collectors.toList());
	}

	public ItemResponseDto findItem(long itemId) {
		Item findItem = validateExistItemById(itemId);

		Optional<ItemFileInfo> itemImg = itemFileInfoRepository.findByItemId(itemId);
		ItemImgDto itemImgDto = null;
		if (itemImg.isPresent()) {
			itemImgDto = ItemImgDto.of(itemImg.get());
		}

		if (!findItem.isRemove()) {
			return ItemResponseDto.builder()
				.itemId(itemId)
				.itemName(findItem.getItemName())
				.itemPrice(findItem.getPrice())
				.itemImgUrl(findItem.getItemImgUrl())
				.isMain(findItem.isMain())
				.itemDescription(findItem.getDescription())
				.firstCategoryId(findItem.getCategory().getParent().getId())
				.firstCategoryName(findItem.getCategory().getParent().getCategoryName())
				.secondCategoryId(findItem.getCategory().getId())
				.secondCategoryName(findItem.getCategory().getCategoryName())
				.isStar(false)
				.itemImgDto(itemImgDto)
				.build();
		} else {
			if (findItem.getCategory() == null) {
				return ItemResponseDto.builder()
					.itemId(itemId)
					.itemName(findItem.getItemName())
					.itemPrice(findItem.getPrice())
					.itemImgUrl(findItem.getItemImgUrl())
					.isMain(findItem.isMain())
					.itemDescription(findItem.getDescription())
					.firstCategoryId(null)
					.firstCategoryName("삭제된 카테고리")
					.secondCategoryId(null)
					.secondCategoryName("")
					.isStar(false)
					.itemImgDto(itemImgDto)
					.build();
			} else {
				return ItemResponseDto.builder()
					.itemId(itemId)
					.itemName(findItem.getItemName())
					.itemPrice(findItem.getPrice())
					.itemImgUrl(findItem.getItemImgUrl())
					.isMain(findItem.isMain())
					.itemDescription(findItem.getDescription())
					.firstCategoryId(findItem.getCategory().getParent().getId())
					.firstCategoryName(findItem.getCategory().getParent().getCategoryName())
					.secondCategoryId(findItem.getCategory().getId())
					.secondCategoryName(findItem.getCategory().getCategoryName())
					.isStar(false)
					.itemImgDto(itemImgDto)
					.build();
			}

		}

	}

	//카테고리 추가해야함
	public ItemResponseDto findItemWithUser(long itemId, long userId) {
		User findUser = validateExistUserById(userId);
		Item findItem = validateExistItemById(itemId);
		Optional<ItemFileInfo> itemImg = itemFileInfoRepository.findByItemId(itemId);
		ItemImgDto itemImgDto = null;
		if (itemImg.isPresent()) {
			itemImgDto = ItemImgDto.of(itemImg.get());
		}

		return ItemResponseDto.builder()
			.itemId(findItem.getId())
			.itemName(findItem.getItemName())
			.itemPrice(findItem.getPrice())
			.itemDescription(findItem.getDescription())
			.isMain(findItem.isMain())
			.isStar(isExistStarByUserIdAndItemId(userId, itemId))
			.firstCategoryId(findItem.getCategory().getParent().getId())
			.firstCategoryName(findItem.getCategory().getParent().getCategoryName())
			.secondCategoryId(findItem.getCategory().getId())
			.secondCategoryName(findItem.getCategory().getCategoryName())
			.itemImgDto(itemImgDto)
			.build();

	}

	@Transactional
	public ItemSaveResponseDto addItem(ItemSaveRequestDto requestDto,
		MultipartFile itemImgFile) throws Exception {

		log.error("요청된 아이템 생성 정보 사항  :::::::::::::::::::  " + requestDto.toString());

		//중복되는 아이템 이름 검증
		validateDuplicateItemName(requestDto.getItemName());

		Category findCategory = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(
			() -> new IllegalArgumentException(
				"해당 아이템을 넣을 카테고리 id가 존재하지 않습니다. categoryId : " + requestDto.getCategoryId())
		);

		//카테고리가 부모 카테고리면 안됨
		if (findCategory.getParent() == null) {
			throw new IllegalArgumentException("대분류에는 아이템을 생성할 수 없습니다!");
		}

		//카테고리도 같이 받아와야함
		Item savedItem = itemRepository.save(Item.builder()
			.itemName(requestDto.getItemName())
			.price(Integer.parseInt(requestDto.getItemPrice().replaceAll(",", "")))
			.description(requestDto.getItemDescription())
			.isMain(requestDto.isMain())
			// .itemImgUrl(requestDto.getItemImgUrl())
			.category(findCategory)
			.build());

		//이미지 등록
		ItemFileInfo itemFileInfo = ItemFileInfo.builder()
			.item(savedItem)
			.build();

		itemImgService.saveItemImg(itemFileInfo, itemImgFile);

		return new ItemSaveResponseDto(savedItem);
	}

	@Transactional
	public ItemResponseDto modifyItem(long itemId, ItemModifyRequestDto requestDto, MultipartFile itemImgFile) throws
		Exception {
		Item findItem = validateExistItemById(itemId);

		String modifiedItemName =
			!StringUtils.isEmpty(requestDto.getItemName()) ? requestDto.getItemName() : findItem.getItemName();

		Integer modifiedItemPrice = !StringUtils.isEmpty(requestDto.getItemPrice()) ?
			Integer.parseInt(requestDto.getItemPrice().replaceAll(",", "")) :
			findItem.getPrice();

		String modifiedItemDescription =
			!StringUtils.isEmpty(requestDto.getItemDescription()) ? requestDto.getItemDescription() :
				findItem.getDescription();
		String modifiedCategoryChildName =
			!StringUtils.isEmpty(requestDto.getCategoryChildName()) ? requestDto.getCategoryChildName() :
				String.valueOf(findItem.getCategory().getId());

		if (!itemImgFile.isEmpty()) {

			Optional<ItemFileInfo> itemFileInfo = itemFileInfoRepository.findByItemId(itemId);

			if (itemFileInfo.isPresent()) {
				itemImgService.updateItemImg(itemFileInfo.get().getId(), itemImgFile);
			} else {
				//이미지 등록
				ItemFileInfo savedItemFileInfo = ItemFileInfo.builder()
					.item(findItem)
					.build();
				itemImgService.saveItemImg(savedItemFileInfo, itemImgFile);
			}
		}

		Category findCategory = categoryRepository.findById(Long.valueOf(modifiedCategoryChildName))
			.orElseThrow(
				() -> new IllegalArgumentException(
					"해당 category Id을 가진 child category가 존재하지 않습니다. categoryName: "
						+ modifiedCategoryChildName)
			);

		Item modifiedItem = itemRepository.save(
			findItem.toBuilder()
				.id(itemId)
				.itemName(modifiedItemName)
				.price(modifiedItemPrice)
				.description(modifiedItemDescription)
				.isMain(findItem.isMain())
				.itemImgUrl(requestDto.getItemImgUrl())
				.category(findCategory)
				.build()
		);

		// log.error("변경된 아이템 사항  :::::::::::::::::::  " + modifiedItem.toString());

		return new ItemResponseDto(modifiedItem);
	}

	@Transactional
	public long removeItem(long itemId) {

		Item item = itemRepository.findById(itemId).orElseThrow(
			() -> new IllegalArgumentException("해당 id를 가진 item이 존재하지 않습니다.")
		);

		// 배너와 연결이 되어있으면, 삭제되면 안된다.
		Optional<BannerFileInfo> bannerFileInfo = bannerFileInfoRepository.findByItemId(itemId);
		if (bannerFileInfo.isPresent()) {
			throw new IllegalArgumentException("먼저 배너와의 연결을 삭제해 주세요.");
		}

		//사용자의 즐겨찾기에서 없애준다.
		List<Star> starsByItemId = starRepository.findStarsByItemId(itemId);
		for (Star star : starsByItemId) {
			starRepository.deleteById(star.getId());
		}

		itemRepository.save(item.toBuilder().id(itemId).isRemove(true)
			.itemName(item.getItemName() + REMOVE_ITEM + UUID.randomUUID())
			.build());


		return itemId;
	}

	@Transactional
	public void removeItemFromDB(long itemId) throws Exception {

		Optional<ItemFileInfo> itemFileInfo = itemFileInfoRepository.findByItemId(itemId);
		if (itemFileInfo.isPresent()) {
			fileService.deleteFile(itemFileInfo.get().getSaveFolderPath());
		}

		Optional<Star> starByItemId = starRepository.findStarByItemId(itemId);
		if (starByItemId.isPresent()) {
			starRepository.deleteById(starByItemId.get().getId());
		}

		Optional<CartItem> cartItemByItemId = cartItemRepository.findCartItemByItemId(itemId);
		if (cartItemByItemId.isPresent()) {
			cartItemRepository.deleteById(cartItemByItemId.get().getId());
		}

		itemRepository.deleteById(itemId);
	}

	@Transactional
	public void removeAllItemFromDB() throws Exception {
		List<Item> allByRemoveIsTrue = itemRepository.findAllByRemoveIsTrue();
		for (Item item : allByRemoveIsTrue) {
			Optional<ItemFileInfo> itemFileInfo = itemFileInfoRepository.findByItemId(item.getId());
			if (itemFileInfo.isPresent()) {
				fileService.deleteFile(itemFileInfo.get().getSaveFolderPath());
			}

			List<Star> starsByItemId = starRepository.findStarsByItemId(item.getId());
			for (Star star : starsByItemId) {
				starRepository.deleteById(star.getId());
			}

			List<CartItem> cartItemsByItemId = cartItemRepository.findCartItemsByItemId(item.getId());
			for (CartItem cartItem : cartItemsByItemId) {
				cartItemRepository.deleteById(cartItem.getId());
			}
		}

		itemRepository.deleteAll();
	}

	@Transactional
	public void removeItemFromCategory(long itemId) {

		//카테고리랑 연결된걸 끊어준다.
		Item item = validateExistItemById(itemId);
		itemRepository.save(item.toBuilder()
			.id(itemId)
			.category(null)
			.build());
	}

	@Transactional
	public void removeItemsFromCategory() {
		List<Item> itemList = itemRepository.findAllByRemoveIsTrue();
		for (Item item : itemList) {
			itemRepository.save(item.toBuilder()
				.id(item.getId())
				.category(null)
				.build());
		}
	}

	private Item validateExistItemById(long itemId) {

		return itemRepository.findById(itemId).orElseThrow(
			() -> new IllegalArgumentException("해당 id의 item이 존재하지 않습니다. itemId : " + itemId)
		);
	}

	private User validateExistUserById(long userId) {
		return userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 id의 user가 존재하지 않습니다. userId : " + userId)
		);
	}

	private void validateDuplicateItemName(String itemName) {
		List<Item> itemByItemName = itemRepository.findItemByItemName(itemName);
		if (itemByItemName.size() > 0) {
			throw new IllegalArgumentException("이미 동일한 이름의 아이템이 존재합니다.");
		}
	}

	private Category validateExistCategoryById(long categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(
			() -> new IllegalArgumentException("해당 id의 category가 존재하지 않습니다. categoryId : " + categoryId)
		);
	}

	public List<ItemResponseDto> searchItemWithKeyword(String keyword, long userId) {
		validateExistUserById(userId);
		List<Item> itemList = itemRepository.findItemsByNameWithSearchText(keyword);

		if (itemList.isEmpty()) {
			throw new IllegalArgumentException("해당 검색어와 연관된 아이템이 존재하지 않습니다.");
		}

		List<ItemResponseDto> result = new ArrayList<>();

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

	public Page<ItemResponseDto> searchItemWithKeywordV3(String keyword, long userId, Pageable pageable) {
		validateExistUserById(userId);

		int page = pageable.getPageNumber() - 1; //page위치에 있는 값은 0부터 시작
		int pageLimit = 12; //한 페이지에 보여줄 글 개수

		validateExistUserById(userId);

		// List<Item> itemList = itemRepository.findItemsByNameWithSearchText(keyword);
		Page<Item> userItemListPages = itemRepository.findItemsByNameWithSearchTextV3(keyword,
			PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

		Page<ItemResponseDto> result = userItemListPages.map(ItemResponseDto::new);

		for (ItemResponseDto itemResponseDto : result) {
			Optional<ItemFileInfo> itemImg = itemFileInfoRepository.findByItemId(itemResponseDto.getItemId());
			ItemImgDto itemImgDto = null;
			if (itemImg.isPresent()) {
				itemImgDto = ItemImgDto.of(itemImg.get());
			}
			itemResponseDto.setItemImgDto(itemImgDto);
			itemResponseDto.setStar(
				isExistStarByUserIdAndItemId(userId, itemResponseDto.getItemId())
			);
		}

		if (result.isEmpty()) {
			throw new IllegalArgumentException("해당 검색어와 연관된 아이템이 존재하지 않습니다.");
		}

		return result;

	}

	public List<ItemResponseDto> searchItemWithKeyword(String keyword) {
		// validateExistUserById(userId);
		List<Item> itemList = itemRepository.findItemsByNameWithSearchText(keyword);

		if (itemList.isEmpty()) {
			throw new IllegalArgumentException("해당 검색어와 연관된 아이템이 존재하지 않습니다.");
		}

		List<ItemResponseDto> result = new ArrayList<>();

		for (Item item : itemList) {
			ItemResponseDto responseDto = ItemResponseDto.builder()
				.itemId(item.getId())
				.itemName(item.getItemName())
				.itemPrice(item.getPrice())
				// .isStar(isExistStarByUserIdAndItemId(userId, item.getId()))
				.firstCategoryId(item.getCategory().getParent().getId())
				.firstCategoryName(item.getCategory().getParent().getCategoryName())
				.secondCategoryId(item.getCategory().getId())
				.secondCategoryName(item.getCategory().getCategoryName())
				.build();

			result.add(responseDto);
		}

		return result;

	}

	public List<ItemResponseDto> findItemWithKeywordAndIsNotMain(String keyword) {
		// validateExistUserById(userId);
		List<Item> itemList = itemRepository.findItemsByNameWithSearchTextAndIsNotMain(keyword);

		// if (itemList.isEmpty()) {
		// 	throw new IllegalArgumentException("해당 검색어와 연관된 아이템이 존재하지 않습니다.");
		// }

		List<ItemResponseDto> result = new ArrayList<>();

		for (Item item : itemList) {
			Optional<ItemFileInfo> itemImg = itemFileInfoRepository.findByItemId(item.getId());
			ItemImgDto itemImgDto = null;
			if (itemImg.isPresent()) {
				itemImgDto = ItemImgDto.of(itemImg.get());
			}
			ItemResponseDto responseDto = ItemResponseDto.builder()
				.itemId(item.getId())
				.itemName(item.getItemName())
				.itemPrice(item.getPrice())
				.firstCategoryId(item.getCategory().getParent().getId())
				.firstCategoryName(item.getCategory().getParent().getCategoryName())
				.secondCategoryId(item.getCategory().getId())
				.secondCategoryName(item.getCategory().getCategoryName())
				.itemImgDto(itemImgDto)
				.build();

			result.add(responseDto);
		}

		return result;
	}

	public List<ItemResponseDto> findItemWithKeywordAndNotConnectedItem(String keyword) {
		List<ItemResponseDto> result = new ArrayList<>();
		List<Item> itemsByNameWithSearchText = itemRepository.findItemsByNameWithSearchText(keyword);
		for (Item item : itemsByNameWithSearchText) {
			Optional<BannerFileInfo> bannerFileInfo = bannerFileInfoRepository.findByItemId(item.getId());
			if (bannerFileInfo.isEmpty()) {
				ItemResponseDto itemResponseDto = new ItemResponseDto(item);
				result.add(itemResponseDto);
			}
		}
		return result;
	}

	public List<ItemResponseDto> findItemListOther(long itemId, long userId) {
		User findUser = validateExistUserById(userId);
		Item findItem = validateExistItemById(itemId);
		Category findChildCategory = categoryRepository.findById(findItem.getCategory().getId()).orElseThrow(
			() -> new IllegalArgumentException(
				"해당 아이템의 카테고리 id가 존재하지 않습니다. categoryId : " + findItem.getCategory().getId())
		);

		List<ItemResponseDto> result = new ArrayList<>();

		List<Item> otherItemList = itemRepository.findOtherItemsById(itemId, findChildCategory.getId());

		for (Item item : otherItemList) {
			ItemResponseDto responseDto = ItemResponseDto.builder()
				.itemId(item.getId())
				.itemName(item.getItemName())
				.itemPrice(item.getPrice())
				.isMain(item.isMain())
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

	@Transactional
	public void addItemToMain(long itemId) {
		Item item = itemRepository.findById(itemId).orElseThrow(
			() -> new IllegalArgumentException(
				"해당 id의 아이템이 존재하지 않습니다. itemId : " + itemId));

		if (item.isMain()) {
			throw new IllegalArgumentException("이미 main에 등록되어있는 item입니다.");
		}

		//메인 아이템에 이미 12개 이상이면 더 못넣음
		List<Item> itemsByMainIsTrue = itemRepository.findItemsByMainIsTrueAAndRemoveIsFalse();
		if (itemsByMainIsTrue.size() >= 12) {
			throw new IllegalArgumentException("이미 main에 등록된 아이템이 12개입니다. 등록하시려면 item을 삭제해 주세요.");
		}

		Item save = itemRepository.save(item.toBuilder()
			.id(item.getId())
			.isMain(true)
			.build());

		// System.out.println(itemId + "의 main 여부가 변경 되었읍니닷. : " + save.isMain());
	}

	@Transactional
	public void removeItemToMain(long itemId) {
		Item item = itemRepository.findById(itemId).orElseThrow(
			() -> new IllegalArgumentException(
				"해당 id의 아이템이 존재하지 않습니다. itemId : " + itemId));

		if (!item.isMain()) {
			throw new IllegalArgumentException("이미 main에 등록되어있지 않은 아이템입니다. itemId : " + itemId);
		}

		itemRepository.save(item.toBuilder()
			.id(item.getId())
			.isMain(false)
			.build());
	}

	public List<ItemResponseDto> findItemListWithCategoryIdWithUserId(long categoryId, long userId) {
		User findUser = validateExistUserById(userId);
		Category findCategory = validateExistCategoryById(categoryId);

		List<ItemResponseDto> result = new ArrayList<>();

		List<Item> itemList = itemRepository.findAllByCategoryId(categoryId);

		for (Item item : itemList) {
			Optional<ItemFileInfo> itemImg = itemFileInfoRepository.findByItemId(item.getId());
			ItemImgDto itemImgDto = null;
			if (itemImg.isPresent()) {
				itemImgDto = ItemImgDto.of(itemImg.get());
			}
			ItemResponseDto responseDto = ItemResponseDto.builder()
				.itemId(item.getId())
				.itemName(item.getItemName())
				.itemPrice(item.getPrice())
				.isStar(isExistStarByUserIdAndItemId(userId, item.getId()))
				.firstCategoryId(item.getCategory().getParent().getId())
				.firstCategoryName(item.getCategory().getParent().getCategoryName())
				.secondCategoryId(item.getCategory().getId())
				.secondCategoryName(item.getCategory().getCategoryName())
				.itemImgDto(itemImgDto)
				.build();

			result.add(responseDto);
		}

		return result;
	}

	//유저 isStar가 포함된 아이템 전체 조회 서비스
	public Page<ItemResponseDto> findItemListWithUserIdV3(long userId, Pageable pageable) {

		int page = pageable.getPageNumber() - 1; //page위치에 있는 값은 0부터 시작
		int pageLimit = 12; //한 페이지에 보여줄 글 개수

		User findUser = validateExistUserById(userId);

		//페이징 처리까지 제대로.
		Page<Item> userItemListPages = itemRepository.findAllIsNotRemove(
			PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

		Page<ItemResponseDto> result = userItemListPages.map(ItemResponseDto::new);

		for (ItemResponseDto itemResponseDto : result) {
			Optional<ItemFileInfo> itemImg = itemFileInfoRepository.findByItemId(itemResponseDto.getItemId());
			ItemImgDto itemImgDto = null;
			if (itemImg.isPresent()) {
				itemImgDto = ItemImgDto.of(itemImg.get());
			}
			itemResponseDto.setItemImgDto(itemImgDto);
			itemResponseDto.setStar(
				isExistStarByUserIdAndItemId(userId, itemResponseDto.getItemId())
			);
		}

		return result;
	}

	public Page<ItemResponseDto> findItemListWithCategoryIdWithUserIdV3(
		long userId, long categoryId, Pageable pageable) {

		int page = pageable.getPageNumber() - 1;
		int pageLimit = 12;

		User findUser = validateExistUserById(userId);
		//일단 받아온 카테고리가 부모인지 자식인지 판단.
		Category findCategory = validateExistCategoryById(categoryId);

		Page<Item> userItemListPages = null;

		if (findCategory.getParent() == null) {
			//getParent()가 null일 경우 부모
			//해당 카테고리에 속해있는 모든 자식 카테고리 들고오기
			userItemListPages = itemRepository.findAllByParentCategoryIdAndIsNotRemoved(categoryId,
				PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

		} else {
			// 페이징 처리 + 카테고리 아이디가 자식일 경우
			userItemListPages = itemRepository.findAllByChildCategoryIdAndIsNotRemoved(categoryId,
				PageRequest.of(page, pageLimit,
					Sort.by(Sort.Direction.DESC, "id")));

		}

		Page<ItemResponseDto> result = userItemListPages.map(ItemResponseDto::new);

		for (ItemResponseDto itemResponseDto : result) {
			Optional<ItemFileInfo> itemImg = itemFileInfoRepository.findByItemId(itemResponseDto.getItemId());
			ItemImgDto itemImgDto = null;
			if (itemImg.isPresent()) {
				itemImgDto = ItemImgDto.of(itemImg.get());
			}
			itemResponseDto.setItemImgDto(itemImgDto);
			itemResponseDto.setStar(
				isExistStarByUserIdAndItemId(userId, itemResponseDto.getItemId())
			);
		}

		return result;

	}

	public List<ItemResponseDto> findItemListWithIsRemoveTrue() {
		List<ItemResponseDto> result = new ArrayList<>();
		List<Item> itemList = itemRepository.findItemsByRemoveIsTrue();
		for (Item item : itemList) {
			Optional<ItemFileInfo> itemImg = itemFileInfoRepository.findByItemId(item.getId());
			ItemImgDto itemImgDto = null;
			if (itemImg.isPresent()) {
				itemImgDto = ItemImgDto.of(itemImg.get());
			}
			ItemResponseDto itemResponseDto = ItemResponseDto.of(item);
			itemResponseDto.setItemImgDto(itemImgDto);
			result.add(itemResponseDto);

		}
		return result;
	}

	public Page<ItemResponseDto> findItemListWithIsRemoveTrueV3() {
		return null;
	}

	public List<ItemResponseDto> findItemListWithCategoryIdWithIsRemoveTrue(long categoryId) {
		List<ItemResponseDto> result = new ArrayList<>();

		Category category = categoryRepository.findById(categoryId).orElseThrow(
			() -> new IllegalArgumentException("해당 id의 category가 존재하지 않습니다.")
		);

		List<Item> itemList = new ArrayList<>();
		if (category.getParent() == null) {
			itemList = itemRepository.findAllByCategoryChildWithParentIdAndRemoved(categoryId);

		} else {
			itemList = itemRepository.findItemsByRemoveIsTrueAndCategoryId(categoryId);
		}

		for (Item item : itemList) {
			Optional<ItemFileInfo> itemImg = itemFileInfoRepository.findByItemId(item.getId());
			ItemImgDto itemImgDto = null;
			if (itemImg.isPresent()) {
				itemImgDto = ItemImgDto.of(itemImg.get());
			}
			ItemResponseDto itemResponseDto = ItemResponseDto.of(item);
			itemResponseDto.setItemImgDto(itemImgDto);
			result.add(itemResponseDto);
		}
		return result;
	}

	public List<ItemResponseDto> findItemListWithIsRemoveTrueAndHaveNotCategory() {
		List<ItemResponseDto> result = new ArrayList<>();
		List<Item> itemList = itemRepository.findItemsByRemoveIsTrue();
		for (Item item : itemList) {
			if (item.getCategory() == null) {
				Optional<ItemFileInfo> itemImg = itemFileInfoRepository.findByItemId(item.getId());
				ItemImgDto itemImgDto = null;
				if (itemImg.isPresent()) {
					itemImgDto = ItemImgDto.of(itemImg.get());
				}
				ItemResponseDto itemResponseDto = ItemResponseDto.of(item);
				itemResponseDto.setItemImgDto(itemImgDto);
				result.add(itemResponseDto);
			}
		}
		return result;
	}

}
