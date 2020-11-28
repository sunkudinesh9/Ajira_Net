package com.ajiranet.networkcommunicationservice.exceptionhandler;

public class DeviceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DeviceNotFoundException() {
		super("Device Not FOund");
	}
}
