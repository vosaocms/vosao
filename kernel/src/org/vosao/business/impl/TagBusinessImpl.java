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

package org.vosao.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.TagBusiness;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.PageTagEntity;
import org.vosao.entity.TagEntity;
import org.vosao.i18n.Messages;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class TagBusinessImpl extends AbstractBusinessImpl 
	implements TagBusiness {

	private static final Pattern TAG_NAME_DISALLOWED = Pattern.compile("[/ ]"); 
	
	@Override
	public List<TreeItemDecorator<TagEntity>> getTree() {
		List<TreeItemDecorator<TagEntity>> result = 
				new ArrayList<TreeItemDecorator<TagEntity>>();
		List<TagEntity> tags = getDao().getTagDao().select();
		Map<Long, TreeItemDecorator<TagEntity>> buf = 
			new HashMap<Long, TreeItemDecorator<TagEntity>>();
		for (TagEntity tag : tags) {
			buf.put(tag.getId(), new TreeItemDecorator<TagEntity>(tag, null));
		}
		for (Long id : buf.keySet()) {
			TreeItemDecorator<TagEntity> tag = buf.get(id);
			if (tag.getEntity().getParent() == null) {
				result.add(tag);
			}
			else {
				TreeItemDecorator<TagEntity> parent = buf.get(
						tag.getEntity().getParent());
				if (parent != null) {
					parent.getChildren().add(tag);
					tag.setParent(parent);
				}
			}
		}
		return result;
	}

	@Override
	public String validateBeforeSave(TagEntity tag) {
		if (StringUtils.isEmpty(tag.getTitle())) {
			return Messages.get("title_is_empty");
		}
		if (StringUtils.isEmpty(tag.getName())) {
			return Messages.get("name_is_empty");
		}
		else {
			TagEntity found = getDao().getTagDao().getByName(tag.getParent(), 
					tag.getName());
			if (tag.isNew()) {
				if (found != null) {
					return Messages.get("tag_already_exists");
				}
			}
			else {
				if (found != null && !found.getId().equals(tag.getId())) {
					return Messages.get("tag_already_exists");
				}	
			}
		}
		if (TAG_NAME_DISALLOWED.matcher(tag.getName()).find()) {
			return Messages.get("tag_wrong_symbol");
		}
		return null;
	}

	@Override
	public void remove(Long id) {
		List<PageTagEntity> pageTags = getDao().getPageTagDao().select();
		for (PageTagEntity pageTag : pageTags) {
			if (pageTag.getTags().contains(id)) {
				pageTag.getTags().remove(id);
				getDao().getPageTagDao().save(pageTag);
			}
		}
		removeTree(id);
	}
	
	private void removeTree(Long id) {
		List<TagEntity> children = getDao().getTagDao().selectByParent(id);
		for (TagEntity child : children) {
			removeTree(child.getId());
		}
		getDao().getTagDao().remove(id);
	}

	@Override
	public TreeItemDecorator<TagEntity> getTree(String name) {
		for (TreeItemDecorator<TagEntity> item : getTree()) {
			if (item.getEntity().getName().equals(name)) {
				return item;
			}
		}
		return null;
	}

	@Override
	public TagEntity getByPath(String tagPath) {
		int start = tagPath.startsWith("/") ? 1 : 0;
		String[] names = tagPath.substring(start).split("/");
		Long parent = null;
		TagEntity tag = null;
		for (String name : names) {
			tag = getDao().getTagDao().getByName(parent, name);
			if (tag == null) {
				return null;
			}
			parent = tag.getId();
		}
		return tag;
	}

	@Override
	public String getPath(TagEntity tag) {
		String result = tag.getName();
		Long parent = tag.getParent();
		while (parent != null) {
			TagEntity parentTag = getDao().getTagDao().getById(parent);
			if (parentTag != null) {
				result = parentTag.getName() + "/" + result;
				parent = parentTag.getParent();
			}
			else {
				logger.error("Tag not found " + parent);
				parent = null;
			}
		}
		return "/" + result;
	}

	@Override
	public void addTag(String pageURL, TagEntity tag) {
		PageTagEntity pageTag = getDao().getPageTagDao().getByURL(pageURL);
		if (pageTag == null) {
			pageTag = new PageTagEntity(pageURL);
		}
		if (!pageTag.getTags().contains(tag.getId())) {
			pageTag.getTags().add(tag.getId());
		}
		getDao().getPageTagDao().save(pageTag);
		if (!tag.getPages().contains(pageURL)) {
			tag.getPages().add(pageURL);
			getDao().getTagDao().save(tag);
		}
	}

	@Override
	public void removeTag(String pageURL, TagEntity tag) {
		PageTagEntity pageTag = getDao().getPageTagDao().getByURL(pageURL);
		if (pageTag != null) {
			if (pageTag.getTags().contains(tag.getId())) {
				pageTag.getTags().remove(tag.getId());
				getDao().getPageTagDao().save(pageTag);
			}
		}
		if (tag.getPages().contains(pageURL)) {
			tag.getPages().remove(pageURL);
			getDao().getTagDao().save(tag);
		}
	}

	
}
