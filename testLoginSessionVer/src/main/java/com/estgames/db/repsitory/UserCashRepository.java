package com.estgames.db.repsitory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.estgames.db.entiity.UserCash;

public interface UserCashRepository extends JpaRepository<UserCash, Long> {

	List<UserCash> findUserCashByUserId(long userId);

	Page<UserCash> findAllByUserId(long userId, Pageable pageable);
}
