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

import com.google.appengine.api.datastore.Text;

/**
 * @author Alexander Oleynik
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ContentEntity implements BaseEntity {

	private static final long serialVersionUID = 7L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;
	
	@Persistent
	private String parentClass;
	
	@Persistent
	private String parentKey;

	@Persistent
	private String languageCode;

	@Persistent(defaultFetchGroup = "true")
	private Text content;
	
	public ContentEntity() {
	}
	
	public ContentEntity(String parentClass, String parentKey, 
			String languageCode, String content) {
		this();
		this.parentClass = parentClass;
		this.parentKey = parentKey;
		this.languageCode = languageCode;
		setContent(content);
	}
	
	@Override
	public Object getEntityId() {
		return id;
	}

	public void copy(final ContentEntity entity) {
		setParentClass(entity.getParentClass());
		setParentKey(entity.getParentKey());
		setLanguageCode(entity.getLanguageCode());
		setContent(entity.getContent());
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getContent() {
		if (content == null) {
			return null;
		}
		return content.getValue();
	}
	
	public void setContent(String content) {
		this.content = new Text(content);
	}
	
	public boolean equals(Object object) {
		if (object instanceof ContentEntity) {
			ContentEntity entity = (ContentEntity)object;
			if (getId() == null && entity.getId() == null) {
				return true;
			}
			if (getId() != null && getId().equals(entity.getId())) {
				return true;
			}
		}
		return false;
	}

	public String getParentClass() {
		return parentClass;
	}

	public void setParentClass(String parentClass) {
		this.parentClass = parentClass;
	}

	public String getParentKey() {
		return parentKey;
	}

	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	
}
