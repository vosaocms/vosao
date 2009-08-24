package org.vosao.servlet;

import java.util.Collections;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import javax.cache.CacheStatistics;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class ClearSessionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(ClearSessionServlet.class);
	
	private void clearSessions() {
        DatastoreService datastore = DatastoreServiceFactory
        		.getDatastoreService();
        Query query = new Query("_ah_SESSION");
        PreparedQuery results = datastore.prepare(query);
        log.info("Deleting " + results.countEntities() + " sessions from data store");
        for (Entity session : results.asIterable()) {
            datastore.delete(session.getKey());
        }
	} 
	
	private void clearCache() throws CacheException {
        CacheFactory cacheFactory = CacheManager.getInstance()
        		.getCacheFactory();
        Cache cache = cacheFactory.createCache(Collections.emptyMap());
        CacheStatistics stats = cache.getCacheStatistics();
        log.info("Clearing " + stats.getObjectCount() + " objects in cache");
        cache.clear();
	} 

	@Override
	public void init() {
		try {
			super.init();
		} catch (ServletException e1) {
			e1.printStackTrace();
		}
		clearSessions();
		try {
			clearCache();
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}

}
