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

package org.vosao.servlet;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.labs.taskqueue.Queue;

public class SessionCleanTaskServlet extends BaseSpringServlet {

	public static final String SESSION_CLEAN_TASK_URL = "/_ah/queue/session_clean";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		clearSessions(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		clearSessions(request, response);
	}

	public void clearSessions(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		String mode = request.getParameter("mode");
		if (mode != null && mode.equals("start")) {
			addSessionCleanTask();
		}
		else {
	        DatastoreService datastore = getBusiness().getSystemService()
	        		.getDatastore(); 
	        Query query = new Query("_ah_SESSION");
	        PreparedQuery results = datastore.prepare(query);
	        int i = 0;
	        for (Entity session : results.asIterable()) {
	        	datastore.delete(session.getKey());
	        	i++;
	    		if (System.currentTimeMillis() - startTime > 24000) {
	    			addSessionCleanTask();
	    			logger.info("Deleted " + i + " sessions.");
	    			break;
	    		}
	        }
		}
	}
	
	private void addSessionCleanTask() {
		Queue queue = getSystemService().getQueue("session-clean");
		queue.add(url(SESSION_CLEAN_TASK_URL));
		logger.info("Added new session clean task");
	}
	
}
