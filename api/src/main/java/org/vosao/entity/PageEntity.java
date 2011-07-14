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

import static org.vosao.utils.EntityUtil.getBooleanProperty;
import static org.vosao.utils.EntityUtil.getDateProperty;
import static org.vosao.utils.EntityUtil.getIntegerProperty;
import static org.vosao.utils.EntityUtil.getLongProperty;
import static org.vosao.utils.EntityUtil.getStringProperty;
import static org.vosao.utils.EntityUtil.getTextProperty;
import static org.vosao.utils.EntityUtil.setProperty;
import static org.vosao.utils.EntityUtil.setTextProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.vosao.common.VosaoContext;
import org.vosao.entity.field.PageAttributesField;
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
	private Date endPublishDate;
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
	private boolean wikiProcessing;
	private String headHtml;
	private boolean skipPostProcessing;
	private boolean cached;
	private String contentType;
	private boolean enableCkeditor;
	private String attributes;
	private boolean restful;

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
		wikiProcessing = false;
		skipPostProcessing = false;
		cached = true;
		enableCkeditor = true;
	}
	
	@Override
	public void load(Entity entity) {
		super.load(entity);
		title = getTextProperty(entity, "title");
		friendlyURL = getStringProperty(entity, "friendlyURL");
		parentUrl = getStringProperty(entity, "parentUrl");
		template = getLongProperty(entity, "template");
		publishDate = getDateProperty(entity, "publishDate");
		endPublishDate = getDateProperty(entity, "endPublishDate");
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
		cached = getBooleanProperty(entity, "cached", true);
		contentType = getStringProperty(entity, "contentType");
		wikiProcessing = getBooleanProperty(entity, "wikiProcessing", false);
		enableCkeditor = getBooleanProperty(entity, "enableCkeditor", true);
		attributes = getStringProperty(entity, "attributes");
		restful = getBooleanProperty(entity, "restful", false);
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		setTextProperty(entity, "title", title);
		setProperty(entity, "friendlyURL", friendlyURL, true);
		setProperty(entity, "parentUrl", parentUrl, true);
		setProperty(entity, "template", template, true);
		setProperty(entity, "publishDate", publishDate, true);
		setProperty(entity, "endPublishDate", endPublishDate, true);
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
		setProperty(entity, "cached", cached, false);
		setProperty(entity, "contentType", contentType, false);
		setProperty(entity, "wikiProcessing", wikiProcessing, false);
		setProperty(entity, "enableCkeditor", enableCkeditor, false);
		setProperty(entity, "attributes", attributes, false);
		setProperty(entity, "restful", restful, false);
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

	public String getPublishTimeString() {
		return DateUtil.timeToString(publishDate);
	}

	public String getEndPublishDateString() {
		return DateUtil.toString(endPublishDate);
	}

	public String getEndPublishTimeString() {
		return DateUtil.timeToString(endPublishDate);
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
		return getLocalTitle(VosaoContext.getInstance().getConfig()
				.getDefaultLanguage());
	}

	public void setTitle(String title) {
		setLocalTitle(title, VosaoContext.getInstance().getConfig()
				.getDefaultLanguage());
	}

	public String getLocalTitle(String lang) {
		parseTitle();
		if (!VosaoContext.getInstance().getConfig()
				.getDefaultLanguage().equals(lang)) {
			if (StringUtils.isEmpty(titles.get(lang))) {
				return titles.get(VosaoContext.getInstance().getConfig()
						.getDefaultLanguage());
			}
		}
		return titles.get(lang);
	}

	public String getLocalTitle() {
		return getLocalTitle(VosaoContext.getInstance().getLanguage());
	}
	
	public void setLocalTitle(String title, String lang) {
		parseTitle();
		titles.put(lang, title);
		packTitle();
	}
	
	private void parseTitle() {
		titles = new HashMap<String, String>();
		if (title != null) {
			try {
				JSONObject obj = new JSONObject(getTitleValue());
				Iterator<String> it = obj.keys();
				while (it.hasNext()) {
					String key = it.next();
					titles.put(key, obj.getString(key));
				}
			} catch (org.json.JSONException e) {
				logger.error("Page title parsing problem: " + getTitleValue());
			}
		}
	}
	
	private void packTitle() {
		if (titles != null) {
			JSONObject obj = new JSONObject(titles);
			setTitleValue(obj.toString());
		}
	}

	public Map<String, String> getTitles() {
		parseTitle();
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
		for (String p : FolderUtil.getPathChain(getFriendlyURL())) {
			 s.append("/").append(p);
			 result.add(s.toString());
		}
		return result;
	}

	public boolean isCached() {
		return cached;
	}

	public void setCached(boolean cached) {
		this.cached = cached;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public boolean isWikiProcessing() {
		return wikiProcessing;
	}

	public void setWikiProcessing(boolean wikiProcessing) {
		this.wikiProcessing = wikiProcessing;
	}
	
	public boolean isForInternalUse() {
		if (getFriendlyURL() != null) {
			return getFriendlyURL().endsWith("/_default");
		}
		return false;
	}

	public Date getEndPublishDate() {
		return endPublishDate;
	}

	public void setEndPublishDate(Date endPublishDate) {
		this.endPublishDate = endPublishDate;
	}

	public boolean isEnableCkeditor() {
		return enableCkeditor;
	}

	public void setEnableCkeditor(boolean value) {
		this.enableCkeditor = value;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	
	private PageAttributesField attribute;
	
	public PageAttributesField getAttribute() {
		if (attribute == null) {
			attribute = new PageAttributesField(getAttributes());
		}
		return attribute;
	}
	
	public void setAttribute(String name, String language, String value) {
		getAttribute().set(name, language, value);
		setAttributes(getAttribute().asJSON());
	}
	
	public boolean isRestful() {
		return restful;
	}

	public void setRestful(boolean restful) {
		this.restful = restful;
	}
	
	public boolean isPublished() {
		return isPublished(new Date());
	}
	
	public boolean isPublished(Date date) {
		if (getPublishDate() == null) {
			return true;
		}
		if (getEndPublishDate() == null) {
			return getPublishDate().before(date);
		}
		return getPublishDate().before(date) && getEndPublishDate().after(date);
	}
}
