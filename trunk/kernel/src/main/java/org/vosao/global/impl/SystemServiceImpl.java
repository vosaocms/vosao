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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.vosao.common.VosaoContext;
import org.vosao.global.CacheService;
import org.vosao.global.FileCache;
import org.vosao.global.SystemService;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.quota.QuotaService;
import com.google.appengine.api.quota.QuotaServiceFactory;

public class SystemServiceImpl implements SystemService, Serializable {

	private static final Log log = LogFactory.getLog(SystemServiceImpl.class);

	private CacheService cache;
	private VelocityEngine velocityEngine;
	private TransformerFactory xsltFactory;
	private Map<String, Transformer> transformers;
	private DatastoreService datastore;
	private FileCache fileCache;	
	
	public void init() {
		cache = new CacheServiceImpl();
        try {
            velocityEngine = new VelocityEngine("WEB-INF/velocity.properties");
			velocityEngine.init();
		} catch (Exception e) {
            log.error("Can't init velocity engine. " + e.getMessage());
		}
		xsltFactory = TransformerFactory.newInstance();
		transformers = new HashMap<String, Transformer>();
		datastore = DatastoreServiceFactory.getDatastoreService();
	}
	

	@Override
	public CacheService getCache() {
		return cache;
	}

	@Override
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	@Override
	public String render(String template, VelocityContext context) {
		StringWriter wr = new StringWriter();
		String log = "vm";
		try {
			getVelocityEngine().evaluate(context, wr, log, template);
			return wr.toString();
		} catch (ParseErrorException e) {
			return e.toString();
		} catch (MethodInvocationException e) {
			return e.toString();
		} catch (ResourceNotFoundException e) {
			return e.toString();
		} catch (IOException e) {
			return e.toString();
		}
	}

	@Override
	public Transformer getTransformer(String template) {
		String key = String.valueOf(template.hashCode());
		if (!transformers.containsKey(key)) {
			try {
				Transformer transformer = xsltFactory.newTransformer(
						new StreamSource(new ByteArrayInputStream(
								template.getBytes("UTF-8"))));
				transformers.put(key, transformer);				
			} catch (TransformerConfigurationException e) {
				log.error(e.getMessage());
				return null;
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage());
				return null;
			}
		}
		return transformers.get(key);
	}

	@Override
	public Queue getDefaultQueue() {
		return QueueFactory.getDefaultQueue();
	}

	@Override
	public Queue getQueue(String name) {
		return QueueFactory.getQueue(name);
	}

	@Override
	public DatastoreService getDatastore() {
		return datastore;
	}

	@Override
	public FileCache getFileCache() {
		if (fileCache == null) {
			fileCache = new FileCacheImpl(getCache());			
		}
		return fileCache;
	}

	@Override
	public QuotaService getQuotaService() {
		return QuotaServiceFactory.getQuotaService();
	}

	@Override
	public long getRequestCPUTimeSeconds() {
		VosaoContext ctx = VosaoContext.getInstance();
		long result = (System.currentTimeMillis() - ctx.getStartTime())/1000;
		if (ctx.getRequestCount() == 1) {
			result += 15;
		}
		return result;
	}

}
