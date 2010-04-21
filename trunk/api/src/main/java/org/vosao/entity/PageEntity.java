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

import static org.vosao.utils.EntityUtil.getBooleanProperty;
import static org.vosao.utils.EntityUtil.getDateProperty;
import static org.vosao.utils.EntityUtil.getIntegerProperty;
import static org.vosao.utils.EntityUtil.getLongProperty;
import static org.vosao.utils.EntityUtil.getStringProperty;
import static org.vosao.utils.EntityUtil.getTextProperty;
import static org.vosao.utils.EntityUtil.setProperty;
import static org.vosao.utils.EntityUtil.setTextProperty;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.vosao.enums.PageState;
import org.vosao.enums.PageType;
import org.vosao.utils.DateUtil;
import org.vosao.utils.FolderUtil;
import org.vosao.utils.UrlUtil;

import com.google.appengine.api.datastore.Entity;

/**
 * @author Alexander Oleynik
 */
public class PageEntity extends BaseEntityImpl {

	private static final long serialVersionUID = 9L;
	
	/**
	 * Titles are stored in string list. Content language stored in first two 
	 * chars. 
	 */
	private String title;
	private String friendlyURL;
	private String parentUrl;
	private Long template;
	private Date publishDate;
	private boolean commentsEnabled;
	private Integer version;
	private String versionTitle;
	private PageState state;
	private PageType pageType;
	private Long structureId;
	private Long structureTemplateId;
	private String keywords;
	private String description;
	private boolean searchable;
	private Integer sortIndex;
	private boolean velocityProcessing;
	private String headHtml;
	private boolean skipPostProcessing;

	// not persisted
	private Map<String, String> titles;
	
	public PageEntity() {
		publishDate = new Date();
		state = PageState.EDIT;
		version = 1;
		versionTitle = "New page";
		pageType = PageType.SIMPLE;
		setKeywords("");
		setDescription("");
		setTitle("");
		setHeadHtml("");
		searchable = true;
		sortIndex = 0;
		velocityProcessing = false;
		skipPostProcessing = false;
	}
	
	@Override
	public void load(Entity entity) {
		super.load(entity);
		title = getTextProperty(entity, "title");
		friendlyURL = getStringProperty(entity, "friendlyURL");
		parentUrl = getStringProperty(entity, "parentUrl");
		template = getLongProperty(entity, "template");
		publishDate = getDateProperty(entity, "publishDate");
		commentsEnabled = getBooleanProperty(entity, "commentsEnabled", false);
		version = getIntegerProperty(entity, "version", 1);
		versionTitle = getStringProperty(entity, "versionTitle");
		state = PageState.valueOf(getStringProperty(entity, "state"));
		pageType = PageType.valueOf(getStringProperty(entity, "pageType"));
		structureId = getLongProperty(entity, "structureId");
		structureTemplateId = getLongProperty(entity, "structureTemplateId");
		keywords = getTextProperty(entity, "keywords");
		description = getTextProperty(entity, "description");
		searchable = getBooleanProperty(entity, "searchable", true);
		sortIndex = getIntegerProperty(entity, "sortIndex", 0);
		velocityProcessing = getBooleanProperty(entity, "velocityProcessing", false);
		headHtml = getTextProperty(entity, "headHtml");
		skipPostProcessing = getBooleanProperty(entity, "skipPostProcessing", false);
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		setTextProperty(entity, "title", title);
		setProperty(entity, "friendlyURL", friendlyURL, true);
		setProperty(entity, "parentUrl", parentUrl, true);
		setProperty(entity, "template", template, true);
		setProperty(entity, "publishDate", publishDate, false);
		setProperty(entity, "commentsEnabled", commentsEnabled, false);
		setProperty(entity, "version", version, true);
		setProperty(entity, "versionTitle", versionTitle, false);
		setProperty(entity, "state", state.name(), false);
		setProperty(entity, "pageType", pageType.name(), false);
		setProperty(entity, "structureId", structureId, true);
		setProperty(entity, "structureTemplateId", structureTemplateId, true);
		setTextProperty(entity, "keywords", keywords);
		setTextProperty(entity, "description", description);
		setProperty(entity, "searchable", searchable, false);
		setProperty(entity, "sortIndex", sortIndex, false);
		setProperty(entity, "velocityProcessing", velocityProcessing, false);
		setTextProperty(entity, "headHtml", headHtml);
		setProperty(entity, "skipPostProcessing", skipPostProcessing, false);
	}

	public PageEntity(String title, String friendlyURL, 
			Long aTemplate, Date publish) {
		this(title, friendlyURL, aTemplate);
		publishDate = publish;
	}

	public PageEntity(String title, String friendlyURL,  
			Long aTemplate) {
		this(title, friendlyURL);
		template = aTemplate;
	}

	public PageEntity(String aTitle, String aFriendlyURL) {
		this();
		setTitle(aTitle);
		setFriendlyURL(aFriendlyURL);
	}
	
	public String getFriendlyURL() {
		return friendlyURL;
	}
	
	public void setFriendlyURL(String aFriendlyURL) {
		friendlyURL = aFriendlyURL;
		parentUrl = getParentFriendlyURL();
	}

	public Long getTemplate() {
		return template;
	}

	public void setTemplate(Long template) {
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
		return UrlUtil.getNameFromFriendlyURL(getFriendlyURL());
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
		return state;
	}

	public String getStateString() {
		return state.name();
	}

	public void setState(PageState aState) {
		this.state = aState;
	}

	public boolean isApproved() {
		return state.equals(PageState.APPROVED);
	}

	public String getParentUrl() {
		return parentUrl;
	}

	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}

	public PageType getPageType() {
		return pageType;
	}

	public String getPageTypeString() {
		return pageType.name();
	}
	
	public void setPageType(PageType pageType) {
		this.pageType = pageType;
	}

	public Long getStructureId() {
		return structureId;
	}

	public void setStructureId(Long structureId) {
		this.structureId = structureId;
	}

	public Long getStructureTemplateId() {
		return structureTemplateId;
	}

	public void setStructureTemplateId(Long structureTemplateId) {
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
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitleValue() {
		return title;
	}

	public void setTitleValue(String t) {
		title = t;
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
		if (!"en".equals(lang)) {
			if (StringUtils.isEmpty(titles.get(lang))) {
				return titles.get("en");
			}
		}
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

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	public boolean isVelocityProcessing() {
		return velocityProcessing;
	}

	public void setVelocityProcessing(boolean velocityProcessing) {
		this.velocityProcessing = velocityProcessing;
	}

	public String getHeadHtml() {
		return headHtml;
	}

	public void setHeadHtml(String headHtml) {
		this.headHtml = headHtml;
	}

	public boolean isSkipPostProcessing() {
		return skipPostProcessing;
	}

	public void setSkipPostProcessing(boolean skipPostProcessing) {
		this.skipPostProcessing = skipPostProcessing;
	}
	
	public List<String> getAncestorsURL() {
		List<String> result = new ArrayList<String>();
		if (isRoot()) {
			result.add(getFriendlyURL());
			return result;
		}
		if (getParentUrl().equals("/")) {
			result.add(getFriendlyURL());
			return result;
		}
		StringBuffer s = new StringBuffer();
		try {
			for (String p : FolderUtil.getPathChain(getFriendlyURL())) {
				 s.append("/").append(p);
				 result.add(s.toString());
			}
		}
		catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return result;
	}
}
