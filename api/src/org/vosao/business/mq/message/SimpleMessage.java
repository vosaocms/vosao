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

package org.vosao.business.mq.message;

import org.vosao.business.mq.AbstractMessage;
import org.vosao.business.mq.QueueSpeed;
import org.vosao.business.mq.Topic;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class SimpleMessage extends AbstractMessage {

	private String message;
	
	public SimpleMessage(String topic, String msg) {
		super();
		setTopic(topic);
		message = msg;
	}

	public SimpleMessage(Topic topic, String msg) {
		this(topic.name(), msg);
	}

	public SimpleMessage(Class clazz) {
		this("simple.command", "");
		setCommandClassName(clazz.getName());
	}

	public SimpleMessage(Topic topic, String msg, QueueSpeed speed) {
		this(topic.name(), msg);
		setSpeed(speed);
	}

	public SimpleMessage(Topic topic) {
		this(topic.name(), null);
	}

	public String getMessage() {
		return message;
	}
}
