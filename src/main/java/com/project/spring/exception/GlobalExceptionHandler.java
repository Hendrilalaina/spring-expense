package com.project.spring.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.project.spring.io.ErrorObject;

import lombok.extern.slf4j.Slf4j;

/**
 * Global exceptional handler for all exceptions
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public ErrorObject handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		log.error("Throwing the ResourceNotFoundException from GlobalExceptionHandler {}", ex.getMessage());
		return ErrorObject.builder()
				.errorCode("DATA_NOT_FOUND")
				.statusCode(HttpStatus.NOT_FOUND.value())
				.message(ex.getMessage())
				.timestamp(new Date())
				.build();
	}
}
