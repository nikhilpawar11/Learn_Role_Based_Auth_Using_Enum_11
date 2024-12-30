package com.nikhil.learn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiMessageResponse> handleGlobalException(ResourceNotFoundException ex){
		
		ApiMessageResponse apiMessageResponse = ApiMessageResponse.builder().message(ex.getMessage()).success(false).status(HttpStatus.NOT_FOUND).build();
		
		return new ResponseEntity<>(apiMessageResponse, HttpStatus.NOT_FOUND);
		
	}

}
