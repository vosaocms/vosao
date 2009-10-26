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
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.vosao.utils.DateUtil;

import com.google.appengine.api.datastore.Text;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PageEntity implements Serializable {

	private static final long serialVersionUID = 7L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;
	
	@Persistent
	private String title;
	
	@Persistent(defaultFetchGroup = "true")
	private Text content;
	
	@Persistent
	private String friendlyURL;
	
	@Persistent
	private String parent;
	
	@Persistent
	private String template;
	
	@Persistent
	private Date publishDate;
	
	@Persistent
	private boolean commentsEnabled;
	
	
	public PageEntity() {
		publishDate = new Date();
	}
	
	public PageEntity(String title, String content,
			String friendlyURL, String aParent, String aTemplate,
			Date publish) {
		this(title, content, friendlyURL, aParent, aTemplate);
		publishDate = publish;
	}

	public PageEntity(String title, String content,
			String friendlyURL, String aParent, String aTemplate) {
		this(title, content, friendlyURL, aParent);
		template = aTemplate;
	}

	public PageEntity(String title, String content,
			String friendlyURL, String aParent) {
		this();
		this.title = title;
		this.content = new Text(content);
		this.friendlyURL = friendlyURL;
		this.parent = aParent;
	}
	
	public void copy(final PageEntity entity) {
		setTitle(entity.getTitle());
		setContent(entity.getContent());
		setFriendlyURL(entity.getFriendlyURL());
		setParent(entity.getParent());
		setTemplate(entity.getTemplate());
		setPublishDate(entity.getPublishDate());
		setCommentsEnabled(entity.isCommentsEnabled());
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
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
	
	public String getFriendlyURL() {
		return friendlyURL;
	}
	
	public void setFriendlyURL(String friendlyURL) {
		this.friendlyURL = friendlyURL;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
	public String getParentFriendlyURL() {
		if (getFriendlyURL() == null) {
			return "";
		}
		int lastSlash = getFriendlyURL().lastIndexOf('/');
		if (lastSlash == 0 || lastSlash == -1) {
			return "";
		}
		return getFriendlyURL().substring(0, lastSlash);
	}

	public void setParentFriendlyURL(final String url) {
		if (getFriendlyURL() == null) {
			setFriendlyURL(url);
		}
		else {
			setFriendlyURL(url + "/" + getPageFriendlyURL());
		}
	}

	public String getPageFriendlyURL() {
		if (getFriendlyURL() == null) {
			return "";
		}
		int lastSlash = getFriendlyURL().lastIndexOf('/');
		if (getFriendlyURL().equals("/") || lastSlash == -1) {
			return "";
		}
		return getFriendlyURL().substring(lastSlash + 1, getFriendlyURL().length());
	}

	public void setPageFriendlyURL(final String url) {
		if (getFriendlyURL() == null) {
			setFriendlyURL(url);
		}
		else {
			setFriendlyURL(getParentFriendlyURL() + "/" + url);
		}
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public String getPublishDateString() {
		return DateUtil.toString(publishDate);
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public boolean isCommentsEnabled() {
		return commentsEnabled;
	}

	public void setCommentsEnabled(boolean commentsEnabled) {
		this.commentsEnabled = commentsEnabled;
	}
	
	public boolean equals(Object object) {
		if (object instanceof PageEntity) {
			PageEntity entity = (PageEntity)object;
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
