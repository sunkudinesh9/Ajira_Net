package com.ajiranet.networkcommunicationservice.models;

import javax.validation.constraints.NotNull;

public class StrengthValue {
	@NotNull(message = "should not be null")
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
