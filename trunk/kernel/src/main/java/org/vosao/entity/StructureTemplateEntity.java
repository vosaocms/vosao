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

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.vosao.enums.StructureTemplateType;

import com.google.appengine.api.datastore.Text;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class StructureTemplateEntity implements BaseEntity {

	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;
	
	@Persistent
	String title;
	
	@Persistent
	String structureId;
	
	@Persistent
	StructureTemplateType type;
	
	@Persistent(defaultFetchGroup = "true")
	private Text content;
	
	public StructureTemplateEntity() {
	}
	
	public StructureTemplateEntity(String title, String structureId, 
			StructureTemplateType type, String content) {
		this();
		this.title = title;
		this.structureId = structureId;
		this.type = type;
		this.content = new Text(content);
	}
	
	@Override
	public Object getEntityId() {
		return id;
	}

	public void copy(final StructureTemplateEntity entity) {
		setTitle(entity.getTitle());
		setStructureId(entity.getStructureId());
		setType(entity.getType());
		setContent(entity.getContent());
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
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
		if (object instanceof StructureEntity) {
			StructureEntity entity = (StructureEntity)object;
			if (getId() == null && entity.getId() == null) {
				return true;
			}
			if (getId() != null && getId().equals(entity.getId())) {
				return true;
			}
		}
		return false;
	}

	public String getStructureId() {
		return structureId;
	}

	public void setStructureId(String structureId) {
		this.structureId = structureId;
	}

	public StructureTemplateType getType() {
		return type;
	}

	public String getTypeString() {
		return type != null ? type.name() : "null";
	}
	
	public void setType(StructureTemplateType value) {
		this.type = value;
	}

}
