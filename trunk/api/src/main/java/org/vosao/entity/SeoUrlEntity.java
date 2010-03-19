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

import com.google.appengine.api.datastore.Entity;

/**
 * SEO Urls plugin link data.
 * @author Alexander Oleynik
 */
public class SeoUrlEntity extends BaseEntityImpl {

	private static final long serialVersionUID = 2L;

	private String fromLink;
	private String toLink;

	public SeoUrlEntity() {
	}
	
	@Override
	public void load(Entity entity) {
		super.load(entity);
		fromLink = getStringProperty(entity, "fromLink");
		toLink = getStringProperty(entity, "toLink");
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		entity.setProperty("fromLink", fromLink);
		entity.setUnindexedProperty("toLink", toLink);
	}

	public SeoUrlEntity(String aFrom, String aTo) {
		this();
		fromLink = aFrom;
		toLink = aTo;
	}
	
	public String getFromLink() {
		return fromLink;
	}
	
	public void setFromLink(String aFrom) {
		fromLink = aFrom;
	}

	public String getToLink() {
		return toLink;
	}

	public void setToLink(String to) {
		this.toLink = to;
	}

}
