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

package org.vosao.business.impl.mq;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.impl.mq.subscriber.ExportTaskSubscriber;
import org.vosao.business.impl.mq.subscriber.FileChangedSubscriber;
import org.vosao.business.impl.mq.subscriber.ImportTaskSubscriber;
import org.vosao.business.impl.mq.subscriber.IndexChangedPages;
import org.vosao.business.impl.mq.subscriber.IndexDeletedPages;
import org.vosao.business.impl.mq.subscriber.Reindex;
import org.vosao.business.impl.mq.subscriber.SessionCleanTaskSubscriber;
import org.vosao.business.mq.Message;
import org.vosao.business.mq.MessageQueue;
import org.vosao.business.mq.QueueSpeed;
import org.vosao.business.mq.Topic;
import org.vosao.business.mq.TopicSubscriber;
import org.vosao.common.VosaoContext;
import org.vosao.global.SystemService;
import org.vosao.servlet.MessageQueueTaskServlet;
import org.vosao.utils.StreamUtil;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.repackaged.com.google.common.util.Base64;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class MessageQueueImpl implements MessageQueue {

	private static final Log logger = LogFactory.getLog(MessageQueueImpl.class);
	
	private Map<String, List<TopicSubscriber>> subscribers = 
		new HashMap<String, List<TopicSubscriber>>();
	
	public MessageQueueImpl() {
		registerSubscribers();
	}
	
	private void registerSubscribers() {
		subscribe(Topic.FILE_CHANGED.name(), new FileChangedSubscriber());
		subscribe(Topic.EXPORT.name(), new ExportTaskSubscriber());
		subscribe(Topic.SESSION_CLEAN.name(), new SessionCleanTaskSubscriber());
		subscribe(Topic.IMPORT.name(), new ImportTaskSubscriber());
		subscribe(Topic.PAGES_DELETED.name(), new IndexDeletedPages());
		subscribe(Topic.PAGES_CHANGED.name(), new IndexChangedPages());
		subscribe(Topic.REINDEX.name(), new Reindex());
	}

	private SystemService getSystemService() {
		return VosaoContext.getInstance().getBusiness().getSystemService();
	}
	
	private String getQueueName(Message message) {
		if (message.getSpeed() == null) {
			return "mq-high";
		}
		else if (message.getSpeed().equals(QueueSpeed.HIGH)) {
			return "mq-high";
		}
		else if (message.getSpeed().equals(QueueSpeed.MEDIUM)) {
			return "mq-medium";
		}
		else if (message.getSpeed().equals(QueueSpeed.LOW)) {
			return "mq-low";
		}
		return "mq-high";
	}
	
	@Override
	public void publish(Message message) {
		Queue queue = getSystemService().getQueue(getQueueName(message));
		if (message.getTopic() == null) {
			logger.error("Topic is null in message " + message);
			return;
		}
		queue.add(url(MessageQueueTaskServlet.MQ_URL)
				.param("topic", message.getTopic())
				.param("message", Base64.encode(StreamUtil.toBytes(message))));
	}

	@Override
	public void subscribe(String topic, TopicSubscriber subscriber) {
		if (!subscribers.containsKey(topic)) {
			subscribers.put(topic, new ArrayList<TopicSubscriber>());
		}
		if (!subscribers.get(topic).contains(subscriber)) {
			subscribers.get(topic).add(subscriber);
		}
	}

	@Override
	public void unsubscribe(String topic, TopicSubscriber subscriber) {
		if (subscribers.containsKey(topic)) {
			if (subscribers.get(topic).contains(subscriber)) {
				subscribers.get(topic).remove(subscriber);
			}
		}
	}

	@Override
	public List<TopicSubscriber> getSubscribers(String topic) {
		if (subscribers.containsKey(topic)) {
			return Collections.unmodifiableList(subscribers.get(topic));
		}
		return Collections.EMPTY_LIST;
	}

}
