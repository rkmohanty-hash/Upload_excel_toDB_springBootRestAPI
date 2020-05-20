package com.capgemini.poc.exception;

import java.util.Date;

public class AppError {
	
	private Date timeStamp;
	private String message;
	private String errorDetails;
	
	public AppError(Date timeStamp, String message, String errorDetails) {
		super();
		this.timeStamp = timeStamp;
		this.message = message;
		this.errorDetails = errorDetails;
	}
	
	public Date getTimeStamp() {
		return timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	
	
	

}
