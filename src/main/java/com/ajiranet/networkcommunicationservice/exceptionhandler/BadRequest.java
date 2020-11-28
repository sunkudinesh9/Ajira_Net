package com.ajiranet.networkcommunicationservice.exceptionhandler;

public class BadRequest extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BadRequest(String error) {
		super(error);
	}
}
