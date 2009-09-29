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
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.vosao.servlet.FolderUtil;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FileEntity implements Serializable {

	private static final long serialVersionUID = 4L;

	private static final String[] IMAGE_EXTENSIONS = {"jpg","jpeg","png","ico",
	"gif"};

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;
	
	@Persistent
	private String title;
	
	@Persistent
	private String filename;	

	@Persistent
	private String folderId;	

	@Persistent
	private String mimeType;
    
	@Persistent
	private Date lastModifiedTime;
	
	@Persistent
	private int size;

	public FileEntity() {
	}
	
	public FileEntity(String aTitle, String aName, String aFolderId,
			String aMimeType, Date aMdttime, int aSize) {
		this();
		title = aTitle;
		filename = aName;
		folderId = aFolderId;
		mimeType = aMimeType;
		lastModifiedTime = aMdttime;
		size = aSize;
	}
	
	public void copy(final FileEntity entity) {
		setTitle(entity.getTitle());
		setFilename(entity.getFilename());
		setFolderId(entity.getFolderId());
		setLastModifiedTime(entity.getLastModifiedTime());
		setMimeType(entity.getMimeType());
		setSize(entity.getSize());
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

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date aLastModifiedTime) {
		this.lastModifiedTime = aLastModifiedTime;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isImage() {
		for (String ext : IMAGE_EXTENSIONS) {
			if (FolderUtil.getFileExt(getFilename()).equals(ext)) {
				return true;
			}
		}
		return false;
	}
	

}
