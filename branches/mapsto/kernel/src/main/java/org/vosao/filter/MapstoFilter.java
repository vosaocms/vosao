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

package org.vosao.filter;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.mapsto.Journal;
import org.mapsto.Table;
import org.vosao.utils.StreamUtil;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * Vosao context creation and request injection.
 * 
 * @author Alexander Oleynik
 *
 */
public class MapstoFilter extends AbstractFilter implements Filter {

	private static final String MAPSTO = "/_ah/cron/mapsto";
	
    public MapstoFilter() {
    	super();
    }
  
    public void doFilter(ServletRequest request, ServletResponse response, 
    		FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
    	String sessionId = httpRequest.getSession(true).getId();
    	long start = System.nanoTime();
    	loadTables(sessionId);
        String url = httpRequest.getServletPath();
    	logger.info("mapsto load " + (System.nanoTime() - start)/1000000000.0);
        if (url.equals(MAPSTO)) {
        	applyJournalAndSave();
        }
        else {
        	chain.doFilter(request, response);
        	saveChanges(sessionId);
        }
    }

	private void loadTables(String sessionId) {
		getBusiness().getDao().getMapsto().clear();
		getBusiness().getDao().getMapsto().clearJournal();
		byte[] data = getMapsto();
		if (data != null) {
			getDao().getMapsto().setTables((Map<String, Table>)StreamUtil
					.toObject(data));
		}
		applyJournal(getJournalEntities());
	}
	
	private byte[] getMapsto() {
		byte[] result = (byte[])getBusiness().getSystemService().getCache()
				.get("mapsto");
		if (result == null) {
			result = loadMapsto();
		}
		return result;
	}

	private byte[] loadMapsto() {
		Entity entity = getMapstoEntity();
		if (entity == null) {
			return null;
		}
		return ((Blob)entity.getProperty("data")).getBytes();
	}
	
	private Entity getMapstoEntity() {
		Query query = new Query("mapsto");
		PreparedQuery p = getSystemService().getDatastore().prepare(query);
		int limit = p.countEntities() > 0 ? p.countEntities() : 1;
		List<Entity> list = p.asList(withLimit(limit));
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
	
	private void saveChanges(String sessionId) {
		if (!getDao().getMapsto().isChanged()) {
			return;
		}
		long timestamp = ((System.currentTimeMillis() >> 4) << 8) | 
				(System.nanoTime() & 0xff);
		String key = "mapstoJournal:" + String.valueOf(timestamp);
		byte[] data = StreamUtil.toBytes(getBusiness().getDao().getMapsto()
				.getJournal());
		getSystemService().getCache().put(key, data);

		Entity entity = new Entity("mapstoJournal");
		entity.setProperty("session", sessionId);
		entity.setProperty("timestamp", timestamp);
		entity.setProperty("key", key);
		getSystemService().getDatastore().put(entity);
	}
	
	private void applyJournalAndSave() {
		List<Entity> entities = getJournalEntities();
		applyJournal(entities);
		getSystemService().getDatastore().delete(getKeys(entities));
		byte[] data = StreamUtil.toBytes(getDao().getMapsto().getTables());
		Entity entity = getMapstoEntity();
		if (entity == null) {
			entity = new Entity("mapsto");
		}
		// TODO limit 1MB
		entity.setProperty("data", new Blob(data));
		getSystemService().getDatastore().put(entity);
		getSystemService().getCache().put("mapsto", data);
	}

	private void applyJournal(List<Entity> entities) {
		for (Entity entity: entities) {
			Journal journal = getJournal(entity);
			if (journal != null) {
				getDao().getMapsto().apply(journal);
			}
		}
		getDao().getMapsto().clearJournal();
	}
	
	private Journal getJournal(Entity entity) {
		String key = (String)entity.getProperty("key");
		byte[] data = (byte[])getSystemService().getCache().get(key);
		if (data == null) {
			return null;
		}
		return (Journal)StreamUtil.toObject(data);
	}
	
	private List<Entity> getJournalEntities() {
		Query query = new Query("mapstoJournal");
		query.addSort("timestamp");
		PreparedQuery p = getSystemService().getDatastore().prepare(query);
		int limit = p.countEntities() > 0 ? p.countEntities() : 1;
		return p.asList(withLimit(limit));
	}
	
	private List<Key> getKeys(List<Entity> entities) {
		List<Key> result = new ArrayList<Key>();
		for (Entity entity : entities) {
			result.add(entity.getKey());
		}
		return result;
	}

	private List<Entity> getUserJournal(String sessionId) {
		Query query = new Query("mapstoJournal");
		query.addFilter("session", FilterOperator.EQUAL, sessionId);
		query.addSort("timestamp");
		PreparedQuery p = getSystemService().getDatastore().prepare(query);
		int limit = p.countEntities() > 0 ? p.countEntities() : 1;
		return p.asList(withLimit(limit));
	}
}
