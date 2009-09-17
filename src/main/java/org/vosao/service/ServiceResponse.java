package org.vosao.service;

import java.util.ArrayList;
import java.util.List;

public class ServiceResponse {

	private String result;
	private String message;
	private List<String> messages;
	
	public ServiceResponse() {
		messages = new ArrayList<String>();
	}
	
	public ServiceResponse(String result, String message) {
		this();
		this.result = result;
		this.message = message;
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
}
