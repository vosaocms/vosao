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

import com.google.appengine.api.datastore.Blob;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FileChunkEntity implements BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;

	@Persistent(serialized = "true", defaultFetchGroup = "true")
	private Blob data;

	@Persistent
	private int index;
	
	@Persistent
	private String fileId;
	
	
    public FileChunkEntity() {
    }
    
    public FileChunkEntity(String fileId, byte[] content, int index) {
		this();
		this.fileId = fileId;
		this.data = new Blob(content);
		this.index = index;
	}

    @Override
	public Object getEntityId() {
		return id;
	}
    
    public void copy(FileChunkEntity entity) {
    	setContent(entity.getContent());
    	setFileId(entity.getFileId());
    	setIndex(entity.getIndex());
    }
    
	public byte[] getContent() {
		return data.getBytes();
	}
	
	public void setContent(byte[] content) {
		this.data = new Blob(content);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	public boolean equals(Object object) {
		if (object instanceof FileChunkEntity) {
			FileChunkEntity entity = (FileChunkEntity)object;
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
