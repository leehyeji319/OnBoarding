package com.estgames.web.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estgames.db.repsitory.BannerFileInfoRepository;
import com.estgames.db.repsitory.CategoryRepository;
import com.estgames.db.repsitory.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminService {

	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;
	private final BannerFileInfoRepository bannerFileInfoRepository;

}
