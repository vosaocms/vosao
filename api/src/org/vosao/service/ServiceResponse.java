/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.service;

import java.util.ArrayList;
import java.util.List;

public class ServiceResponse {

	public static String ERROR_RESULT = "error";
	public static String SUCCESS_RESULT = "success";
	
	private String result;
	private String message;
	private List<String> messages;
	private Object data;
	
	public static ServiceResponse createErrorResponse(final String msg) {
		return new ServiceResponse(ERROR_RESULT, msg);
	}
	
	public static ServiceResponse createErrorResponse(final String msg,
			final List<String> errors) {
		ServiceResponse result = new ServiceResponse(ERROR_RESULT, msg);
		result.messages= errors;
		return result;
	}

	public static ServiceResponse createSuccessResponse(final String msg) {
		return new ServiceResponse(SUCCESS_RESULT, msg);
	}

	public static ServiceResponse createSuccessResponse(final String msg, 
			final Object data) {
		return new ServiceResponse(SUCCESS_RESULT, msg, data);
	}

	public static ServiceResponse createSuccessResponse(final String msg, 
			final List<String> messages) {
		ServiceResponse result = new ServiceResponse(SUCCESS_RESULT, msg);
		result.setMessages(messages);
		return result;
	}

	public ServiceResponse() {
		messages = new ArrayList<String>();
	}
	
	public ServiceResponse(String result, String message) {
		this();
		this.result = result;
		this.message = message;
	}
	
	public ServiceResponse(String result, String message, Object data) {
		this();
		this.result = result;
		this.message = message;
		this.data = data;
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
