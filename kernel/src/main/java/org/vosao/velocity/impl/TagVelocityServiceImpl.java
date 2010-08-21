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

package org.vosao.velocity.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.vosao.business.Business;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.common.AbstractServiceBeanImpl;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PageTagEntity;
import org.vosao.entity.TagEntity;
import org.vosao.entity.helper.PageHelper;
import org.vosao.utils.ListUtil;
import org.vosao.velocity.TagVelocityService;

/**
 * 
 * @author Alexander Oleynik
 *
 */
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
		Collections.sort(result, PageHelper.PUBLISH_DATE);
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
	public List<PageEntity> getPagesByPath(String tagPaths) {
		String[] paths = tagPaths.replace(" ", "").split(",");
		if (paths.length == 0) {
			return Collections.EMPTY_LIST;
		}
		Set<PageEntity> pages = null;
		for (String tagPath : paths) {
			TagEntity tag = getBusiness().getTagBusiness().getByPath(tagPath);
			if (tag != null) {
				if (pages == null) {
					pages = getPagesByTag(tag.getId());
				}
				else {
					pages.retainAll(getPagesByTag(tag.getId()));
				}
			}
		}
		List<PageEntity> result = new ArrayList<PageEntity>(pages);
		Collections.sort(result, PageHelper.PUBLISH_DATE);
		return result;
	}
	
	private Set<PageEntity> getPagesByTag(long tagId) {
		Set<PageEntity> result = new HashSet<PageEntity>(getPagesById(tagId));
		for (TagEntity tag : getDao().getTagDao().selectByParent(tagId)) {
			result.addAll(getPagesByTag(tag.getId()));
		}
		return result;
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
