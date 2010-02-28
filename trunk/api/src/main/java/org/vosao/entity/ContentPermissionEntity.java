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
import java.util.List;

import org.vosao.enums.ContentPermissionType;

import com.google.appengine.api.datastore.Entity;

/**
 * @author Alexander Oleynik
 */
public class ContentPermissionEntity extends BaseNativeEntityImpl {

	private static final long serialVersionUID = 3L;

	private String url;
	private boolean allLanguages;
	private String languages;
	private ContentPermissionType permission;
    private Long groupId;
	
	public ContentPermissionEntity() {
		allLanguages = true;
	}
	
	public ContentPermissionEntity(String anUrl) {
		this();
		url = anUrl;
	}
	
	public ContentPermissionEntity(String anUrl, ContentPermissionType perm) {
		this(anUrl);
		permission = perm;
	}

	public ContentPermissionEntity(String anUrl, ContentPermissionType perm,
			Long aGroupId) {
		this(anUrl, perm);
		groupId = aGroupId;
	}

	@Override
	public void load(Entity entity) {
		super.load(entity);
		url = getStringProperty(entity, "url");
		allLanguages = getBooleanProperty(entity, "allLanguages", true);
		languages = getStringProperty(entity, "languages");
		permission = ContentPermissionType.valueOf(getStringProperty(entity, 
				"permission"));
		groupId = getLongProperty(entity, "groupId", 0);
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		entity.setProperty("url", url);
		entity.setProperty("allLanguages", allLanguages);
		entity.setProperty("languages", languages);
		entity.setProperty("permission", permission.name());
		entity.setProperty("groupId", groupId);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ContentPermissionType getPermission() {
		return permission;
	}

	public void setPermission(ContentPermissionType permission) {
		this.permission = permission;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public boolean isAllLanguages() {
		return allLanguages;
	}

	public void setAllLanguages(boolean allLanguages) {
		this.allLanguages = allLanguages;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}
	
	public List<String> getLanguagesList() {
		List<String> result = new ArrayList<String>();
		if (languages != null) {
			for (String lang : languages.split(",")) {
				result.add(lang);
			}
		}
		return result;
	}
	
	public boolean isRead() {
		return permission.equals(ContentPermissionType.READ);
	}

	public boolean isWrite() {
		return permission.equals(ContentPermissionType.WRITE);
	}
	
	public boolean isPublish() {
		return permission.equals(ContentPermissionType.PUBLISH);
	}
	
	public boolean isDenied() {
		return permission.equals(ContentPermissionType.DENIED);
	}

	public boolean isAdmin() {
		return permission.equals(ContentPermissionType.ADMIN);
	}

	public boolean isChangeGranted() {
		return isWrite() || isPublish() || isAdmin();
	}
	
	public boolean isPublishGranted() {
		return isPublish() || isAdmin();
	}
	
	public boolean isMyPermissionHigher(ContentPermissionEntity perm) {
		return getPermission().ordinal() > perm.getPermission().ordinal();
	}
	
}
