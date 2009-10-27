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

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Collections;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.vosao.global.SystemService;

public class SystemServiceImpl implements SystemService, Serializable {

	private static final Log log = LogFactory.getLog(SystemServiceImpl.class);

	private Cache cache;
	private VelocityEngine velocityEngine;
	
	public void init() {
		try {
            cache = CacheManager.getInstance().getCacheFactory().createCache(
            		Collections.emptyMap());
        } catch (CacheException e) {
            log.error("Can't init cache manager. " + e.getMessage());
        }
        velocityEngine = new VelocityEngine();
        try {
			velocityEngine.init();
		} catch (Exception e) {
            log.error("Can't init velocity engine. " + e.getMessage());
		}
	}
	

	@Override
	public Cache getCache() {
		return cache;
	}

	@Override
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	@Override
	public String render(String template, VelocityContext context) {
		StringWriter wr = new StringWriter();
		String log = null;
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
	
	
}
