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

import static org.vosao.utils.EntityUtil.getLongProperty;
import static org.vosao.utils.EntityUtil.getStringProperty;
import static org.vosao.utils.EntityUtil.getListProperty;
import static org.vosao.utils.EntityUtil.setProperty;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Entity;

/**
 * @author Alexander Oleynik
 */
public class TagEntity extends BaseEntityImpl {

	private static final long serialVersionUID = 1L;

	private Long parent;
	private String name;
	private List<String> pages;
	
	public TagEntity() {
		pages = new ArrayList<String>();
	}
	
	@Override
	public void load(Entity entity) {
		super.load(entity);
		parent = getLongProperty(entity, "parent");
		name = getStringProperty(entity, "name");
		pages = getListProperty(entity, "pages");
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		setProperty(entity, "name", name, true);
		setProperty(entity, "parent", parent, true);
		setProperty(entity, "pages", pages);
	}

	public TagEntity(Long aParent, String aName) {
		this();
		name = aName;
		parent = aParent;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public List<String> getPages() {
		return pages;
	}

	public void setPages(List<String> pages) {
		this.pages = pages;
	}
	
}
