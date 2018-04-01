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

package org.vosao.common;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vosao.business.Business;
import org.vosao.business.mq.MessageQueue;
import org.vosao.business.page.PageRenderingContext;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.UserEntity;
import org.vosao.service.BackService;
import org.vosao.service.FrontService;

/**
 * Store request scoped data in thread local variable and set it in filter.
 * 
 * @author Alexander Oleynik
 */
public class VosaoContext {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private int requestCount;
	private long startTime;
	
	private Session session;
	private Locale locale;
	private UserEntity user;
	private ConfigEntity config;
	private Business business;
	private FrontService frontService;
	private BackService backService;
	private MessageQueue messageQueue;
	private List<String> skipURLs;
	private PageRenderingContext pageRenderingContext;
	
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
		if (locale == null) {
			return getConfig().getDefaultLanguage();
		}
		return locale.getLanguage();
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale aLocale) {
		locale = aLocale;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	private static ThreadLocal<VosaoContext> threadInstance;
 
	public static VosaoContext getInstance() {
		if (threadInstance == null) {
			threadInstance = new ThreadLocal<VosaoContext>() {
				@Override
				protected VosaoContext initialValue() {
					return new VosaoContext();
				}
			};
		}
		return threadInstance.get();
	}
	
	public int getRequestCount() {
		return requestCount;
	}

	public long getStartTime() {
		return startTime;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public FrontService getFrontService() {
		return frontService;
	}

	public void setFrontService(FrontService frontService) {
		this.frontService = frontService;
	}

	public BackService getBackService() {
		return backService;
	}

	public void setBackService(BackService backService) {
		this.backService = backService;
	}

	public MessageQueue getMessageQueue() {
		return messageQueue;
	}

	public void setMessageQueue(MessageQueue messageQueue) {
		this.messageQueue = messageQueue;
	}

	public List<String> getSkipURLs() {
		return skipURLs;
	}

	public void setSkipURLs(List<String> skipURLs) {
		this.skipURLs = skipURLs;
	}
	
	public boolean isSkipUrl(final String url) {
    	for (String u : skipURLs) {
    		if (url.startsWith(u)) {
    			return true;
    		}
    	}
    	return false;
    }

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public ConfigEntity getConfig() {
		if (config == null) {
			config = getBusiness().getDao().getConfigDao().getConfig();
			if (config == null) {
				config = new ConfigEntity();
			}
		}
		return config;
	}

	public void setConfig(ConfigEntity config) {
		this.config = config;
	}

	public PageRenderingContext getPageRenderingContext() {
		if (pageRenderingContext == null) {
			pageRenderingContext = new PageRenderingContext();
		}
		return pageRenderingContext;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}
