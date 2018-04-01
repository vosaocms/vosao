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

import static org.vosao.utils.EntityUtil.*;
import com.google.appengine.api.datastore.Entity;

public class FolderEntity extends BaseEntityImpl {

	private static final long serialVersionUID = 4L;

	private String title;
	private String name;
	private Long parentId;
	
	public FolderEntity() {
	}
	
	@Override
	public void load(Entity entity) {
		super.load(entity);
		title = getStringProperty(entity, "title");
		name = getStringProperty(entity, "name");
		parentId = getLongProperty(entity, "parentId");
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		setProperty(entity, "title", title, false);
		setProperty(entity, "name", name, true);
		setProperty(entity, "parentId", parentId, true);
	}

	public FolderEntity(String aName) {
		this();
		name = aName;
		title = aName;
	}
	
	public FolderEntity(String aName, Long aParent) {
		this(aName);
		parentId = aParent;
	}

	public FolderEntity(String aTitle, String aName, Long aParent) {
		this(aName, aParent);
		title = aTitle;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Long getParent() {
		return parentId;
	}

	public void setParent(Long parent) {
		this.parentId = parent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isRoot() {
		return parentId == null;
	}
	
}
