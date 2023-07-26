package com.estgames.common.exception;

import org.springframework.http.HttpStatus;

public final class ExceptionFactory {
	private ExceptionFactory() {
	}

	public static CustomException userNotFound(long userId) {
		return new CustomException(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다. userId : " + userId);
	}

	public static CustomException authenticateAdminFail() {
		return new CustomException(HttpStatus.UNAUTHORIZED, "일반 유저는 권한이 없습니다.");
	}

	public static CustomException authenticateUserFail() {
		return new CustomException(HttpStatus.UNAUTHORIZED, "관리자 유저는 권한이 없습니다.");
	}

	public static CustomException invalidAccess() {
		return new CustomException(HttpStatus.BAD_REQUEST, "잘못된 접근입니다. 로그인을 진행해주세요.");
	}

	//유저
	// 장바구니에 이미 같은 아이템이 들어가 있을 때
	public static CustomException duplicateItemUserCart() {
		return new CustomException(HttpStatus.CONFLICT, "이미 동일한 아이템이 장바구니에 존재합니다.");
	}

	// 유저의 돈이 모자랄 때
	public static CustomException userCashLessThanForPurchase() {
		return new CustomException(HttpStatus.BAD_REQUEST, "구매금액이 보유금액보다 큽니다.");
	}

	//어드민

	//메인에 등록된 아이템이 이미 12개 일때
	public static CustomException invalidRegisterMainItem() {
		return new CustomException(HttpStatus.BAD_REQUEST,
			"이미 메인에 12개의 아이템이 등록되어 있습니다. 삭제 후 진행해주세요.");
	}

	//카테고리 대분류 삭제 시 중분류가 존재하여 안될 때
	public static CustomException invalidDeleteParentCategory() {
		return new CustomException(HttpStatus.BAD_REQUEST, "대분류 카테고리 하위에 중분류 카테고리가 존재합니다.");
	}

	//카테고리 중분류 삭제 시 하위에 아이템이 존재하여 안될 때
	public static CustomException invalidDeleteChildCategory() {
		return new CustomException(HttpStatus.BAD_REQUEST,
			"중분류 하위에 아이템이 존재합니다. 아이템을 옮긴 후 삭제를 진행해 주세요.");
	}

}
