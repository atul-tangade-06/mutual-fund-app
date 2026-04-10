package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> validation() {
		return ResponseEntity.badRequest().body("Invalid Input");
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handle(RuntimeException ex) {

		if ("DUPLICATE".equals(ex.getMessage()))
			return ResponseEntity.status(409).body("Duplicate Request");

		if ("INSUFFICIENT".equals(ex.getMessage()))
			return ResponseEntity.badRequest().body("Insufficient Units");

		return ResponseEntity.badRequest().body(ex.getMessage());
	}
}
