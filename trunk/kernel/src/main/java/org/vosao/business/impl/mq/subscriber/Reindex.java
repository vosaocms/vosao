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

import org.datanucleus.util.StringUtils;
import org.vosao.business.impl.mq.AbstractSubscriber;
import org.vosao.business.mq.Message;
import org.vosao.business.mq.message.PageMessage;
import org.vosao.common.VosaoContext;
import org.vosao.entity.PageEntity;
import org.vosao.entity.helper.UserHelper;

/**
 * Reindex all pages.
 * 
 * @author Alexander Oleynik
 *
 */
public class Reindex extends AbstractSubscriber {

	public void onMessage(Message message) {
		try {
			VosaoContext.getInstance().setUser(UserHelper.ADMIN);
			PageMessage m = (PageMessage)message; 
			if (!StringUtils.isEmpty(m.getMessage()) 
					&& m.getMessage().equals("save")) {
				getBusiness().getSearchEngine().saveIndex();
			}
			if (StringUtils.isEmpty(m.getMessage())) {
				for (String url : m.getPages().keySet()) {
					for (Long id : m.getPages().get(url)) {
						PageEntity page = getDao().getPageDao().getById(id);
						getBusiness().getSearchEngine().updateIndex(page);
					}
				}
			}
		}
		catch(Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

}