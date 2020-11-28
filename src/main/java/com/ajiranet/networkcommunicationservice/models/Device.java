package com.ajiranet.networkcommunicationservice.models;

import javax.validation.constraints.NotNull;

public class Device {
	@NotNull(message = "should not be null")
	private String type;
	@NotNull(message = "should not be null")
	private String name;
	@NotNull(message = "should not be null")
	private int strength;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

}
