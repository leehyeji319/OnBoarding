package com.estgames.db.repsitory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.estgames.web.dto.category.CategoryChildDto;
import com.estgames.web.dto.category.CategoryParentsResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CategoryRepositoryImpl {

	private final EntityManager em;

	//카테고리 부모만 다 불러오기
	public List<CategoryParentsResponseDto> findAllCategoryByDto_optimization() {
		List<CategoryParentsResponseDto> result = findParentCategories();

		Map<Long, List<CategoryChildDto>> categoryChildMap = findCategoryChildMap(toCategoryParentsIds(result));
		result.forEach(c -> c.setCategoryChildDtoList(categoryChildMap.get(c.getCategoryParentId())));

		return result;
	}


	private List<CategoryParentsResponseDto> findParentCategories() {
		return em.createQuery(
			"select new com.estgames.web.dto.category.CategoryParentsResponseDto"
				+ "(c.id, c.categoryName)"
				+ " from Category c"
				+ " where c.parent = null", CategoryParentsResponseDto.class)
			.getResultList();
	}

	private List<Long> toCategoryParentsIds(List<CategoryParentsResponseDto> result) {
		return result.stream()
			.map(CategoryParentsResponseDto::getCategoryParentId)
			.collect(Collectors.toList());
	}

	private Map<Long, List<CategoryChildDto>> findCategoryChildMap(List<Long> categoryParentsIds) {
		List<CategoryChildDto> categoryChild = em.createQuery(
				"select new com.estgames.web.dto.category.CategoryChildDto"
					+ "(c.parent.id, c.id, c.categoryName)"
					+ " from Category c"
					+ " where c.parent.id in :categoryParentsIds", CategoryChildDto.class)
			.setParameter("categoryParentsIds", categoryParentsIds)
			.getResultList();

		// log.error(collect.toString());
		return categoryChild.stream()
			.collect(Collectors.groupingBy(CategoryChildDto::getCategoryParentId));
	}
}
