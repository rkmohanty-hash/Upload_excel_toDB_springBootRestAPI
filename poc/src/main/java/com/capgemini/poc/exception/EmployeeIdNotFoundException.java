package com.capgemini.poc.exception;

public class EmployeeIdNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeeIdNotFoundException(String message) {
		super(message);
	}

}
