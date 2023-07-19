package com.estgames.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estgames.db.repsitory.CategoryRepositoryImpl;
import com.estgames.web.dto.category.CategoryModifyRequestDto;
import com.estgames.web.dto.category.CategoryModifyResponseDto;
import com.estgames.web.dto.category.CategoryParentsResponseDto;
import com.estgames.web.dto.category.CategoryResponseDto;
import com.estgames.web.dto.category.CategorySaveRequestDto;
import com.estgames.db.entiity.Category;
import com.estgames.db.repsitory.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryRepositoryImpl categoryRepositoryImpl;

	public List<CategoryParentsResponseDto> findCategoryList() {
		return categoryRepositoryImpl.findAllCategoryByDto_optimization();
	}


	// public List<CategoryResponseDto> findCategoryList() {
	// 	List<Category> list = categoryRepository.findAll();
	//
	// 	return list.stream()
	// 		.map(CategoryResponseDto::new)
	// 		.collect(Collectors.toList());
	// }

	public CategoryResponseDto findCategory(long categoryId) {
		Category findCategory = validateCategoryExistByCategoryId(categoryId);

		CategoryResponseDto responseDto = CategoryResponseDto.builder()
			.categoryId(findCategory.getId())
			.categoryName(findCategory.getCategoryName())
			.build();

		return responseDto;
	}

	//카테고리 생성
	@Transactional
	public CategoryResponseDto addCategory(CategorySaveRequestDto requestDto) {

		// 중복 카테고리인지 확인
		validateCategoryDuplicateByCategoryName(requestDto.getCategoryName());

		//requestDto.targetCategoryId 가 null이면 대분류 생성 not null 이면 중분류
		if (requestDto.getCategoryId() == null) {
			Category savedCategory = categoryRepository.save(requestDto.toEntity());

			return CategoryResponseDto.builder()
				.categoryId(savedCategory.getId())
				.categoryName(savedCategory.getCategoryName())
				.build();

		} else {

			Category findParentCategory = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(
				() -> new IllegalArgumentException(
					"해당 id의 대분류 카테고리가 존재하지 않습니다. categoryId: " + requestDto.getCategoryId())
			);

			Category savedCategory = categoryRepository.save(requestDto.toEntity(findParentCategory));

			return CategoryResponseDto.builder()
				.parentCategoryId(savedCategory.getParent().getId())
				.parentCategoryName(savedCategory.getParent().getCategoryName())
				.categoryId(savedCategory.getId())
				.categoryName(savedCategory.getCategoryName())
				.build();
		}
	}

	@Transactional
	public CategoryModifyResponseDto modifyCategory(CategoryModifyRequestDto requestDto) {
		Category findCategory = validateCategoryExistByCategoryId(requestDto.getCategoryId());

		boolean isChild = false;

		//대분류를 변경하는 경우
		if (requestDto.getParentCategoryId() != null) {
			// 일단 변경하고자 하는 타겟이 중분류가 맞는지 확인
			if (findCategory.getParent() == null) {
				throw new IllegalArgumentException("해당 카테고리는 대분류이기 때문에 상위 카테고리가 존재할 수 없습니다.");
			} else {
				//중분류이고, 상위 변경하려는 대분류가 존재하는지 확인
				Category targetParentCategory = validateCategoryExistByCategoryId(requestDto.getParentCategoryId());
				isChild = true;
				if (requestDto.getCategoryName() == null) {
					requestDto.setCategoryName(findCategory.getCategoryName());
				}

				//존재한다면, 부모 바꾸기
				categoryRepository.save(
					findCategory.toBuilder()
						.id(findCategory.getId())
						.categoryName(requestDto.getCategoryName())
						.parent(targetParentCategory)
						.build()
				);

			}

		} else {
			//이름을 변경하는데 카테고리 이름 중복 조회
			validateCategoryDuplicateByCategoryName(requestDto.getCategoryName());
			categoryRepository.save(
				findCategory.toBuilder()
					.id(findCategory.getId())
					.categoryName(requestDto.getCategoryName())
					.build()
			);
		}

		if (isChild) {
			return CategoryModifyResponseDto.builder()
				.categoryId(findCategory.getId())
				.categoryName(findCategory.getCategoryName())
				.parentCategoryId(findCategory.getParent().getId())
				.parentCategoryName(findCategory.getParent().getCategoryName())
				.build();
		} else {
			return CategoryModifyResponseDto.builder()
				.categoryId(findCategory.getId())
				.categoryName(findCategory.getCategoryName())
				.build();
		}

	}

	@Transactional
	public long removeCategory(long categoryId) {
		Category category = validateCategoryExistByCategoryId(categoryId);
		if (category.getParent() == null & category.getChild().size() > 0) {
			throw new IllegalArgumentException("해당 대분류 하위에 있는 중분류 카테고리가 존재하지 않아야합니다.");
		}
		if (category.getItemList().size() > 0) {
			throw new IllegalArgumentException("해당 카테고리에 아이템이 존재해서는 안됩니다.");
		}

		categoryRepository.deleteById(categoryId);
		return categoryId;
	}

	private Category validateCategoryExistByCategoryId(long categoryId) {
		Category findCategory = categoryRepository.findById(categoryId).orElseThrow(
			() -> new IllegalArgumentException("해당 id의 카테고리가 존재하지 않습니다. categoryId: " + categoryId)
		);
		return findCategory;
	}

	private void validateCategoryDuplicateByCategoryName(String categoryName) {
		Optional<Category> categoryByCategoryName = categoryRepository.findCategoryByCategoryName(
			categoryName);

		if (categoryByCategoryName.isPresent()) {
			throw new IllegalArgumentException("동일 이름의 카테고리가 이미 존재합니다. categoryName: " + categoryName);
		}
	}

	@Transactional
	public void modifyCategoryName(CategoryModifyRequestDto requestDto) {

		Category findCategory = validateCategoryExistByCategoryId(requestDto.getCategoryId());
		validateCategoryDuplicateByCategoryName(requestDto.getCategoryName());

		Category modifiedCategory = categoryRepository.save(
			findCategory.toBuilder()
				.id(requestDto.getCategoryId())
				.categoryName(requestDto.getCategoryName())
				.build()
		);

	}

	@Transactional
	public void moveChildCategoryAnotherParentCategory(CategoryModifyRequestDto requestDto) {

		Long childCategoryId = requestDto.getCategoryId();
		Long targetCategoryId = requestDto.getTargetCategoryId();

		Category childCategory = categoryRepository.findById(childCategoryId).orElseThrow(
			() -> new IllegalArgumentException("해당 id의 카테고리가 존재하지 않습니다.")
		);

		Category targetParentCategory = categoryRepository.findById(targetCategoryId).orElseThrow(
			() -> new IllegalArgumentException("해당 id의 대분류 카테고리가 존재하지 않습니다.")
		);

		categoryRepository.save(childCategory.toBuilder()
			.id(childCategoryId)
			.parent(targetParentCategory)
			.build());

	}
}
