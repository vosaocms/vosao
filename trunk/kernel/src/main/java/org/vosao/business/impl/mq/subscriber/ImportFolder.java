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
import org.vosao.common.VfsNode;

/**
 * Import folder.
 * 
 * @author Alexander Oleynik
 *
 */
public class ImportFolder extends AbstractSubscriber {

	public void onMessage(Message message) {
		SimpleMessage msg = (SimpleMessage)message;
		String path = msg.getMessage();
		getBusiness().getFolderBusiness().createFolder(path);
		VfsNode node = VfsNode.find(path);
		if (node == null) {
			logger.error("VFS node not found " + path);
			return;
		}
		for (VfsNode child : node.getChildren()) {
			if (child.isDirectory()) {
				getBusiness().getMessageQueue().publish(new SimpleMessage(
						Topic.IMPORT_FOLDER, child.getPath()));
			}
			else {
				getBusiness().getMessageQueue().publish(new SimpleMessage(
						Topic.IMPORT_FILE, child.getPath()));
			}
		}
	}

}