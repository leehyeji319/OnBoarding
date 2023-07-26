package com.estgames.common.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {
	private String message;
	private String timestamp;
}
