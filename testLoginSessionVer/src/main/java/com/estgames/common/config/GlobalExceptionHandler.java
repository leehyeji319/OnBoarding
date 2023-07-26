package com.estgames.common.config;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.estgames.common.exception.CustomException;
import com.estgames.common.exception.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<?> handlerCustomException(CustomException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(ErrorResponse.builder()
			.message(e.getMessage())
			.timestamp(e.getTimestamp().toString())
			.build(), e.getStatus());
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<?> handlerException(Exception e) {
		log.error("글로벌 핸들러 : " + e.getMessage());
		e.printStackTrace();
		return new ResponseEntity<>(
			ErrorResponse.builder()
				.message("글로벌 핸들러 : " + e.getMessage())
				.timestamp(Timestamp.from(Instant.now()).toString())
				.build(), HttpStatus.INTERNAL_SERVER_ERROR
		);
	}
}
