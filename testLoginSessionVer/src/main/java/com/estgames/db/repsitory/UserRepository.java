package com.estgames.db.repsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.estgames.db.entiity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByLoginId(String loginId);

	@Query("select u from User u where u.loginId = :loginId")
	Optional<User> findByLoginId(@Param("loginId") String loginId);

	Optional<User> findByLoginIdAndPassword(String loginId, String password);
}
