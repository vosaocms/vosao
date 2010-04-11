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

package org.vosao.velocity.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vosao.business.Business;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.common.AbstractServiceBeanImpl;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PageTagEntity;
import org.vosao.entity.TagEntity;
import org.vosao.utils.ListUtil;
import org.vosao.velocity.TagVelocityService;

public class TagVelocityServiceImpl extends AbstractServiceBeanImpl implements
		TagVelocityService {

	public TagVelocityServiceImpl(Business business) {
		super(business);
	}
	
	@Override
	public List<TreeItemDecorator<TagEntity>> getTrees() {
		return getBusiness().getTagBusiness().getTree();
	}

	@Override
	public TreeItemDecorator<TagEntity> getTree(String name) {
		return getBusiness().getTagBusiness().getTree(name);
	}

	@Override
	public List<PageEntity> getPagesById(Long tagId) {
		TagEntity tag = getDao().getTagDao().getById(tagId);
		List<PageEntity> result = new ArrayList<PageEntity>();
		if (tag != null) {
			for (String url : tag.getPages()) {
				PageEntity page = getBusiness().getPageBusiness().getByUrl(url);
				if (page != null) {
					result.add(page);
				}
			}
		}
		return result;
	}

	@Override
	public List<TagEntity> getTags(String pageURL) {
		PageTagEntity pageTag = getDao().getPageTagDao().getByURL(pageURL);
		if (pageTag != null) {
			return getDao().getTagDao().getById(pageTag.getTags());
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<PageEntity> getPagesByPath(String tagPath) {
		TagEntity tag = getBusiness().getTagBusiness().getByPath(tagPath);
		if (tag != null) {
			return getPagesById(tag.getId());
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<PageEntity> getPagesById(Long tagId, int index, int count) {
		return ListUtil.slice(getPagesById(tagId), index, count);
	}

	@Override
	public List<PageEntity> getPagesByPath(String tagPath, int index, int count) {
		return ListUtil.slice(getPagesByPath(tagPath), index, count);
	}

}
