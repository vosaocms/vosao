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

package org.vosao.entity;

import static org.vosao.utils.EntityUtil.*;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

public class TemplateEntity extends BaseEntityImpl {

	private static final long serialVersionUID = 2L;

	private String title;
	private String url;
	private String content;
	
	public TemplateEntity() {
		content = "";
	}
	
	@Override
	public void load(Entity entity) {
		super.load(entity);
		title = getStringProperty(entity, "title");
		url = getStringProperty(entity, "url");
		content = getTextProperty(entity, "content");
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		setProperty(entity, "title", title, false);
		setProperty(entity, "url", url, true);
		setTextProperty(entity, "content", content);
	}

	public TemplateEntity(String title, String content, String url) {
		this(title, content);
		this.url = url;
	}

	public TemplateEntity(String title, String content) {
		this();
		this.title = title;
		this.content = content;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
