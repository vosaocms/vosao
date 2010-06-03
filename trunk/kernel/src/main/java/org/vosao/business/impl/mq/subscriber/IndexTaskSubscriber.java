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
import org.vosao.business.mq.message.SimpleMessage;
import org.vosao.common.VosaoContext;
import org.vosao.entity.PageEntity;
import org.vosao.entity.helper.UserHelper;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class IndexTaskSubscriber extends AbstractSubscriber {

	public void onMessage(Message message) {
		SimpleMessage msg = (SimpleMessage)message;
		String id = msg.getMessage();
		try {
			VosaoContext.getInstance().setUser(UserHelper.ADMIN);
			PageEntity page = getDao().getPageDao().getById(Long.valueOf(id));
			if (page != null) {
				getBusiness().getSearchEngine().updateIndex(page);
				getBusiness().getSearchEngine().saveIndex();
			}
		}
		catch(Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
}