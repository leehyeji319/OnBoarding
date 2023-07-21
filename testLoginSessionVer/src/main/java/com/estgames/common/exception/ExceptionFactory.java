package com.estgames.common.exception;

import org.springframework.http.HttpStatus;

public final class ExceptionFactory {
	private ExceptionFactory() {}

	public static CustomException userNotFound(long userId) {
		return new CustomException(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다. userId : " + userId);
	}

	public static CustomException authenticateAdminFail(){
		return new CustomException(HttpStatus.UNAUTHORIZED, "일반 유저는 권한이 없습니다.");
	}

	public static CustomException authenticateUserFail(){
		return new CustomException(HttpStatus.UNAUTHORIZED, "관리자 유저는 권한이 없습니다.");
	}

	public static CustomException invalidAccess() {
		return new CustomException(HttpStatus.BAD_REQUEST, "잘못된 접근입니다. 로그인을 진행해주세요.");
	}

}
