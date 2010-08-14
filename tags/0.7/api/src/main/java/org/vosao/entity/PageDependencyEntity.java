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

import static org.vosao.utils.EntityUtil.getStringProperty;
import static org.vosao.utils.EntityUtil.setProperty;

import com.google.appengine.api.datastore.Entity;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class PageDependencyEntity extends BaseEntityImpl {

	private static final long serialVersionUID = 1L;

	private String dependency;
	private String page;

	public PageDependencyEntity() {
		dependency = "";
		page = "";
	}

	@Override
	public void load(Entity entity) {
		super.load(entity);
		dependency = getStringProperty(entity, "dependency");
		page = getStringProperty(entity, "page");
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		setProperty(entity, "dependency", dependency, true);
		setProperty(entity, "page", page, true);
	}

	public PageDependencyEntity(String dependency, String pageUrl) {
		setDependency(dependency);
		setPage(pageUrl);
	}

	public String getDependency() {
		return dependency;
	}

	public void setDependency(String url) {
		this.dependency = url;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
	
}
