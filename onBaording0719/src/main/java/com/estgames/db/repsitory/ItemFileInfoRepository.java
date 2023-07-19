package com.estgames.db.repsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estgames.db.entiity.ItemFileInfo;

public interface ItemFileInfoRepository extends JpaRepository<ItemFileInfo, Long> {

	Optional<ItemFileInfo> findByItemId(Long itemId);
}
