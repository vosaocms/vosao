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

package org.vosao.business.mq.message;

import org.vosao.business.mq.AbstractMessage;
import org.vosao.business.mq.QueueSpeed;
import org.vosao.business.mq.Topic;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class ImportMessage extends AbstractMessage {

	public static class Builder {
		private String filename;
		private int start;
		private String currentFile;
		private int fileCounter;
		
		public ImportMessage create() {
			return new ImportMessage(filename, start, currentFile, fileCounter);
		}
		
		public Builder setFilename(String filename) {
			this.filename = filename;
			return this;
		}
		
		public Builder setStart(int start) {
			this.start = start;
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
		
	}
	
	private String filename;
	private int start;
	private String currentFile;
	private int fileCounter;

	public ImportMessage(String filename, int start, String currentFile,
			int fileCounter) {
		super();
		setTopic(Topic.IMPORT.name());
		setSpeed(QueueSpeed.LOW);
		this.filename = filename;
		this.start = start;
		this.currentFile = currentFile;
		this.fileCounter = fileCounter;
	}

	public String getFilename() {
		return filename;
	}

	public int getStart() {
		return start;
	}

	public String getCurrentFile() {
		return currentFile;
	}

	public int getFileCounter() {
		return fileCounter;
	}
	
	
}
