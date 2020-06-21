package com.capgemini.poc.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomGlobalExceptionHandler {
	//handle specific exception
		@ExceptionHandler(EmployeeIdNotFoundException.class)
		public final ResponseEntity<Object> handleEmployeeIdNotFoundException(EmployeeIdNotFoundException ex,
				WebRequest request) {

			AppError appError = new AppError(new Date(), ex.getMessage(), request.getDescription(false));
			return new ResponseEntity<>(appError, HttpStatus.NOT_FOUND);

		}
		
		@ExceptionHandler(BadRequestException.class)
		public final ResponseEntity<Object> handleEmployeeIdNotFoundException(BadRequestException ex,
				WebRequest request) {

			AppError appError = new AppError(new Date(), ex.getMessage(), request.getDescription(false));
			return new ResponseEntity<>(appError, HttpStatus.BAD_REQUEST);

		}
		

		//handle global exception
		@ExceptionHandler(Exception.class)
		public final ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {

			AppError appError = new AppError(new Date(), ex.getMessage(), request.getDescription(false));
			return new ResponseEntity<>(appError, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		/*
		 * //handling custom validation errors field level validation
		 * 
		 * @ExceptionHandler(MethodArgumentNotValidException.class) public final
		 * ResponseEntity<Object>
		 * customValidationErrorHandling(MethodArgumentNotValidException exception) {
		 * 
		 * AppError appError = new AppError(new
		 * Date(),"Validation Error",exception.getBindingResult().getFieldError().
		 * getDefaultMessage()); return new ResponseEntity<>(appError,
		 * HttpStatus.BAD_REQUEST);
		 * 
		 * }
		 */
}
