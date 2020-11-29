package com.ajiranet.networkcommunicationservice.models;

import java.util.List;

import javax.validation.constraints.NotNull;

public class Connection {
	@NotNull
	private String source;
	@NotNull(message = "target should not be null")
	private List<String> target;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<String> getTarget() {
		return target;
	}

	public void setTarget(List<String> target) {
		this.target = target;
	}

}
