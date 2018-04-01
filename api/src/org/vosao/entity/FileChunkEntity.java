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
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;

public class FileChunkEntity extends BaseEntityImpl {
	
	private static final long serialVersionUID = 2L;

	private byte[] content;
	private int index;
	private Long fileId;
	
    public FileChunkEntity() {
    }
    
    public FileChunkEntity(Long fileId, byte[] content, int index) {
		this();
		this.fileId = fileId;
		this.content = content;
		this.index = index;
	}

    @Override
    public void load(Entity entity) {
    	super.load(entity);
    	content = getBlobProperty(entity, "content");
    	index = getIntegerProperty(entity, "index", 0);
    	fileId = getLongProperty(entity, "fileId");
    }
    
    @Override
    public void save(Entity entity) {
    	super.save(entity);
    	setProperty(entity, "content", content);
    	setProperty(entity, "index", index, false);
    	setProperty(entity, "fileId", fileId, true);
    }

    public byte[] getContent() {
		return content;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	
}
