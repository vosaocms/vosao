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

package org.vosao.service.back.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.datanucleus.util.StringUtils;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.common.Messages;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PageTagEntity;
import org.vosao.entity.TagEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.TagService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.PageVO;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class TagServiceImpl extends AbstractServiceImpl 
		implements TagService {

	@Override
	public List<TreeItemDecorator<TagEntity>> getTree() {
		return getBusiness().getTagBusiness().getTree();
	}

	@Override
	public TagEntity getById(Long id) {
		return getDao().getTagDao().getById(id);
	}

	@Override
	public ServiceResponse save(Map<String, String> vo) {
		TagEntity tag = null;
		if (!StringUtils.isEmpty(vo.get("id"))) {
			tag = getDao().getTagDao().getById(Long.valueOf(vo.get("id")));
		}
		if (tag == null) {
			tag = new TagEntity();
		}
		if (StringUtils.isEmpty(vo.get("parent"))) {
			tag.setParent(null);
		}
		else {
			tag.setParent(Long.valueOf(vo.get("parent")));
		}
		tag.setName(vo.get("name"));
		String error = getBusiness().getTagBusiness().validateBeforeSave(tag);
		if (error == null) {
			getDao().getTagDao().save(tag);
			return ServiceResponse.createSuccessResponse(
					Messages.get("tag.success_save"));
		}
		else {
			return ServiceResponse.createErrorResponse(error);
		}
	}

	@Override
	public ServiceResponse remove(Long id) {
		getBusiness().getTagBusiness().remove(id);
		return ServiceResponse.createSuccessResponse(
				Messages.get("tag.success_remove"));
	}

	@Override
	public ServiceResponse addTag(String pageURL, Long tagId) {
		TagEntity tag = getDao().getTagDao().getById(tagId);
		if (tag == null) {
			logger.error("Tag not found " + tagId);
		}
		else {
			getBusiness().getTagBusiness().addTag(pageURL, tag);
		}
		return ServiceResponse.createSuccessResponse(
				Messages.get("tag.success_add"));
	}

	@Override
	public ServiceResponse removeTag(String pageURL, Long tagId) {
		TagEntity tag = getDao().getTagDao().getById(tagId);
		if (tag != null) {
			getBusiness().getTagBusiness().removeTag(pageURL, tag);
		}
		return ServiceResponse.createSuccessResponse(
				Messages.get("tag.success_remove"));
	}

	@Override
	public List<PageVO> getPages(Long tagId) {
		List<PageVO> result = new ArrayList<PageVO>();
		TagEntity tag = getById(tagId);
		if (tag != null) {
			for (String url : tag.getPages()) {
				PageEntity page = getBusiness().getPageBusiness().getByUrl(url);
				if (page != null) {
					result.add(new PageVO(page));
				}
			}
		}
		return result;
	}
	
}
