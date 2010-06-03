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

package org.vosao.business.impl.mq;

import java.util.List;

import org.vosao.business.mq.AbstractMessage;
import org.vosao.business.mq.QueueSpeed;
import org.vosao.business.mq.Topic;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class ExportMessage extends AbstractMessage {

	public static class Builder {
		
		private String filename;
		private String currentFile;
		private int fileCounter;
		private String exportType;
		private List<Long> ids;
		private Long folderId;

		public ExportMessage create() {
			return new ExportMessage(filename, currentFile, fileCounter,
					exportType, ids, folderId);
		}

		public Builder setFilename(String filename) {
			this.filename = filename;
			return this;
		}

		public Builder setCurrentFile(String currentFile) {
			this.currentFile = currentFile;
			return this;
		}

		public Builder setFileCounter(int fileCounter) {
			this.fileCounter = fileCounter;
			return this;
		}

		public Builder setExportType(String exportType) {
			this.exportType = exportType;
			return this;
		}

		public Builder setIds(List<Long> ids) {
			this.ids = ids;
			return this;
		}

		public Builder setFolderId(Long folderId) {
			this.folderId = folderId;
			return this;
		}
		
	}
	
	
	private String filename;
	private String currentFile;
	private int fileCounter;
	private String exportType;
	private List<Long> ids;
	private Long folderId;
	
	private ExportMessage(String filename, String currentFile, int fileCounter,
			String exportType, List<Long> ids, Long folderId) {
		super();
		setTopic(Topic.EXPORT.name());
		setSpeed(QueueSpeed.LOW);
		this.filename = filename;
		this.currentFile = currentFile;
		this.fileCounter = fileCounter;
		this.exportType = exportType;
		this.ids = ids;
		this.folderId = folderId;
	}

	public String getFilename() {
		return filename;
	}

	public String getCurrentFile() {
		return currentFile;
	}

	public int getFileCounter() {
		return fileCounter;
	}

	public String getExportType() {
		return exportType;
	}

	public List<Long> getIds() {
		return ids;
	}

	public Long getFolderId() {
		return folderId;
	}
	
}
