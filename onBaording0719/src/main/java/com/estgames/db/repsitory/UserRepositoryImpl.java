package com.estgames.db.repsitory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.estgames.db.entiity.Category;
import com.estgames.db.entiity.Item;
import com.estgames.db.entiity.Role;
import com.estgames.db.entiity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserRepositoryImpl {

	//ConcurrentHashMap: 메모리에 저장하도록 했다.
	private static Map<Long, User> store = new ConcurrentHashMap<>();
	private static long sequence = 0L;

	public User save(User user) {

		user.setId(++sequence);
		log.info("save: member={}", user);
		store.put(user.getId(), user);

		return user;
	}


	public User findById(Long id) {
		return store.get(id);
	}

	public Optional<User> findByLoginId(String loginId) {

		return this.findAll().stream()
			.filter(u -> u.getLoginId().equals(loginId))
			.findFirst();
	}

	public List<User> findAll() {
		return new ArrayList<>(store.values());
	}

	public void clearStore() {
		store.clear();
	}

	/**
	 * 테스트용 데이터 추가
	 */
	// @PostConstruct //테스트용 회원 생성
	// public void init() {
	// 	User user = User.builder()
	// 		.loginId("test")
	// 		.password("test!")
	// 		.name("테스트")
	// 		.role(Role.ROLE_USER)
	// 		.cash(5000)
	// 		.build();
	// 	save(user);
	// }

	// @PostConstruct
	// public void initAdmin() {
	// 	User user = User.builder()
	// 		.loginId("admin")
	// 		.password("admin!")
	// 		.name("관리자")
	// 		.role(Role.ROLE_ADMIN)
	// 		.cash(0)
	// 		.build();
	// 	save(user);
	// }
}
