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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.lang.StringUtils;
import org.vosao.enums.PageState;
import org.vosao.enums.PageType;
import org.vosao.utils.DateUtil;
import org.vosao.utils.UrlUtil;

import com.google.appengine.api.datastore.Text;

/**
 * @author Alexander Oleynik
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PageEntity implements BaseEntity {

	private static final long serialVersionUID = 8L;
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;
	
	/**
	 * Titles are stored in string list. Content language stored in first two 
	 * chars. 
	 */
	@Persistent(defaultFetchGroup = "true")
	private Text title;

	@NotPersistent
	private Map<String, String> titles;

	@Persistent
	private String friendlyURL;
	
	@Persistent
	private String parentUrl;

	@Persistent
	private String template;
	
	@Persistent
	private Date publishDate;
	
	@Persistent
	private boolean commentsEnabled;

	@Persistent
	private Integer version;
	
	@Persistent
	private String versionTitle;

	@Persistent
	private String state;
	
	@Persistent
	private String createUserEmail;
	
	@Persistent
	private Date createDate;
	
	@Persistent
	private String modUserEmail;
	
	@Persistent
	private Date modDate;
	
	@Persistent
	private String pageType;
	
	@Persistent
	private String structureId;
	
	@Persistent
	private String structureTemplateId;
	
	@Persistent(defaultFetchGroup = "true")
	private Text keywords;
	
	@Persistent(defaultFetchGroup = "true")
	private Text description;

	@Persistent
	private boolean searchable;

	public PageEntity() {
		publishDate = new Date();
		state = PageState.EDIT.name();
		version = 1;
		versionTitle = "New page";
		createDate = new Date();
		modDate = createDate;
		createUserEmail = "";
		modUserEmail = "";
		pageType = PageType.SIMPLE.name();
		setKeywords("");
		setDescription("");
		setTitle("");
		searchable = true;
	}
	
	public PageEntity(String title, String friendlyURL, 
			String aTemplate, Date publish) {
		this(title, friendlyURL, aTemplate);
		publishDate = publish;
	}

	public PageEntity(String title, String friendlyURL,  
			String aTemplate) {
		this(title, friendlyURL);
		template = aTemplate;
	}

	public PageEntity(String aTitle, String aFriendlyURL) {
		this();
		setTitle(aTitle);
		setFriendlyURL(aFriendlyURL);
	}
	
	@Override
	public Object getEntityId() {
		return id;
	}

	public void copy(final PageEntity entity) {
		setTitleValue(entity.getTitleValue());
		setFriendlyURL(entity.getFriendlyURL());
		setTemplate(entity.getTemplate());
		setPublishDate(entity.getPublishDate());
		setCommentsEnabled(entity.isCommentsEnabled());
		setVersion(entity.getVersion());
		setVersionTitle(entity.getVersionTitle());
		setState(entity.getState());
		setCreateDate(entity.getCreateDate());
		setCreateUserEmail(entity.getCreateUserEmail());
		setModDate(entity.getModDate());
		setModUserEmail(entity.getModUserEmail());
		setPageType(entity.getPageType());
		setStructureId(entity.getStructureId());
		setStructureTemplateId(entity.getStructureTemplateId());
		setKeywords(entity.getKeywords());
		setDescription(entity.getDescription());
		setSearchable(entity.isSearchable());
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFriendlyURL() {
		return friendlyURL;
	}
	
	public void setFriendlyURL(String aFriendlyURL) {
		friendlyURL = aFriendlyURL;
		parentUrl = getParentFriendlyURL();
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
	public String getParentFriendlyURL() {
		return UrlUtil.getParentFriendlyURL(getFriendlyURL());
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
		return UrlUtil.getPageFriendlyURL(getFriendlyURL());
	}

	public void setPageFriendlyURL(final String url) {
		if (getFriendlyURL() == null) {
			setFriendlyURL(url);
		}
		else {
			if (parentUrl.equals("/")) {
				friendlyURL = parentUrl + url;
			}
			else {
				friendlyURL = parentUrl + "/" + url;
			}
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
	
	public boolean isRoot() {
		return friendlyURL.equals("/");
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getVersionTitle() {
		return versionTitle;
	}

	public void setVersionTitle(String versionTitle) {
		this.versionTitle = versionTitle;
	}

	public PageState getState() {
		return PageState.valueOf(state);
	}

	public String getStateString() {
		return state;
	}

	public void setState(PageState aState) {
		this.state = aState.name();
	}

	public String getCreateUserEmail() {
		return createUserEmail;
	}

	public void setCreateUserEmail(String createUser) {
		this.createUserEmail = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getModUserEmail() {
		return modUserEmail;
	}

	public void setModUserEmail(String modUser) {
		this.modUserEmail = modUser;
	}

	public Date getModDate() {
		return modDate;
	}

	public String getModDateString() {
		return DateUtil.dateTimeToString(modDate);
	}

	public String getCreateDateString() {
		return DateUtil.dateTimeToString(createDate);
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}
	
	public boolean isApproved() {
		return state.equals(PageState.APPROVED.name());
	}

	public String getParentUrl() {
		return parentUrl;
	}

	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}

	public PageType getPageType() {
		return pageType == null ? null : PageType.valueOf(pageType);
	}

	public String getPageTypeString() {
		return pageType;
	}
	
	public void setPageType(PageType pageType) {
		this.pageType = pageType.name();
	}

	public String getStructureId() {
		return structureId;
	}

	public void setStructureId(String structureId) {
		this.structureId = structureId;
	}

	public String getStructureTemplateId() {
		return structureTemplateId;
	}

	public void setStructureTemplateId(String structureTemplateId) {
		this.structureTemplateId = structureTemplateId;
	}
	
	public boolean isSimple() {
		if (pageType != null) {
			return getPageType().equals(PageType.SIMPLE);
		}
		return true;
	}

	public boolean isStructured() {
		if (pageType != null) {
			return getPageType().equals(PageType.STRUCTURED);
		}
		return false;
	}

	public String getKeywords() {
		return keywords == null ? null : keywords.getValue();
	}

	public void setKeywords(String keywords) {
		this.keywords = new Text(keywords);
	}

	public String getDescription() {
		return description == null ? null : description.getValue();
	}

	public void setDescription(String description) {
		this.description = new Text(description);
	}

	public String getTitleValue() {
		return title == null ? null : title.getValue();
	}

	public void setTitleValue(String t) {
		title = new Text(t);
		parseTitle();
	}

	public String getTitle() {
		return getLocalTitle("en");
	}

	public void setTitle(String title) {
		setLocalTitle(title, "en");
	}

	public String getLocalTitle(String lang) {
		parseTitle();
		return titles.get(lang);
	}

	public void setLocalTitle(String title, String lang) {
		parseTitle();
		titles.put(lang, title);
		packTitle();
	}
	
	private void parseTitle() {
		if (title == null) {
			titles = new HashMap<String, String>();
		}
		else {
			for (String s : getTitleValue().split(",")) {
				titles.put(s.substring(0, 2), s.substring(2)); 
			}
		}
	}
	
	private void packTitle() {
		if (titles != null) {
			StringBuffer s = new StringBuffer();
			int count = 0;
			for (String lang : titles.keySet()) {
				if (count++ > 0) {
					s.append(",");
				}
				s.append(lang).append(titles.get(lang));
			}
			setTitleValue(s.toString());
		}
	}

	public Map<String, String> getTitles() {
		return titles;
	}

	public void setTitles(Map<String, String> titles) {
		this.titles = titles;
		packTitle();
	}

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}
	
}
