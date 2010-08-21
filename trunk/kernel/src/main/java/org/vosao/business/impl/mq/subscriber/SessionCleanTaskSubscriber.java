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

package org.vosao.business.impl.mq.subscriber;

import org.vosao.business.impl.mq.AbstractSubscriber;
import org.vosao.business.mq.Message;
import org.vosao.business.mq.Topic;
import org.vosao.business.mq.message.SimpleMessage;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class SessionCleanTaskSubscriber extends AbstractSubscriber {

	public void onMessage(Message message) {
		SimpleMessage msg = (SimpleMessage) message;
		String mode =  msg.getMessage();
		if (mode != null && mode.equals("start")) {
			addSessionCleanTask();
		}
		else {
	        DatastoreService datastore = getBusiness().getSystemService()
	        		.getDatastore(); 
	        Query query = new Query("_ah_SESSION");
	        query.addFilter("_expires", FilterOperator.LESS_THAN, 
	        		System.currentTimeMillis());
	        PreparedQuery results = datastore.prepare(query);
	        int i = 0;
	        boolean end = true;
	        for (Entity session : results.asIterable()) {
	        	datastore.delete(session.getKey());
	        	i++;
	    		if (getBusiness().getSystemService()
	    				.getRequestCPUTimeSeconds() > 25) {
	    			addSessionCleanTask();
	    			end = false;
	    			break;
	    		}
	        }
			logger.info("Deleted " + i + " sessions.");
	        if (end) {
	        	logger.info("Finished session cleaning.");
	        }
		}
	}
	
	private void addSessionCleanTask() {
		getMessageQueue().publish(new SimpleMessage(Topic.SESSION_CLEAN.name(), 
				null));
		logger.info("Added new session clean task");
	}
	
}
