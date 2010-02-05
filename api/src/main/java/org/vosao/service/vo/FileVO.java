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

package org.vosao.service.vo;

import java.util.ArrayList;
import java.util.List;

import org.vosao.entity.FieldEntity;
import org.vosao.entity.FileEntity;

/**
 * Value object to be returned from services.
 * @author Alexander Oleynik
 */
public class FileVO {

	private FileEntity file;
	private String link;
	private boolean textFile;
	private boolean imageFile;
	private String content;
	
	public FileVO(final FileEntity entity) {
		file = entity;
	}

	public String getTitle() {
		return file.getTitle();
	}
	
	public String getName() {
		return file.getFilename();
	}

	public String getMimeType() {
		return file.getMimeType();
	}

	public String getFolderId() {
		return file.getFolderId();
	}
	
	public int getSize() {
		return file.getSize();
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public boolean isTextFile() {
		return textFile;
	}

	public void setTextFile(boolean text) {
		this.textFile = text;
	}

	public boolean isImageFile() {
		return imageFile;
	}

	public void setImageFile(boolean image) {
		this.imageFile = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
