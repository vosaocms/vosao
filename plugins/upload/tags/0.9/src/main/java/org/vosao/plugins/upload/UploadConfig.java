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

package org.vosao.plugins.upload;

public class UploadConfig {

	private String folder;
	private String secretKey;
	private boolean useKey;
	private String subscriberClass;
	private Integer maxSize;
	
	public UploadConfig() {	
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String value) {
		secretKey = value;
	}

	public boolean isUseKey() {
		return useKey;
	}

	public void setUseKey(boolean value) {
		useKey = value;
	}

	public String getSubscriberClass() {
		return subscriberClass;
	}

	public void setSubscriberClass(String subscriberClass) {
		this.subscriberClass = subscriberClass;
	}

	public Integer getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	
}
