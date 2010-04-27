/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.common;

import javax.servlet.http.HttpServletRequest;

import org.vosao.entity.UserEntity;

/**
 * Because of GAE is single threaded we can store request scoped data in 
 * singleton and set it in filter.
 * 
 * @author Alexander Oleynik
 */
public class VosaoContext {

	private HttpServletRequest request;
	private int requestCount;
	private long startTime;
	
	private String language;
	private UserEntity user;
	
	private VosaoContext() {
		requestCount = 0;
		startTime = System.currentTimeMillis();
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
		requestCount++;
		startTime = System.currentTimeMillis();
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	
	private static VosaoContext instance;
	
	public static VosaoContext getInstance() {
		if (instance == null) {
			instance = new VosaoContext();
		}
		return instance;
	}

	public int getRequestCount() {
		return requestCount;
	}

	public long getStartTime() {
		return startTime;
	}
}
