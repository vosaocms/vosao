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

package org.vosao.global.impl;

import javax.cache.Cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.mq.Message;
import org.vosao.business.mq.Topic;
import org.vosao.business.mq.TopicSubscriber;
import org.vosao.business.mq.message.SimpleMessage;
import org.vosao.common.VosaoContext;
import org.vosao.entity.FileEntity;
import org.vosao.global.FileCache;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class FileCacheImpl implements FileCache {

	private static final Log logger = LogFactory.getLog(FileCacheImpl.class);
	
	private Cache cache;
	
	public FileCacheImpl(Cache cache) {
		this.cache = cache;
		VosaoContext.getInstance().getMessageQueue().subscribe(
			Topic.FILE_CHANGED.name(), new TopicSubscriber() {

				@Override
				public void onMessage(Message message) {
					SimpleMessage msg = (SimpleMessage)message;
					remove(msg.getMessage());
					logger.debug("Clear file cache " + msg.getMessage());
				}
				
			});
	}
	
	@Override
	public byte[] getContent(String path) {
		return (byte[])cache.get("data:" + path);
	}

	@Override
	public FileEntity getFile(String path) {
		return (FileEntity)cache.get(path);
	}

	@Override
	public boolean isInCache(String path) {
		return cache.containsKey(path) && cache.containsKey("data:" + path);
	}

	@Override
	public boolean isInPublicCache(String path) {
		return isInCache(path) && cache.containsKey("public:" + path);
	}

	@Override
	public void put(String path, FileEntity file, byte[] content) {
		cache.put(path, file);
		cache.put("data:" + path, content);
	}

	@Override
	public void makePublic(String path) {
		cache.put("public:" + path, true);
	}

	@Override
	public void remove(String path) {
		cache.remove(path);
		cache.remove("data:" + path);
		cache.remove("public:" + path);
	}

}
