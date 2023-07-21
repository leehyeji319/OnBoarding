package com.estgames.db.repsitory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.estgames.db.entiity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

	@Query("select i from Item i where i.itemName = :itemName")
	List<Item> findItemByItemName(@Param("itemName") String itemName);

	@Query("select i from Item i where i.isMain = true and i.isRemove = false")
	List<Item> findItemsByMainIsTrueAAndRemoveIsFalse();

	@Query("select i from Item i where i.itemName like %:text% and i.isRemove = false")
	List<Item> findItemsByNameWithSearchText(@Param("text") String text);

	@Query("select i from Item i where i.itemName like %:text% and i.isMain = false and i.isRemove = false")
	List<Item> findItemsByNameWithSearchTextAndIsNotMain(@Param("text") String text);

	@Query("select i from Item i where i.id != :itemId and i.category.id = :childCategoryId and i.isRemove = false")
	List<Item> findOtherItemsById(long itemId, long childCategoryId);

	@Query("select i from Item i where i.category.id = :categoryId and i.isRemove = false")
	List<Item> findAllByCategoryId(@Param("categoryId") long categoryId);

	@Query("select i from Item i where i.category.parent.id = :categoryId and i.isRemove = false")
	List<Item> findAllByCategoryChildWithParentId(@Param("categoryId") long categoryId);

	@Query("select i from Item i where i.category.parent.id = :categoryId and i.isRemove = true")
	List<Item> findAllByCategoryChildWithParentIdAndRemoved(@Param("categoryId") long categoryId);
	@Query("select i from Item i where i.isRemove = false ")
	List<Item> findAllByRemoveIsFalse();

	@Query("SELECT i FROM Item i WHERE i.isRemove = false")
	Page<Item> findAllIsNotRemove(Pageable pageable);

	@Query("select i from Item i where i.category.id = :categoryId and i.isRemove = false")
	Page<Item> findAllByChildCategoryIdAndIsNotRemoved(@Param("categoryId") long categoryId, Pageable pageable);

	@Query("select i from Item i where i.category.parent.id = :categoryId and i.isRemove = false")
	Page<Item> findAllByParentCategoryIdAndIsNotRemoved(@Param("categoryId") long categoryId, Pageable pageable);

	@Query("select i from Item i where i.itemName like %:keyword% and i.isRemove = false")
	Page<Item> findItemsByNameWithSearchTextV3(@Param("keyword") String keyword, Pageable pageable);

	@Query("select i from Item i where i.isRemove = true")
	List<Item> findItemsByRemoveIsTrue();

	@Query("select i from Item i where i.isRemove = true and i.category.id = :categoryId")
	List<Item> findItemsByRemoveIsTrueAndCategoryId(@Param("categoryId") long categoryId);

	@Query("select i from Item i where i.isRemove = true")
	List<Item> findAllByRemoveIsTrue();
}
