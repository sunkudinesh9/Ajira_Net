package com.ajiranet.networkcommunicationservice.exceptionhandler;

public class DeviceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DeviceNotFoundException(String error) {
		super(error);
	}
}
