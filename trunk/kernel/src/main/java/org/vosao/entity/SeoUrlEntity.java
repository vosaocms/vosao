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

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * SEO Urls plugin link data.
 * @author Alexander Oleynik
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class SeoUrlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;
	
	@Persistent
	private String fromLink;
	
	@Persistent
	private String toLink;

	public SeoUrlEntity() {
	}
	
	public SeoUrlEntity(String aFrom, String aTo) {
		this();
		fromLink = aFrom;
		toLink = aTo;
	}
	
	public void copy(final SeoUrlEntity entity) {
		setFromLink(entity.getFromLink());
		setToLink(entity.getToLink());
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
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

	public boolean equals(Object object) {
		if (object instanceof SeoUrlEntity) {
			SeoUrlEntity entity = (SeoUrlEntity)object;
			if (getId() == null && entity.getId() == null) {
				return true;
			}
			if (getId() != null && getId().equals(entity.getId())) {
				return true;
			}
		}
		return false;
	}
	
}
