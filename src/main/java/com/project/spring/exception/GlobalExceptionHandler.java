package com.project.spring.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.HttpHeaders;

import com.project.spring.io.ErrorObject;

import lombok.extern.slf4j.Slf4j;

/**
 * Global exceptional handler for all exceptions
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
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
	
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(ItemExistsException.class)
	public ErrorObject handleItemExistsException(ItemExistsException ex, WebRequest request) {
		log.error("Throwing the ItemExistsException from GlobalExceptionHandler {}", ex.getMessage());
		return ErrorObject.builder()
				.errorCode("DATA_NOT_EXISTS")
				.statusCode(HttpStatus.CONFLICT.value())
				.message(ex.getMessage())
				.timestamp(new Date())
				.build();
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorObject handleGException(Exception ex, WebRequest request) {
		log.error("Throwing the ResourceNotFoundException from GlobalExceptionHandler {}", ex.getMessage());
		return ErrorObject.builder()
				.errorCode("UNEXPECTED_ERROR")
				.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(ex.getMessage())
				.timestamp(new Date())
				.build();
	}
	
	@Override
	@Nullable
	protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
			@NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
		// TODO Auto-generated method stub
		super.handleMethodArgumentNotValid(ex, headers, status, request);
		Map<String, Object> errorResponse = new HashMap<>();
		List<String> errors = ex.getBindingResult().getFieldErrors()
				.stream().map(field -> field.getDefaultMessage())
				.collect(Collectors.toList());
		errorResponse.put("statusCode", HttpStatus.BAD_REQUEST.value());
		errorResponse.put("message", errors);
		errorResponse.put("timestamp", new Date());
		errorResponse.put("errorCode", "VALIDATION_FAILED");
		return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}
