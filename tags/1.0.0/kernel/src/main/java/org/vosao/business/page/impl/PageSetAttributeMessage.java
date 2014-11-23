package org.vosao.business.page.impl;

import org.vosao.business.mq.AbstractMessage;
import org.vosao.business.mq.QueueSpeed;

public class PageSetAttributeMessage extends AbstractMessage {
	
	private String url;
	private String name;
	private String language;
	private String value;
	
	public PageSetAttributeMessage(String url, String name, String language,
			String value) {
		super();
		this.url = url;
		this.name = name;
		this.language = language;
		this.value = value;
		setTopic("page.setAttribute");
		setCommandClassName(PageSetAttributeTask.class.getName());
		setSpeed(QueueSpeed.MEDIUM);
	}

	public String getUrl() {
		return url;
	}

	public String getName() {
		return name;
	}

	public String getLanguage() {
		return language;
	}

	public String getValue() {
		return value;
	}
	
}
