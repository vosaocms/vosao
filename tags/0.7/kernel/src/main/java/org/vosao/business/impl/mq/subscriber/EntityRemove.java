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

package org.vosao.business.impl.mq.subscriber;

import org.vosao.business.impl.mq.AbstractSubscriber;
import org.vosao.business.mq.Message;
import org.vosao.business.mq.QueueSpeed;
import org.vosao.business.mq.Topic;
import org.vosao.business.mq.message.SimpleMessage;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class EntityRemove extends AbstractSubscriber {

	public void onMessage(Message message) {
		SimpleMessage msg = (SimpleMessage) message;
		String kind =  msg.getMessage();
		DatastoreService datastore = getBusiness().getSystemService()
	        		.getDatastore(); 
        Query query = new Query(kind);
        PreparedQuery results = datastore.prepare(query);
        int i = 0;
        boolean end = true;
        for (Entity entity : results.asIterable()) {
        	datastore.delete(entity.getKey());
        	i++;
    		if (getBusiness().getSystemService()
    				.getRequestCPUTimeSeconds() > 25) {
    			addEntityRemoveTask(kind);
    			end = false;
    			break;
    		}
        }
		logger.info("Deleted " + i + " entities " + kind);
        if (end) {
        	logger.info("Finished entity removing.");
        }
	}
	
	private void addEntityRemoveTask(String kind) {
		getMessageQueue().publish(new SimpleMessage(Topic.ENTITY_REMOVE, 
				kind, QueueSpeed.LOW));
		logger.info("Added new entity remove task " + kind);
	}
	
}
