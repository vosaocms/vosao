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

package org.vosao.global;

import javax.xml.transform.Transformer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.vosao.entity.PageEntity;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.quota.QuotaService;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public interface SystemService {
	
	CacheService getCache();
	
	FileCache getFileCache();
	
	PageCache getPageCache();
	
	Queue getDefaultQueue();

	Queue getQueue(String name);

	VelocityEngine getVelocityEngine();
	
	QuotaService getQuotaService();

	/**
	 * Render velocity template in specified context. 
	 * @param template - template to render.
	 * @param content - context to use.
	 * @return rendered html.
	 */
	String render(final String template, final VelocityContext context);
	
	Transformer getTransformer(String template);
	
	DatastoreService getDatastore();
	
	/**
	 * Time duration of current request in seconds. Used in tasks to control
	 * 30 sec limit.
	 * @return request seconds.
	 */
	long getRequestCPUTimeSeconds();
	
	/**
	 * Render wiki text. 
	 * @param template - template to render.
	 * @param page - page to use.
	 * @return rendered html.
	 */
	String renderWiki(String template, PageEntity page);
	
}
