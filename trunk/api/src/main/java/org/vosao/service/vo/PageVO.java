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

package org.vosao.service.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.vosao.entity.PageEntity;
import org.vosao.utils.DateUtil;

/**
 * Value object to be returned from services.
 * @author Alexander Oleynik
 */
public class PageVO {

    private PageEntity page;
    private boolean hasPublishedVersion;

	public PageVO(final PageEntity entity) {
		page = entity;
	}

	public static List<PageVO> create(Collection<PageEntity> pages) {
		List<PageVO> result = new ArrayList<PageVO>();
		for (PageEntity entity : pages) {
			result.add(new PageVO(entity));
		}
		return result;
	}

	public String getId() {
		return page.getId();
	}

	public String getTitle() {
		return page.getTitle();
	}

	public Map<String, String> getTitles() {
		return page.getTitles();
	}

	public String getFriendlyURL() {
		return page.getFriendlyURL();
	}

	public String getParentFriendlyURL() {
		return page.getParentFriendlyURL();
	}

	public String getParentUrl() {
		return page.getParentUrl();
	}

	public String getTemplate() {
		return page.getTemplate();
	}

	public String getPublishDate() {
		return DateUtil.toString(page.getPublishDate());
	}
	
	public String getPublishDateString() {
		return DateUtil.toString(page.getPublishDate());
	}

	public boolean isCommentEnabled() {
		return page.isCommentsEnabled();
	}
	
	public Integer getVersion() {
		return page.getVersion();
	}

	public String getVersionTitle() {
		return page.getVersionTitle();
	}

	public String getState() {
		return page.getState().name();
	}
	
	public String getCreateUserEmail() {
		return page.getCreateUserEmail();
	}
	
	public String getCreateDate() {
		return DateUtil.dateTimeToString(page.getCreateDate());
	}

	public String getModUserEmail() {
		return page.getModUserEmail();
	}
	
	public String getModDate() {
		return DateUtil.dateTimeToString(page.getModDate());
	}

	public String getKeywords() {
		return page.getKeywords();
	}

	public String getDescription() {
		return page.getDescription();
	}

	public boolean isHasPublishedVersion() {
		return hasPublishedVersion;
	}

	public void setHasPublishedVersion(boolean value) {
		this.hasPublishedVersion = value;
	}
	
	
}
