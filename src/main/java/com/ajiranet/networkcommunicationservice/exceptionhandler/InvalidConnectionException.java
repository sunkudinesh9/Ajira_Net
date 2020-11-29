package com.ajiranet.networkcommunicationservice.exceptionhandler;

public class InvalidConnectionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidConnectionException(String error) {
		super(error);
	}
}
