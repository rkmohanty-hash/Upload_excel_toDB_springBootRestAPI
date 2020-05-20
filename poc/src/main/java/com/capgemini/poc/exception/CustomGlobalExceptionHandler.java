package com.capgemini.poc.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

	@ExceptionHandler(EmployeeIdNotFoundException.class)
	public final ResponseEntity<Object> handleEmployeeIdNotFoundException(EmployeeIdNotFoundException ex,
			WebRequest request) {

		AppError appError = new AppError(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(appError, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {

		AppError appError = new AppError(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(appError, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
