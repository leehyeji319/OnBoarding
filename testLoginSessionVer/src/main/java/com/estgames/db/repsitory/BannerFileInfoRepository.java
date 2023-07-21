package com.estgames.db.repsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estgames.db.entiity.BannerFileInfo;

public interface BannerFileInfoRepository extends JpaRepository<BannerFileInfo, Long> {

	Optional<BannerFileInfo> findByItemId(Long itemId);
}
