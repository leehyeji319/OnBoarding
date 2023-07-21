package com.estgames.common.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
	private final LocalDateTime timestamp = LocalDateTime.now();
	private final HttpStatus status;
	private final String message;
}