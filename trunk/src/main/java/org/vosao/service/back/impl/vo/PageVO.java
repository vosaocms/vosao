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

package org.vosao.service.back.impl.vo;

import java.util.ArrayList;
import java.util.List;

import org.vosao.entity.PageEntity;
import org.vosao.utils.DateUtil;

/**
 * Value object to be returned from services.
 * @author Alexander Oleynik
 */
public class PageVO {

    private PageEntity page;

	public PageVO(final PageEntity entity) {
		page = entity;
	}

	public static List<PageVO> create(List<PageEntity> list) {
		List<PageVO> result = new ArrayList<PageVO>();
		for (PageEntity entity : list) {
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

	public String getFriendlyURL() {
		return page.getFriendlyURL();
	}

	public String getParentFriendlyURL() {
		return page.getParentFriendlyURL();
	}

	public String getParent() {
		return page.getParent();
	}

	public String getTemplate() {
		return page.getTemplate();
	}

	public String getPublishDate() {
		return DateUtil.toString(page.getPublishDate());
	}
	
	public boolean isCommentEnabled() {
		return page.isCommentsEnabled();
	}
	
}
