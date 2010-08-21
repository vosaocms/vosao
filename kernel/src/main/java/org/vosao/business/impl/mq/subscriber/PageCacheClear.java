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
import org.vosao.business.mq.QueueSpeed;
import org.vosao.business.mq.Topic;
import org.vosao.business.mq.message.PageMessage;
import org.vosao.entity.PageDependencyEntity;
import org.vosao.utils.FolderUtil;

/**
 * Clear cache for all dependent pages.
 * 
 * @author Alexander Oleynik
 *
 */
public class PageCacheClear extends AbstractSubscriber {

	public void onMessage(Message message) {
		PageMessage msg = (PageMessage)message;
		try {
			for (String url : msg.getPages().keySet()) {
				clearDependency(url);			
			}
		}
		catch(Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	private void clearDependency(String url) {
		String dependency = "";
		String[] parts = FolderUtil.getPathChain(url);
		PageMessage message = new PageMessage(Topic.PAGE_CACHE_CLEAR, 
				QueueSpeed.LOW);
		
		for (String part : parts) {
			dependency += "/" + part;
			for (PageDependencyEntity entity : getDao().getPageDependencyDao()
					.selectByDependency(dependency)) {

				if (getBusiness().getSystemService().getPageCache()
						.contains(entity.getPage())) {
				
					getBusiness().getSystemService().getPageCache().remove(
							entity.getPage());
					
					message.addPage(entity.getPage(), -1L);
				}
			}
		}
		getBusiness().getMessageQueue().publish(message);
	}
	
}