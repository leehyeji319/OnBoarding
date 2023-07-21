package com.estgames.web.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estgames.db.entiity.OrderItem;
import com.estgames.db.entiity.UserCash;
import com.estgames.db.repsitory.OrderItemRepository;
import com.estgames.web.dto.cash.UserCashLogDto;
import com.estgames.web.dto.order.UserOrderItemDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemService {

	private final OrderItemRepository orderItemRepository;

	public Page<UserOrderItemDto> findUserOrderItemListV3(long userId, Pageable pageable) {
		int page = pageable.getPageNumber() - 1; //page위치에 있는 값은 0부터 시작
		int pageLimit = 10; //한 페이지에 보여줄 글 개수

		// 한 페이지당 10개씩 글을 보여주고 정렬 기준은 ID 기준으로 내림차순
		Page<OrderItem> userOrderItemLogListPages = orderItemRepository.findAllByUserId(
			userId, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "orderDate")));

		return userOrderItemLogListPages.map(UserOrderItemDto::new);
	}
}
