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

package org.vosao.entity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

/**
 * @author Alexander Oleynik
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class CommentEntity implements Serializable {

	private static final long serialVersionUID = 7L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;

	@Persistent
	private String pageUrl;
	
	@Persistent
	private String name;
	
	@Persistent(defaultFetchGroup = "true")
	private Text content;
	
	@Persistent
	private Date publishDate;
	
	@Persistent
	private boolean disabled;

	public CommentEntity() {
		publishDate = new Date();
	}
	
	public CommentEntity(final String aName, final String aContent, 
			final Date aPublishDate, final String aPageUrl) {
		setName(aName);
		setContent(aContent);
		setPublishDate(aPublishDate);
		setPageUrl(aPageUrl);
		setDisabled(false);
	}

	public CommentEntity(final String aName, final String aContent, 
			final Date aPublishDate, final String aPageUrl, 
			final boolean aDisabled) {
		this(aName, aContent, aPublishDate, aPageUrl);
		setDisabled(aDisabled);
	}
	
	public void copy(final CommentEntity entity) {
		setName(entity.getName());
		setContent(entity.getContent());
		setPublishDate(entity.getPublishDate());
		setPageUrl(entity.getPageUrl());
		setDisabled(entity.isDisabled());
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String value) {
		this.name = value;
	}
	
	public String getContent() {
		if (content == null) {
			return null;
		}
		return content.getValue();
	}
	
	public void setContent(String content) {
		this.content = new Text(content);
	}
	
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public boolean equals(Object object) {
		if (object instanceof CommentEntity) {
			CommentEntity entity = (CommentEntity)object;
			if (getId() == null && entity.getId() == null) {
				return true;
			}
			if (getId() != null && getId().equals(entity.getId())) {
				return true;
			}
		}
		return false;
	}
	
}
