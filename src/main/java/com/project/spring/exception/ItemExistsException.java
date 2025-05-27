package com.project.spring.exception;

@SuppressWarnings("serial")
public class ItemExistsException extends RuntimeException {
	public ItemExistsException(String message) {
		super(message);
	}
}
